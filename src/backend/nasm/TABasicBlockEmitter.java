package backend.nasm;

import analysis.symboltable.ClassDescriptor;
import java.util.*;
import analysis.tac.*;
import analysis.tac.variables.*;
import analysis.tac.instructions.*;
import static backend.nasm.NasmUtils.*;
import analysis.symboltable.MethodDescriptor;
import analysis.symboltable.SymbolTable;

public class TABasicBlockEmitter implements TABasicBlockVisitor {
  private TABasicBlock block;
  private List<NasmInstruction> code;
  private List<VirtualTable> vtl;
  private RegisterPool pool;
  private MethodDescriptor methodD;
  private int numParams;
  private boolean savedEbx, savedEcx, savedExit;

  public TABasicBlockEmitter(List<NasmInstruction> c, List<VirtualTable> l) {
    code = c;
    vtl = l;
  }

  private void emit(NasmInstruction i) {
    /*TAInstruction current = pool.getCurrentInstruction();

    if (current != null)
      i.setComment(current.toString());*/

    code.add(i);
  }

  public void visit(TABasicBlock b, MethodDescriptor m) {
    block = b;
    methodD = m;
    pool = new RegisterPool(methodD, code);

    NasmUtils.setRegisterPool(pool);
    NasmUtils.setMethodDescriptor(methodD);

    for (Label l : block.labels())
      emit(Nasm.LABEL.make(l.toString()));

    savedExit = false;

    for (TAInstruction i : block.instructions()) {
      pool.setCurrentInstruction(i);
      i.accept(this);
    }

    if (!savedExit)
      pool.saveForExit(block);
  }

  public void visit(Action action) {
    switch (action.getOpcode()) {
      case SAVE_CTX:
      case SAVE_C_CTX:
        emit(Nasm.OP.make("push edx"));
        numParams = 0;
        break;

      case LOAD_CTX:
      case LOAD_C_CTX:
        if (numParams != 0)
          emit(Nasm.OP.make("add esp, " + 4 * numParams));
        
        emit(Nasm.OP.make("pop edx"));
        break;
    }
  }

  public void visit(ParameterSetup param) {
    if (param.getParameter() instanceof TAThisReferenceVar) {
      TAThisReferenceVar trv = (TAThisReferenceVar)param.getParameter();
      String handle = varHandle(trv.getReference(), SOURCE);

      emit(Nasm.OP.make("mov edx, " + handle));
      return;
    }
    else if (param.getParameter() instanceof TALabelPointerVar) {
      TALabelPointerVar var = (TALabelPointerVar)param.getParameter();
      emit(Nasm.OP.make("push dword " + var.getLabel()));
      return;
    }
    
    ++numParams;

    String handle = varHandle(param.getParameter(), SOURCE);

    if (isMemoryHandle(handle))
      handle = "dword " + handle;

    emit(Nasm.OP.make("push " + handle));
  }

  public void visit(Label label) {
    emit(Nasm.LABEL.make(label.toString()));
  }

  public void visit(Jump jump) {
    pool.saveForExit(block);
    savedExit = true;

    if (jump.isConditionalJump()) {
      ConditionalJump cj = (ConditionalJump)jump;

      String handleA = varHandle(cj.getA(), SOURCE, ON_REGISTER), handleB;

      if (cj.getB() != null)
        handleB = varHandle(cj.getB(), SOURCE, ANY, unitSet(handleA));
      else
        handleB = "0";

      emit(Nasm.OP.make("cmp " + handleA + ", " + handleB));

      String jump_op = null;

      switch (cj.getCondition()) {
        case EQUAL:
        case IS_FALSE:
          jump_op = "je"; break;
        case NOT_EQUAL:
        case IS_TRUE:
          jump_op = "jne"; break;
        case LESS_THAN:
          jump_op = "jl"; break;
        case GREATER_OR_EQUAL:
          jump_op = "jge"; break;
      }

      emit(Nasm.OP.make(jump_op + " " + cj.getTarget()));
    }
    else {
      emit(Nasm.OP.make("jmp " + jump.getTarget()));
    }
  }

  public void visit(Return ret) {
    TAVariable var = ret.getReturnVariable();

    String handle = varHandle(var, SOURCE);

    if (!handle.equals("eax")) {
      pool.spillRegister("eax");
      emit(Nasm.OP.make("mov eax, " + handle));
    }

    emit(Nasm.OP.make("mov esp, ebp"));
    emit(Nasm.OP.make("pop ebp"));
    emit(Nasm.OP.make("ret"));
  }
  
