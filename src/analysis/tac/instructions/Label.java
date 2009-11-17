package analysis.tac.instructions;

import analysis.tac.TABasicBlockVisitor;

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

  @Override
  public void accept(TABasicBlockVisitor v) {
    v.visit(this);
  }
}
