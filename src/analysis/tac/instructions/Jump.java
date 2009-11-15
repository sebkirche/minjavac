package analysis.tac.instructions;

public class Jump {
  private Label target;

  public Jump(Label l) {
    target = l;
  }

  public Label getTarget() {
    return target;
  }

  @Override
  public String toString() {
    return target.toString();
  }
}
