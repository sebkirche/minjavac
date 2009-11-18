package backend.nasm;

import analysis.symboltable.Util;
import java.util.List;

public class VirtualTable {
  private String className;
  private List<String> methodLabels;

  public VirtualTable(String classN, List<String> methods) {
    className = classN;
    methodLabels = methods;
  }

  public String getClassName() {
    return className;
  }

  public List<String> getMethodLabels() {
    return methodLabels;
  }

  public int getMethodPosition(String methodName) {
    for (int i = 0; i < methodLabels.size(); ++i) {
      String label = methodLabels.get(i);

      if (Util.getDefinedName(label).equals(methodName))
        return i;
    }

    return -1;
  }
}
