package analysis.tac.instructions;

import analysis.tac.variables.Variable;

public class Return extends Instruction {
  private Variable v;

  public Return(Variable _v) {
    v = _v;
  }

  public Variable getReturnVariable() {
    return v;
  }

  @Override
  public String toString() {
    return "return " + v;
  }
}
