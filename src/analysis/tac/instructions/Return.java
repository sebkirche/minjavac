package analysis.tac.instructions;

import analysis.tac.TABasicBlockVisitor;
import analysis.tac.variables.TAVariable;

public class Return extends TAInstruction {
  private TAVariable v;

  public Return(TAVariable _v) {
    v = _v;
  }

  public TAVariable getReturnVariable() {
    return v;
  }

  @Override
  public String toString() {
    return "return " + v;
  }

  @Override
  public void accept(TABasicBlockVisitor v) {
    v.visit(this);
  }
}
