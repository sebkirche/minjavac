package backend.nasm;

import java.util.*;
import analysis.symboltable.*;

public class NasmUtils {
  public static void calculateOffsets() {
    SymbolTable symT = SymbolTable.getInstance();

    for (ClassDescriptor c : symT.getClassDescriptors()) {
      int offset = 4;

      for (VariableDescriptor v : c.getVariableDescriptors()) {
        v.setOffset(offset);
        offset += 4;
      }

      c.setSize(offset);
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

}
