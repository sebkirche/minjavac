package backend.nasm;

import analysis.tac.TAClass;
import analysis.tac.TAModule;
import analysis.tac.TAVisitor;
import analysis.tac.TAProcedure;
import analysis.tac.TABasicBlock;

public class CodeGeneratorVisitor implements TAVisitor {
  public void visit(TAModule taModule) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(TAClass taClass) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(TAProcedure taProcedure) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(TABasicBlock taBasicBlock) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
