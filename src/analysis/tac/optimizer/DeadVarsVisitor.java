package analysis.tac.optimizer;

import analysis.tac.*;
import analysis.tac.instructions.*;
import analysis.tac.variables.TAArrayCellVar;
import analysis.tac.variables.TALocalVar;
import analysis.tac.variables.TAThisReferenceVar;
import analysis.tac.variables.TAVariable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeadVarsVisitor implements TABasicBlockVisitor {
  private Set<TALocalVar> deadVars;
  private TAInstruction instruction;

  public void visit(TABasicBlock block) {
    deadVars = new HashSet<TALocalVar>(20);
    deadVars.addAll(block.readVars());
    deadVars.addAll(block.writeVars());
    deadVars.removeAll(block.liveVars());

    List<TAInstruction> code = block.instructions();

    for (int i = code.size()-1; i >= 0; --i) {
      instruction = code.get(i);
      //attachDeadVars(instruction);
      instruction.accept(this);
    }
  }

  public void visit(Copy copy) {
    visitRead(copy.getSource());
    attachDeadVars();
    visitWrite(copy.getDestiny());
  }

  public void visit(Jump jump) {
    if (jump.isConditionalJump()) {
      ConditionalJump j = (ConditionalJump)jump;
      visitRead(j.getA());
      if (j.getB() != null) visitRead(j.getB());
      attachDeadVars();
    }
  }

  public void visit(Operation op) {
    visitRead(op.getA());
    if (op.getB() != null) visitRead(op.getB());
    attachDeadVars();
    visitWrite(op.getDestiny());
  }

  public void visit(ParameterSetup param) {
    visitRead(param.getParameter());
    attachDeadVars();
  }

  public void visit(ProcedureCall proc) {
    attachDeadVars();
    visitWrite(proc.getDestiny());
  }

  public void visit(Return ret) {
    visitRead(ret.getReturnVariable());
    attachDeadVars();
  }

  public void visit(Action action) { }

  public void visit(Label label) { }

  private void visitRead(TAVariable v) {
    if (v instanceof TAArrayCellVar) {
      visitRead(((TAArrayCellVar)v).getIndexVar());
      visitRead(((TAArrayCellVar)v).getArrayVar());
    } else if (v instanceof TALocalVar) {
      deadVars.remove((TALocalVar)v);
    } else if (v instanceof TAThisReferenceVar) {
      visitRead(((TAThisReferenceVar)v).getReference());
    }
  }

  private void visitWrite(TAVariable v) {
    if (v instanceof TAThisReferenceVar) {
      visitWrite(((TAThisReferenceVar)v).getReference());
    } else if (v instanceof TAArrayCellVar) {
      visitRead(((TAArrayCellVar)v).getIndexVar());
      visitRead(((TAArrayCellVar)v).getArrayVar());
    } else if (v instanceof TALocalVar) {
      deadVars.add((TALocalVar)v);
    }
  }

  private void attachDeadVars() {
    for (TALocalVar v : deadVars)
      instruction.addDeadVar(v);
  }
}
