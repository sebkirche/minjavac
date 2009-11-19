package analysis.tac.instructions;

public enum Opcode {
  ADD("add"),
  SUB("sub"),
  MULT("mult"),
  AND("and"),
  NOT("not"),
  ARRAY_LENGTH("length"),
  SAVE_CTX("save_context"),
  LOAD_CTX("load_context");

  private String op;

  private Opcode(String _op) {
    op = _op;
  }

  @Override
  public String toString() {
    return op;
  }
}
