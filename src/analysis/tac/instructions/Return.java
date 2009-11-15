package analysis.tac.instructions;

import analysis.symboltable.Variable;

public class Return {
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
