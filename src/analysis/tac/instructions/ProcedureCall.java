package analysis.tac.instructions;

import analysis.tac.variables.TAVariable;

public class ProcedureCall extends TAInstruction {
  private TAVariable dest;
  private Label procedure;

  public ProcedureCall(TAVariable _d, Label _proc) {
    dest = _d;
    procedure = _proc;
  }

  public TAVariable getDestiny() {
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
