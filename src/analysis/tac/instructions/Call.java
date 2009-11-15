package analysis.tac.instructions;

import analysis.symboltable.Variable;

public class Call implements Instruction {
  private Variable dest;
  private Label procedure;

  public Call(Variable _d, Label _proc) {
    dest = _d;
    procedure = _proc;
  }

  public Variable getDestiny() {
    return dest;
  }

  public Label getProcedure() {
    return procedure;
  }

  @Override
  public String toString() {
    return dest + " := call " + procedure;
  }
}