  public void visit(Copy copy) {
    TAVariable s = copy.getSource();
    TAVariable d = copy.getDestiny();

    if (d.isLocalVar() && s.isLocalVar()) {
      String destN = d.toString();
      String srcReg = varHandle(s, SOURCE, ON_REGISTER);
      
      pool.killVar(destN);
      pool.varDescriptor(destN).setOnly(srcReg);
      pool.regDescriptor(srcReg).add(destN);
    }
    else if (!d.isLocalVar() && (s.isLocalVar() || s.isConstant())) {
      String srcReg = varHandle(s, SOURCE, ON_REGISTER);
      String destHandle = varHandle(d, DESTINY, ANY);
      emit(Nasm.OP.make("mov " + destHandle + ", " + srcReg));
    }
    else if (d.isLocalVar() && !s.isLocalVar()) {
      String destN = d.toString();
      String srcHandle = varHandle(s, SOURCE, ANY);

      String destReg = varHandle(d, DESTINY, ON_REGISTER);
      pool.prepareDestiny(destReg, d.toString());

      emit(Nasm.OP.make("mov " + destReg + ", " + srcHandle));

      pool.killVar(destN);
      pool.varDescriptor(destN).setOnly(destReg);
      pool.regDescriptor(destReg).setOnly(destN);
    }
    else {
      throw new IllegalArgumentException("visit::Copy");
    }
  }

  public void visit(Operation op) {
    TAVariable dest, a, b = null;
    String handleDest, handleA, handleB = null;

    dest = op.getDestiny();
    a = op.getA();
    b = op.getB();

    if (!dest.isLocalVar() ||
        !(a.isLocalVar() || a.isConstant()) ||
        (b != null && !(b.isLocalVar() || b.isConstant())))
      throw new IllegalArgumentException("visit::Operation");

    Set<String> fr = emptySet();
    handleA = varHandle(a, SOURCE, ON_REGISTER, fr);
    fr.add(handleA);

    if (b != null) {
      handleB = varHandle(b, SOURCE, ANY, fr);
      fr.add(handleB);
    }

    handleDest = varHandle(dest, DESTINY, ON_REGISTER, fr);
    pool.prepareDestiny(handleDest, dest.toString());

    switch (op.getOperation()) {
      case ADD:
        emit(Nasm.OP.make("mov " + handleDest + ", " + handleA));
        emit(Nasm.OP.make("add " + handleDest + ", " + handleB));
        break;

      case SUB:
        emit(Nasm.OP.make("mov " + handleDest + ", " + handleA));
        emit(Nasm.OP.make("sub " + handleDest + ", " + handleB));
        break;

      case MULT:
        emit(Nasm.OP.make("mov " + handleDest + ", " + handleA));
        emit(Nasm.OP.make("imul " + handleDest + ", " + handleB));
        break;

      case AND:
        emit(Nasm.OP.make("mov " + handleDest + ", " + handleA));
        emit(Nasm.OP.make("and " + handleDest + ", " + handleB));
        break;

      case NOT:
        emit(Nasm.OP.make("mov " + handleDest + ", 1"));
        emit(Nasm.OP.make("sub " + handleDest + ", " + handleA));
        break;

      case ARRAY_LENGTH:
        String i = String.format("mov %s, [%s]", handleDest, handleA);
        emit(Nasm.OP.make(i));
        break;
    }

    String destN = dest.toString();
    pool.killVar(destN);
    pool.varDescriptor(destN).setOnly(handleDest);
    pool.regDescriptor(handleDest).setOnly(destN);
  }
  
  public void visit(ProcedureCall proc) {
    pool.spillRegister("eax");
    pool.spillRegister("ebx");
    pool.spillRegister("ecx");
    pool.prepareDestiny("eax", proc.getDestiny().toString());

    String label = proc.getProcedure().getLabel();

    if (!label.startsWith("_") && !label.contains("@@")) {
      String procLabel = proc.getProcedure().getLabel();
      String classN = procLabel.split("@")[0].trim();
      String methodN = procLabel.split("@")[1].trim();
      VirtualTable vt = getVirtualTableForClass(classN);

      int methodPos = vt.getMethodPosition(methodN);

      emit(Nasm.OP.make("mov esi, [edx]"));
      emit(Nasm.OP.make("call [esi+" + 4*methodPos + "]"));
    }
    else {
      emit(Nasm.OP.make("call " + label));
    }
  }

  private VirtualTable getVirtualTableForClass(String classN) {
    SymbolTable symT = SymbolTable.getInstance();
    Collection<ClassDescriptor> cl = symT.getClassDescriptors();
    Iterator<ClassDescriptor> it = cl.iterator();

    int i;
    for (i = 0; ; ++i) {
      ClassDescriptor c = it.next();
      if (c.getName().equals(classN))
        break;
    }

    return vtl.get(i);
  }

  private void debug(String msg) {
    code.add(Nasm.COMMENT.make(msg));
  }
}
