package analysis.tac.instructions;

import analysis.tac.variables.Variable;

public class ParameterSetup extends Instruction {
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
