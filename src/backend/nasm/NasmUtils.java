package backend.nasm;

import java.util.*;
import analysis.symboltable.*;
import analysis.tac.variables.*;

public class NasmUtils {
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

  public static String getVariableHandleOnRegister(
        TAVariable var, RegisterPool pool,
        MethodDescriptor methodD, String forbiddenReg) {

    String handle = getVariableHandle(var, pool, methodD);

    if (isMemoryHandle(handle)) {
      for (String reg : RegisterPool.registerNames) {
        if (reg.equals(forbiddenReg)) continue;

        if (var instanceof TALocalVar) {
          pool.prepareSource(reg, var.toString());
        }
        else {
          pool.spillRegister(reg);
          pool.emit(Nasm.OP.make("mov " + reg + ", " + handle));
        }

        return reg;
      }
    }

    return handle;
  }

  public static String getVariableHandle(
        TAVariable var, RegisterPool pool, MethodDescriptor methodD) {

    if (var instanceof TALocalVar) {
      return pool.getVariableHandle(var.toString());
    }
    else if (var instanceof TAConstantVar) {
      return var.toString();
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

      String arrayHandle = getVariableHandle(
        cv.getArrayVar(), pool, methodD
      );

      String indexHandle = getVariableHandle(
        cv.getIndexVar(), pool, methodD
      );

      if (isMemoryHandle(arrayHandle)) {
        pool.emit(Nasm.OP.make("mov esi, " + arrayHandle));
        arrayHandle = "esi";
      }

      if (isMemoryHandle(indexHandle)) {
        pool.emit(Nasm.OP.make("mov edi, " + indexHandle));
        indexHandle = "edi";
      }

      return String.format("[%s+4*%s]", arrayHandle, indexHandle);
    }

    return null;
  }

  public static boolean isMemoryHandle(String handle) {
    return handle.startsWith("[");
  }

  public static final String labelPad = " ";
  public static final String stmtPad = "   ";
}
