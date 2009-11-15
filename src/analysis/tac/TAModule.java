package analysis.tac;

import java.util.ArrayList;
import java.util.List;

public class TAModule {
  private List<TAClass> classes;

  public TAModule() {
    classes = new ArrayList<TAClass>(5);
  }

  @Override
  public String toString() {
    String str = "";

    for (TAClass c : classes)
      str += "\n" + c;

    return str;
  }
}
