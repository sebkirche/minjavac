package analysis.tac.instructions;

import analysis.symboltable.Variable;

public class Copy {
  private Variable source, destiny;

  public Copy(Variable _x, Variable _y) {
    destiny = _x;
    source = _y;
  }

  public Variable getSource() {
    return source;
  }

  public Variable getDestiny() {
    return destiny;
  }

  @Override
  public String toString() {
    return destiny + " := " + source;
  }
}
