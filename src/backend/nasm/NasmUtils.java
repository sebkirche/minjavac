package backend.nasm;

import java.util.*;
import analysis.symboltable.*;

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

  public static final String labelPad = " ";
  public static final String stmtPad = "   ";
}
