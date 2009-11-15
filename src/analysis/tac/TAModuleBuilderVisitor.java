package analysis.tac;

import analysis.syntaxtree.*;
import analysis.visitors.Visitor;
import analysis.tac.variables.*;
import analysis.tac.instructions.*;
import analysis.symboltable.SymbolTable;

public class TAModuleBuilderVisitor implements Visitor {
  private TAModule module;
  private SymbolTable symbolTable;
 
  public TAModuleBuilderVisitor(SymbolTable symT) {
    symbolTable = symT;
    module = new TAModule();
  }

  public void visit(Program program) {
    MainClass main = program.mainC;
  }

  public void visit(MainClass n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ClassDeclSimple n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ClassDeclExtends n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(VarDecl n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(MethodDecl n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Formal n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(IntArrayType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(BooleanType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(IntegerType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(IdentifierType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Block n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(If n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(While n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Print n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Assign n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ArrayAssign n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(And n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(LessThan n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Plus n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Minus n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Times n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ArrayLookup n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ArrayLength n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Call n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(IntegerLiteral n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(True n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(False n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(This n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(NewArray n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(NewObject n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Not n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Identifier n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
