package analysis.tac.variables;

public class TALocalVar extends TAVariable {
  private String name;

  public TALocalVar(String s) {
    name = s;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  public boolean equals(Object e) {
    return ((TALocalVar)e).name.equals(name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
