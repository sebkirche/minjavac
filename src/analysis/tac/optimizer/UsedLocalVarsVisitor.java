package analysis.tac.optimizer;

import java.util.Set;
import java.util.HashSet;
import analysis.tac.*;
import analysis.tac.variables.*;
import analysis.tac.instructions.*;

class UsedLocalVarsVisitor implements TABasicBlockVisitor {
  private TABasicBlock block;
  private Set<TAVariable> seen;

  public void visit(TABasicBlock b) {
    block = b;
    seen = new HashSet<TAVariable>(20);

    for (TAInstruction i : block.instructions())
      i.accept(this);
  }

  public void visit(Copy copy) {
    visitRead(copy.getSource());
    visitWrite(copy.getDestiny());
  }

  public void visit(Jump jump) {
    if (jump.isConditionalJump()) {
      ConditionalJump j = (ConditionalJump)jump;
      visitRead(j.getA());
      if (j.getB() != null) visitRead(j.getB());
    }
  }

  public void visit(Operation op) {
    visitRead(op.getA());
    visitWrite(op.getDestiny());
    if (op.getB() != null) visitRead(op.getB());
  }

  public void visit(ParameterSetup param) {
    visitRead(param.getParameter());
  }

  public void visit(PrintInstruction print) {
    visitRead(print.getVar());
  }

  public void visit(ProcedureCall proc) {
    visitWrite(proc.getDestiny());
  }

  public void visit(Return ret) {
    visitRead(ret.getReturnVariable());
  }

  public void visit(Action action) { }

  public void visit(Label label) { }

  private void visitRead(TAVariable v) {
    if (v instanceof TAArrayCellVar) {
      visitRead(((TAArrayCellVar)v).getIndexVar());
      visitRead(((TAArrayCellVar)v).getArrayVar());
    }
    else if (v instanceof TALocalVar) {
      if (!seen.contains(v))
        block.firstReadVars().add((TALocalVar)v);

      block.readVars().add((TALocalVar)v);
      seen.add(v);
    }
  }

  private void visitWrite(TAVariable v) {
    if (v instanceof TAArrayCellVar) {
      visitRead(((TAArrayCellVar)v).getIndexVar());
      visitRead(((TAArrayCellVar)v).getArrayVar());
    }
    if (v instanceof TALocalVar) {
      block.writeVars().add((TALocalVar)v);
      seen.add(v);
    }
  }
}
