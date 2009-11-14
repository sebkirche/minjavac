package analysis.typechecker;

import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.visitors.TypeVisitor;

public class TypeCheckerVisitor implements TypeVisitor {
  private SymbolTable symbolTable;
  private analysis.symboltable.Class currentClass;
  private analysis.symboltable.Method currentMethod;

  public TypeCheckerVisitor(SymbolTable symT) {
    symbolTable = symT;
    currentClass = null;
    currentMethod = null;
  }

  private void error(String expected, String inferred) {
    System.out.println("Expected type " + expected + ", got " + inferred);
  }

  public Type visit(Program program) {
    program.m.accept(this);

    return null;
  }

  public Type visit(MainClass n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(ClassDeclSimple n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(ClassDeclExtends n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(VarDecl n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(MethodDecl n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Formal n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(IntArrayType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(BooleanType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(IntegerType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(IdentifierType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Block n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(If n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(While n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Print n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Assign n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(ArrayAssign n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(And n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(LessThan n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Plus n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Minus n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Times n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(ArrayLookup n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(ArrayLength n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Call n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(IntegerLiteral n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(True n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(False n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(This n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(NewArray n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(NewObject n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Not n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Type visit(Identifier n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
