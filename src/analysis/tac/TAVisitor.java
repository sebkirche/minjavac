package analysis.tac;

public interface TAVisitor {
  void visit(TAModule taModule);
  void visit(TAClass taClass);
  void visit(TAProcedure taProcedure);
  void visit(TABasicBlock taBasicBlock);
}
