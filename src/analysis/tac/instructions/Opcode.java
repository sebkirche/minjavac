package analysis.tac.instructions;

public enum Opcode {
  ADD("add"), SUB("sub"), MULT("mult"),
  AND("and"), NOT("not"), IS_LESS("is_less"),
  ARRAY_LENGTH("length"), NEW_ARRAY("new[]"),
  NEW_OBJECT("new");

  private String op;

  private Opcode(String _op) {
    op = _op;
  }

  @Override
  public String toString() {
    return op;
  }
}
