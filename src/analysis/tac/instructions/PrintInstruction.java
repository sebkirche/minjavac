package analysis.tac.instructions;

import analysis.tac.variables.Variable;

public class PrintInstruction extends Instruction {
  private Variable var;

  public PrintInstruction(Variable v) {
    var = v;
  }

  public Variable getVar() {
    return var;
  }

  @Override
  public String toString() {
    return "print " + var;
  }
}
