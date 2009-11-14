package analysis.symboltable;

import analysis.syntaxtree.*;
import analysis.visitors.Visitor;

public class SymbolTableBuilderVisitor implements Visitor {
  private Class currentClass;
  private Method currentMethod;
  private SymbolTable symbolTable;

  public SymbolTableBuilderVisitor() {
    symbolTable = new SymbolTable();
    currentClass = null;
    currentMethod = null;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public void visit(Program p) {
    p.m.accept(this);

    for (ClassDecl classD : p.cl.getList())
      classD.accept(this);
  }

  public void visit(MainClass mainC) {
    String name = mainC.i1.toString();
    String param = mainC.i2.toString();

    symbolTable.addClass(name, null);
    currentClass = symbolTable.getClass(name);

    currentMethod = new Method("main", new IdentifierType("void"));
    currentMethod.addParameter(param, new IdentifierType("String[]"));

    mainC.s.accept(this);

    currentClass = null;
    currentMethod = null;
  }

  public void visit(ClassDeclSimple classDecl) {
    String name = classDecl.i.toString();

    

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
