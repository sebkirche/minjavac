package analysis.tac.instructions;

public abstract class Instruction {
  public boolean isLabel() {
    return this instanceof Label;
  }

  public boolean isJump() {
    return this instanceof Jump;
  }
}
