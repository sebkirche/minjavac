package analysis.tac.instructions;

import analysis.tac.variables.Variable;

public class ConditionalJump extends Jump {
  private Variable a, b;
  private Condition cond;

  public ConditionalJump(Condition c, Variable _a, Variable _b, Label target) {
    super(target);
    cond = c;
    a = _a;
    b = _b;
  }

  public Condition getCondition() {
    return cond;
  }

  public Variable getA() {
    return a;
  }

  public Variable getB() {
    return b;
  }

  @Override
  public String toString() {
    if (b != null)
      return String.format("if %s(%s, %s) goto %s",cond, a, b, getTarget());
    else
      return String.format("if %s %s goto %s",cond, a, getTarget());
  }
}
