package analysis.tac;

import java.util.List;
import java.util.ArrayList;
import analysis.tac.variables.Variable;
import analysis.tac.instructions.Label;

public class TAProcedure {
  private Label name;
  private List<TABasicBlock> code;
  private List<Variable> parameters;

  public TAProcedure(String _name) {
    name = new Label(_name);
    code = new ArrayList<TABasicBlock>(20);
    parameters = new ArrayList<Variable>(10);
  }

  public Label getName() {
    return name;
  }

  public List<TABasicBlock> getCode() {
    return code;
  }

  public List<Variable> getParameters() {
    return parameters;
  }

  @Override
  public String toString() {
    String str = "procedure " + name + "(";

    boolean f = true;
    for (Variable v : parameters) {
      if (f) f = false;
      else str += ", ";
      str += v;
    }

    str += "):";

    for (TABasicBlock b : code)
      str += "\n" + b;

    str += "\nend";
    return str;
  }
}
