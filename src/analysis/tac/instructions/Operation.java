package analysis.tac.instructions;

import analysis.tac.TABasicBlockVisitor;
import analysis.tac.variables.TAVariable;

public class Operation extends TAInstruction {
  private Opcode op;
  private TAVariable dest, a, b;

  public Operation(Opcode op, TAVariable x, TAVariable y) {
    this(op, x, y, null);
  }

  public Operation(Opcode _op, TAVariable _d, TAVariable _a, TAVariable _b) {
    op = _op;
    dest = _d;
    a = _a;
    b = _b;
  }

  public Opcode getOperation() {
    return op;
  }

  public TAVariable getDestiny() {
    return dest;
  }

  public TAVariable getA() {
    return a;
  }

  public TAVariable getB() {
    return b;
  }

  @Override
  public String toString() {
    if (b != null)
      return String.format("%s := %s %s, %s", dest, op, a, b);
    else
      return String.format("%s := %s %s", dest, op ,a);
  }

  @Override
  public void accept(TABasicBlockVisitor v) {
    v.visit(this);
  }
}
