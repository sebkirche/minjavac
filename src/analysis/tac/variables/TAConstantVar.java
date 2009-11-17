package analysis.tac.variables;

public class TAConstantVar extends TAVariable {
  private int value;

  public TAConstantVar(int v) {
    value = v;
  }

  public int value() {
    return value;
  }

  @Override
  public String toString() {
    return value + "";
  }
}
