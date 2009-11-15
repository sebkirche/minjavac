package analysis.tac;

import java.util.List;
import java.util.ArrayList;
import analysis.tac.variables.Variable;

public class TAClass {
  private String name;
  private List<Variable> staticVars;
  private List<TAProcedure> procedures;

  public TAClass(String s) {
    name = s;
    staticVars = new ArrayList<Variable>(15);
    procedures = new ArrayList<TAProcedure>(15);
  }

  public String getName() {
    return name;
  }

  public List<Variable> getStaticVars() {
    return staticVars;
  }

  public List<TAProcedure> getProcedures() {
    return procedures;
  }

  @Override
  public String toString() {
    String str = "class " + name + ":";

    for (Variable v : staticVars)
      str += "\n" + v;

    for (TAProcedure p : procedures)
      str += "\n\n" + p;

    str += "\nend";
    return str;
  }
}
