package analysis.tac.instructions;

import analysis.tac.variables.TAVariable;

public class Copy extends TAInstruction {
  private TAVariable source, destiny;

  public Copy(TAVariable _x, TAVariable _y) {
    destiny = _x;
    source = _y;
  }

  public TAVariable getSource() {
    return source;
  }

  public TAVariable getDestiny() {
    return destiny;
  }

  @Override
  public String toString() {
    return destiny + " := " + source;
  }
}
