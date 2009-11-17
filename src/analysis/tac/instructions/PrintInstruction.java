package analysis.tac.instructions;

import analysis.tac.TABasicBlockVisitor;
import analysis.tac.variables.TAVariable;

public class PrintInstruction extends TAInstruction {
  private TAVariable var;

  public PrintInstruction(TAVariable v) {
    var = v;
  }

  public TAVariable getVar() {
    return var;
  }

  @Override
  public String toString() {
    return "print " + var;
  }

  @Override
  public void accept(TABasicBlockVisitor v) {
    v.visit(this);
  }
}
