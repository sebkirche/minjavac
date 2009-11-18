package backend.nasm;

import java.util.List;
import analysis.tac.*;
import analysis.tac.instructions.*;

public class TABasicBlockEmitter implements TABasicBlockVisitor {
  private List<NasmInstruction> code;

  public TABasicBlockEmitter(List<NasmInstruction> c) {
    code = c;
  }

  private void emit(NasmInstruction i) {
    code.add(i);
  }

  public void visit(TABasicBlock block) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Action action) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Copy copy) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Jump jump) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Label label) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Operation operation) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ParameterSetup param) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(PrintInstruction print) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ProcedureCall proc) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Return returnInstr) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
