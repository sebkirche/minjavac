package backend.nasm;

import java.util.List;
import analysis.tac.*;
import analysis.tac.instructions.*;
import analysis.symboltable.MethodDescriptor;

public class TABasicBlockEmitter implements TABasicBlockVisitor {
  private List<NasmInstruction> code;
  private RegisterPool pool;
  private MethodDescriptor methodD;
  private int numParams;
  private boolean savedEbx, savedEcx;

  public TABasicBlockEmitter(List<NasmInstruction> c) {
    code = c;
  }

  private void emit(NasmInstruction i) {
    code.add(i);
  }

  public void visit(TABasicBlock block, MethodDescriptor m) {
    methodD = m;
    pool = new RegisterPool(methodD, code);

    for (Label l : block.labels())
      emit(Nasm.LABEL.make(l.toString()));

    for (TAInstruction i : block.instructions()) {
      pool.setCurrentInstruction(i);
      i.accept(this);
    }

    pool.saveForExit(block);
  }

  public void visit(Action action) {
    switch (action.getOpcode()) {
      case SAVE_CTX:
        if (savedEbx = pool.regNeedSaving("ebx"))
          emit(Nasm.OP.make("push ebx"));
        else
          pool.spillRegister("ebx");

        if (savedEcx = pool.regNeedSaving("ecx"))
          emit(Nasm.OP.make("push ecx"));
        else
          pool.spillRegister("ecx");

        pool.spillRegister("eax");
        emit(Nasm.OP.make("push edx"));
        numParams = 0;

      case LOAD_CTX:
        emit(Nasm.OP.make("add esp, " + 4 * numParams));
        emit(Nasm.OP.make("pop edx"));

        if (savedEcx)
          emit(Nasm.OP.make("pop ecx"));

        if (savedEbx)
          emit(Nasm.OP.make("pop ebx"));
    }
  }

  public void visit(ParameterSetup param) {
    ++numParams;

    String paramHandle = NasmUtils.getVariableHandle(
      param.getParameter(), pool, methodD
    );

    if (NasmUtils.isMemoryHandle(paramHandle))
      paramHandle = "dword " + paramHandle;

    emit(Nasm.OP.make("push " + paramHandle));
  }

  public void visit(Label label) {
    emit(Nasm.LABEL.make(label.toString()));
  }

  public void visit(Jump jump) {
    if (jump.isConditionalJump()) {
      ConditionalJump cj = (ConditionalJump)jump;

      String handleA = NasmUtils.getVariableHandleOnRegister(
        cj.getA(), pool, methodD, ""
      );

      String handleB = null;

      if (cj.getB() != null) {
        handleB = NasmUtils.getVariableHandle(cj.getB(), pool, methodD);
      } else{
        handleB = "0";
      }

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
          jump_op = "jb"; break;
        case GREATER_OR_EQUAL:
          jump_op = "jae"; break;
      }

      emit(Nasm.OP.make(jump_op + " " + cj.getTarget()));
    }
    else {
      emit(Nasm.OP.make("goto " + jump.getTarget()));
    }
  }

  public void visit(Return returnInstr) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public void visit(Copy copy) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Operation operation) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(PrintInstruction print) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ProcedureCall proc) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
