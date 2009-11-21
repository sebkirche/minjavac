package analysis.tac.instructions;

public enum Condition {
  EQUAL("equal"),
  NOT_EQUAL("not_equal"),
  LESS_THAN("less_than"),
  LESS_OR_EQUAL("less_or_equal"),
  GREATER("greater"),
  GREATER_OR_EQUAL("greater_or_equal"),
  IS_TRUE("is_true"),
  IS_FALSE("is_false");

  private String op;

  private Condition(String _op) {
    op = _op;
  }

  @Override
  public String toString() {
    return op;
  }
}
