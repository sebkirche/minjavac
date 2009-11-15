package analysis.tac.instructions;

import analysis.symboltable.Variable;

public class ParameterSetup {
  private Variable parameter;

  public ParameterSetup(Variable v) {
    parameter = v;
  }

  public Variable getParameter() {
    return parameter;
  }

  @Override
  public String toString() {
    return "param " + parameter;
  }
}
