package analysis.tac.instructions;

public class Label extends TAInstruction {
  private String label;

  public Label(String l) {
    label = l;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public boolean equals(Object e) {
    return ((Label)e).label.equals(label);
  }
  
  @Override
  public String toString() {
    return label;
  }
}
