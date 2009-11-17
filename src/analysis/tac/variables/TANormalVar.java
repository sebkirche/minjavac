package analysis.tac.variables;

public class TANormalVar extends TAVariable {
  private String name;

  public TANormalVar(String s) {
    name = s;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
