package backend.nasm;

import java.util.List;
import analysis.symboltable.Util;

public class VirtualTable {
  private String className;
  private List<String> methodLabels;

  public VirtualTable(String classN, List<String> methods) {
    className = classN;
    methodLabels = methods;
  }

  public String getName() {
    return className + "@@vt";
  }

  public int getSize() {
    return methodLabels.size();
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
