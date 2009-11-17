package analysis.tac.instructions;

public abstract class TAInstruction {
  public boolean isLabel() {
    return this instanceof Label;
  }

  public boolean isJump() {
    return this instanceof Jump;
  }

  public boolean isGoto() {
    return isJump() && !isConditionalJump();
  }

  public boolean isConditionalJump() {
    return this instanceof ConditionalJump;
  }
}
