package analysis.tac.instructions;

import analysis.tac.TABasicBlockVisitor;

public class Action extends TAInstruction {
  private Opcode opcode;

  public Action(Opcode op) {
    opcode = op;
  }

  public Opcode getOpcode() {
    return opcode;
  }

  @Override
  public String toString() {
    return opcode.toString();
  }

  public void accept(TABasicBlockVisitor v) {
    v.visit(this);
  }
}
