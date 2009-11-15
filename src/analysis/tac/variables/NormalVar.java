package analysis.tac.variables;

public class NormalVar extends Variable {
  private String name;

  public NormalVar(String s) {
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
