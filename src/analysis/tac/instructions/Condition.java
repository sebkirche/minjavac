package analysis.tac.instructions;

public enum Condition {
  EQUAL("equal"), NOT_EQUAL("not_equal"),
  LESS_THAN("less_than"), IS_TRUE("is_true"), IS_FALSE("is_false");

  private String op;

  private Condition(String _op) {
    op = _op;
  }

  @Override
  public String toString() {
    return op;
  }
}
