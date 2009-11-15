package analysis.tac.variables;

public class InstanceFieldVar implements Variable {
  private Variable reference;
  private String field;

  public InstanceFieldVar(Variable a, String f) {
    reference = a;
    field = f;
  }

  public Variable getReference() {
    return reference;
  }

  public String getFieldName() {
    return field;
  }

  @Override
  public String toString() {
    return reference + "." + field;
  }
}
