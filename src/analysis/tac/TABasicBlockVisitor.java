package analysis.tac;

import analysis.tac.instructions.*;

public interface TABasicBlockVisitor {
  public void visit(TABasicBlock block);
  public void visit(Action action);
  public void visit(Copy copy);
  public void visit(Jump jump);
  public void visit(Label label);
  public void visit(Operation operation);
  public void visit(ParameterSetup param);
  public void visit(PrintInstruction print);
  public void visit(ProcedureCall proc);
  public void visit(Return returnInstr);
}
