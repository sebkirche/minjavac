package analysis.tac.instructions;

import analysis.tac.variables.TAVariable;

public class ConditionalJump extends Jump {
  private TAVariable a, b;
  private Condition cond;

  public ConditionalJump(Condition c, TAVariable _a, TAVariable _b, Label target) {
    super(target);
    cond = c;
    a = _a;
    b = _b;
  }

  public Condition getCondition() {
    return cond;
  }

  public void setInverseCondition() {
    switch (cond) {
      case EQUAL:
        cond = Condition.NOT_EQUAL;
        break;

      case NOT_EQUAL:
        cond = Condition.EQUAL;
        break;

      case LESS_THAN:
        cond = Condition.GREATER_OR_EQUAL;
        break;

      case IS_TRUE:
        cond = Condition.IS_FALSE;
        break;

      case IS_FALSE:
        cond = Condition.IS_TRUE;
        break;
    }
  }

  public TAVariable getA() {
    return a;
  }

  public TAVariable getB() {
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
