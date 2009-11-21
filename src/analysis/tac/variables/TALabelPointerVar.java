package analysis.tac.variables;

public class TALabelPointerVar extends TAVariable {
  private String label;

  public TALabelPointerVar(String l) {
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
