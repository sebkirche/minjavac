package analysis.tac.instructions;

import analysis.tac.variables.Variable;

public class Operation extends Instruction {
  private Opcode op;
  private Variable dest, a, b;

  public Operation(Opcode op, Variable x, Variable y) {
    this(op, x, y, null);
  }

  public Operation(Opcode _op, Variable _d, Variable _a, Variable _b) {
    op = _op;
    dest = _d;
    a = _a;
    b = _b;
  }

  public Opcode getOperation() {
    return op;
  }

  public Variable getDestiny() {
    return dest;
  }

  public Variable getA() {
    return a;
  }

  public Variable getB() {
    return b;
  }

  @Override
  public String toString() {
    if (b != null)
      return String.format("%s := %s %s, %s", dest, op, a, b);
    else
      return String.format("%s := %s %s", dest, op ,a);
  }
}
