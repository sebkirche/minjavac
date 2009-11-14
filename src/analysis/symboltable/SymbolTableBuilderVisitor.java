package analysis.symboltable;

import analysis.syntaxtree.*;
import analysis.visitors.Visitor;

public class SymbolTableBuilderVisitor implements Visitor {
  private Class currentClass;
  private Method currentMethod;
  private SymbolTable symbolTable;

  public SymbolTableBuilderVisitor() {
    currentClass = null;
    currentMethod = null;
    symbolTable = new SymbolTable();
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  private void error(String prefix, Identifier symbol, String posfix) {
    System.out.println(
      prefix + " " + symbol.getDescriptor() + " " + posfix
    );
    System.exit(1);
  }

  private void checkVar(String varName) {
    symbolTable.getVarType(currentMethod, currentClass, varName);
  }

  private void checkMethod(String methodName) {
    symbolTable.getMethod(methodName, currentClass.getName());
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

    currentClass.addMethod("main", new IdentifierType("void"));
    currentMethod = currentClass.getMethod("main");
    currentMethod.addParameter(param, new IdentifierType("String[]"));

    mainC.s.accept(this);

    currentClass = null;
    currentMethod = null;
  }

  public void visit(ClassDeclSimple classDecl) {
    String name = classDecl.i.toString();

    if (!symbolTable.addClass(name, null))
      error("Class", classDecl.i, "is already defined");

    currentClass = symbolTable.getClass(name);

    for (VarDecl varD : classDecl.vl.getList())
      varD.accept(this);
    
    for (MethodDecl methodD : classDecl.ml.getList())
      methodD.accept(this);

    currentClass = null;
  }

  public void visit(ClassDeclExtends classDecl) {
    String name = classDecl.i.toString();
    String baseClass = classDecl.j.toString();

    if (!symbolTable.addClass(name, baseClass))
      error("Class", classDecl.i, "is already defined");

    currentClass = symbolTable.getClass(name);

    for (VarDecl varD : classDecl.vl.getList())
      varD.accept(this);

    for (MethodDecl methodD : classDecl.ml.getList())
      methodD.accept(this);

    currentClass = null;
  }

  public void visit(VarDecl varDecl) {
    String varName = varDecl.i.toString();
    Type varType = varDecl.t;

    if (currentMethod == null) {
      if (!currentClass.addVar(varName, varType))
        error("Variable", varDecl.i, " is already defined");
    } else {
      if (!currentMethod.addLocalVar(varName, varType))
        error("Variable", varDecl.i, " is already defined");
    }
  }

  public void visit(MethodDecl methodDecl) {
    String methodName = methodDecl.i.toString();
    Type returnType = methodDecl.t;

    if (!currentClass.addMethod(methodName, returnType))
      error("Method", methodDecl.i, "is already defined");

    currentMethod = currentClass.getMethod(methodName);

    for (Formal param : methodDecl.fl.getList())
      param.accept(this);

    for (VarDecl varDecl : methodDecl.vl.getList())
      varDecl.accept(this);

    for (Statement stmt : methodDecl.sl.getList())
      stmt.accept(this);
    
    methodDecl.e.accept(this);

    currentMethod = null;
  }

  public void visit(Formal param) {
    String name = param.i.toString();

    if (!currentMethod.addParameter(name, param.t))
      error("Parameter", param.i, "is already defined");
  }

  public void visit(IntArrayType n) {
  }

  public void visit(BooleanType n) {
  }

  public void visit(IntegerType n) {
  }

  public void visit(IdentifierType n) {
    // só checa erro de tipo identificador na checagem de tipo
  }

  public void visit(Block block) {
    for (Statement stmt : block.sl.getList())
      stmt.accept(this);
  }

  public void visit(If ifStmt) {
    ifStmt.e.accept(this);
    ifStmt.s1.accept(this);
    ifStmt.s2.accept(this);
  }

  public void visit(While whileStmt) {
    whileStmt.e.accept(this);
    whileStmt.s.accept(this);
  }

  public void visit(Print printStmt) {
    printStmt.e.accept(this);
  }

  public void visit(Assign assignStmt) {
    checkVar(assignStmt.i.toString());
    assignStmt.e.accept(this);
  }

  public void visit(ArrayAssign arrayAssign) {
    checkVar(arrayAssign.i.toString());
    arrayAssign.e1.accept(this);
    arrayAssign.e2.accept(this);
  }

  public void visit(And andExp) {
    andExp.e1.accept(this);
    andExp.e2.accept(this);
  }

  public void visit(LessThan lessExp) {
    lessExp.e1.accept(this);
    lessExp.e2.accept(this);
  }

  public void visit(Plus plusExp) {
    plusExp.e1.accept(this);
    plusExp.e2.accept(this);
  }

  public void visit(Minus minusExp) {
    minusExp.e1.accept(this);
    minusExp.e2.accept(this);
  }

  public void visit(Times timesExp) {
    timesExp.e1.accept(this);
    timesExp.e2.accept(this);
  }

  public void visit(ArrayLookup arrayLookup) {
    arrayLookup.e1.accept(this);
    arrayLookup.e2.accept(this);
  }

  public void visit(ArrayLength arrayLength) {
    arrayLength.e.accept(this);
  }

  public void visit(Call callStmt) {
    callStmt.e.accept(this);
    checkMethod(callStmt.i.toString());

    for (Exp expr : callStmt.el.getList())
      expr.accept(this);
  }

  public void visit(IntegerLiteral intLiteral) {
  }

  public void visit(True n) {
  }

  public void visit(False n) {
  }

  public void visit(This n) {
  }

  public void visit(NewArray newArrayExp) {
    newArrayExp.e.accept(this);
  }

  public void visit(NewObject newObjectExp) {
    // deixa pra pegar o erro no type checking
  }

  public void visit(Not notExp) {
    notExp.e.accept(this);
  }

  public void visit(Identifier varId) {
    checkVar(varId.toString());
  }
}
