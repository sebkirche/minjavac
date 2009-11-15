package analysis.tac.instructions;

public class Jump extends Instruction {
  private Label target;

  public Jump(Label l) {
    target = l;
  }

  public Label getTarget() {
    return target;
  }

  @Override
  public String toString() {
    return "goto " + target.toString();
  }
}
