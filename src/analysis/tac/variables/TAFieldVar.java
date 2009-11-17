package analysis.tac.variables;

public class TAFieldVar extends TAVariable {
  private String name;

  public TAFieldVar(String n) {
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
