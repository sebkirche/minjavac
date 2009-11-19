package analysis.tac.variables;

public class TAThisReferenceVar extends TAVariable {
  private TAVariable var;

  public TAThisReferenceVar(TAVariable v) {
    var = v;
  }

  public TAVariable getReference() {
    return var;
  }

  @Override
  public String toString() {
    return var.toString();
  }
}
