package analysis.tac.variables;

public class ConstantVar extends Variable {
  private int value;

  public ConstantVar(int v) {
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
