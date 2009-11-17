package analysis.tac;

import java.util.List;
import java.util.ArrayList;

public class TAClass {
  private String name;
  private List<TAProcedure> procedures;

  public TAClass(String s) {
    name = s;
    procedures = new ArrayList<TAProcedure>(15);
  }

  public String getName() {
    return name;
  }

  public List<TAProcedure> getProcedures() {
    return procedures;
  }

  public TAProcedure getProcedure(String procName) {
    for (TAProcedure p : procedures)
      if (p.getLabel().getLabel().equals(procName))
        return p;
    
    return null;
  }

  @Override
  public String toString() {
    String str = "class " + name + ":";

    for (TAProcedure p : procedures)
      str += "\n\n" + p;

    str += "\nend";
    return str;
  }
}
