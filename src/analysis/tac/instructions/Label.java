package analysis.tac.instructions;

public class Label extends Instruction {
  private String label;

  public Label(String l) {
    label = l;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }
}
