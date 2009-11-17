package analysis.tac.variables;

public class TAOtherVar extends TAVariable {
  private String name;

  public TAOtherVar(String n) {
    name = n;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
