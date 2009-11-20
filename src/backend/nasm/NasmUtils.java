package backend.nasm;

import java.util.*;
import analysis.symboltable.*;
import analysis.tac.variables.*;

public final class NasmUtils {
  public static void calculateOffsets() {
    SymbolTable symT = SymbolTable.getInstance();

    for (ClassDescriptor c : symT.getClassDescriptors()) {
      int offset = 4; // from instance base address

      for (VariableDescriptor v : c.getVariableDescriptors()) {
        v.setOffset(offset);
        offset += 4;
      }

      c.setSize(offset);

      for (MethodDescriptor m : c.getMethodDescriptors()) {
        offset = 8; // from ebp (+)

        for (VariableDescriptor param : m.getParameters()) {
          param.setOffset(offset);
          offset += 4;
        }

        offset = -4; // from ebp (-)

        for (VariableDescriptor local : m.getLocalVars()) {
          local.setOffset(offset);
          offset -= 4;
        }
      }
    }
  }

  public static List<VirtualTable> buildVirtualTables() {
    SymbolTable symT = SymbolTable.getInstance();
    Collection<ClassDescriptor> classes = symT.getClassDescriptors();

    List<VirtualTable> vtl = new ArrayList<VirtualTable>(classes.size());

    for (ClassDescriptor c : classes) {
      VirtualTable vt = new VirtualTable(
        c.getName(), c.getAccessibleMethodLabels()
      );
      
      vtl.add(vt);
    }

    return vtl;
  }

  public static String varHandle(TAVariable var, boolean isSource) {
    return varHandle(
      var, isSource, ANY
    );
  }

  public static String varHandle(
        TAVariable var, boolean isSource, boolean needsReg) {

    return varHandle(var, isSource, needsReg, emptySet());
  }

  public static String varHandle(
        TAVariable var, boolean isSource, boolean needsReg,
        Set<String> forbiddenRegs) {

    if (isSource) {
      if (needsReg)
        return getSourceVarHandleOnRegister(var, forbiddenRegs);
      else
        return getSourceVarHandle(var, forbiddenRegs);
    }
    else {
      if (var instanceof TALocalVar) {
        String varN = var.toString();
        return pool.getRegForDestiny(varN, forbiddenRegs);
      }

      return getSpecialVarHandle(var, forbiddenRegs);
    }
  }

  private static String getSourceVarHandleOnRegister(
        TAVariable var, Set<String> forbiddenReg) {

    String handle = getSourceVarHandle(var, forbiddenReg);

    if (isMemoryHandle(handle)) {
      List<String> regs = RegisterPool.registerNames;

      if (var instanceof TALocalVar) {
        String reg = pool.minSpills(var.toString(), forbiddenReg);
        pool.prepareSource(reg, var.toString());
        return reg;
      }

      for (int i = regs.size()-1; i >= 0; --i) {
        String reg = regs.get(i);
        if (forbiddenReg.contains(reg)) continue;
        pool.spillRegister(reg);
        pool.emit(Nasm.OP.make("mov " + reg + ", " + handle));
        return reg;
      }
    }

    return handle;
  }

  private static String getSourceVarHandle(
          TAVariable var, Set<String> forbiddenRegs) {

    if (var instanceof TALocalVar) {
      String reg = pool.getRegForSource(var.toString(), forbiddenRegs);
      pool.prepareSource(reg, var.toString());
      return reg;
    }

    return getSpecialVarHandle(var, forbiddenRegs);
  }

  private static String getSpecialVarHandle(
          TAVariable var, Set<String> forbiddenRegs) {
    if (var instanceof TAConstantVar) {
      return "dword " + var.toString();
    }
    else if (var instanceof TAFieldVar) {
      ClassDescriptor classD = methodD.getClassDescriptor();
      VariableDescriptor varD = classD.getVarInScope(var.toString());
      return "[edx+" + varD.getOffset() + "]";
    }
    else if (var instanceof TAThisVar) {
      return "edx";
    }
    else if (var instanceof TAArrayCellVar) {
      TAArrayCellVar cv = (TAArrayCellVar)var;
      TAVariable arrayV = cv.getArrayVar();
      TAVariable indexV = cv.getIndexVar();
      String arrayN = arrayV.toString();
      String indexN = indexV.toString();

      if (!(cv.getArrayVar().isLocalVar()
            || cv.getArrayVar().isConstant())
         ||
          !(cv.getIndexVar().isLocalVar()
            || cv.getIndexVar().isConstant()))
        throw new IllegalArgumentException("NasmUtils::specialVarHandle");

      RegisterPool.VarGenDescriptor arrayD = pool.varDescriptor(
        arrayV.toString()
      );

      String indexHandle = "";

      if (arrayD.size() == 0) {
        String memD = arrayD.getMemoryId(arrayN);
        pool.emit(Nasm.OP.make("mov esi, [" + memD + "]"));
      } else {
        String reg = arrayD.iterator().next();
        pool.emit(Nasm.OP.make("mov esi, " + reg));
      }

      RegisterPool.VarGenDescriptor indexD = pool.varDescriptor(
        indexV.toString()
      );

      if (indexV.isConstant()) {
        indexHandle = indexV.toString();
      } else if (indexD.size() == 0) {
        String memD = indexD.getMemoryId(indexN);
        pool.emit(Nasm.OP.make("mov edi, [" + memD + "]"));
        indexHandle = "edi";
      } else {
        String reg = indexD.iterator().next();
        pool.emit(Nasm.OP.make("mov edi, " + reg));
        indexHandle = "edi";
      }

      return String.format("dword [esi+4*%s+4]", indexHandle);
    }

    return null;
  }

  public static boolean isMemoryHandle(String handle) {
    return handle.startsWith("[");
  }

  public static boolean isRegister(String handle) {
    return RegisterPool.registerNames.contains(handle);
  }

  public static Set<String> emptySet() {
    return new HashSet<String>();
  }

  public static Set<String> unitSet(String str) {
    Set<String> s = emptySet();
    s.add(str);
    return s;
  }

  public static void setRegisterPool(RegisterPool p) {
    pool = p;
  }

  public static void setMethodDescriptor(MethodDescriptor md) {
    methodD = md;
  }

  private static RegisterPool pool;
  private static MethodDescriptor methodD;

  public static final String labelPad = " ";
  public static final String stmtPad = "   ";
  public static final boolean SOURCE = true;
  public static final boolean DESTINY = false;
  public static final boolean ON_REGISTER = true;
  public static final boolean ANY = false;
}
