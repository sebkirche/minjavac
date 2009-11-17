package analysis.symboltable;

import analysis.syntaxtree.*;
import analysis.visitors.Visitor;

public class SymbolTableBuilderVisitor implements Visitor {
  private ClassDescriptor currentClass;
  private MethodDescriptor currentMethod;
  private SymbolTable symbolTable;

  public SymbolTableBuilderVisitor() {
    currentClass = null;
    currentMethod = null;
    symbolTable = new SymbolTable();
    SymbolTable.setInstance(symbolTable);
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
    p.mainC.accept(this);

    for (ClassDecl classD : p.classList.getList())
      classD.accept(this);
  }

  public void visit(MainClass mainC) {
    String name = mainC.classNameId.toString();
    String param = mainC.argId.toString();

    symbolTable.addClass(name, null);
    currentClass = symbolTable.getClass(name);

    currentClass.addMethod("main", new IdentifierType("void"));
    currentMethod = currentClass.getMethod("main");
    currentMethod.addParameter(param, new IdentifierType("String[]"));

    mainC.mainStmt.accept(this);

    currentClass = null;
    currentMethod = null;
  }

  public void visit(ClassDeclSimple classDecl) {
    String name = classDecl.classId.toString();

    if (!symbolTable.addClass(name, null))
      error("Class", classDecl.classId, "is already defined");

    currentClass = symbolTable.getClass(name);

    for (VarDecl varD : classDecl.fieldVarList.getList())
      varD.accept(this);
    
    for (MethodDecl methodD : classDecl.methodList.getList())
      methodD.accept(this);

    currentClass = null;
  }

  public void visit(ClassDeclExtends classDecl) {
    String name = classDecl.classId.toString();
    String baseClass = classDecl.baseClassId.toString();

    if (!symbolTable.addClass(name, baseClass))
      error("Class", classDecl.classId, "is already defined");

    currentClass = symbolTable.getClass(name);

    for (VarDecl varD : classDecl.fieldVarList.getList())
      varD.accept(this);

    for (MethodDecl methodD : classDecl.methodList.getList())
      methodD.accept(this);

    currentClass = null;
  }

  public void visit(VarDecl varDecl) {
    String varName = varDecl.varId.toString();
    Type varT = varDecl.varType;

    if (currentMethod == null) {
      if (!currentClass.addVar(varName, varT))
        error("Variable", varDecl.varId, " is already defined");
    } else {
      if (!currentMethod.addLocalVar(varName, varT))
        error("Variable", varDecl.varId, " is already defined");
    }
  }

  public void visit(MethodDecl methodDecl) {
    String methodName = methodDecl.methodNameId.toString();
    Type returnType = methodDecl.methodReturnT;

    if (!currentClass.addMethod(methodName, returnType))
      error("Method", methodDecl.methodNameId, "is already defined");

    currentMethod = currentClass.getMethod(methodName);

    for (Formal param : methodDecl.formalParamList.getList())
      param.accept(this);

    for (VarDecl varDecl : methodDecl.localVarList.getList())
      varDecl.accept(this);

    for (Statement stmt : methodDecl.statementList.getList())
      stmt.accept(this);
    
    methodDecl.returnExpr.accept(this);

    currentMethod = null;
  }

  public void visit(Formal param) {
    String name = param.paramId.toString();

    if (!currentMethod.addParameter(name, param.paramType))
      error("Parameter", param.paramId, "is already defined");
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
    for (Statement stmt : block.stmtList.getList())
      stmt.accept(this);
  }

  public void visit(If ifStmt) {
    ifStmt.boolExpr.accept(this);
    ifStmt.trueStmt.accept(this);
    ifStmt.falseStmt.accept(this);
  }

  public void visit(While whileStmt) {
    whileStmt.boolExpr.accept(this);
    whileStmt.stmt.accept(this);
  }

  public void visit(Print printStmt) {
    printStmt.intExpr.accept(this);
  }

  public void visit(Assign assignStmt) {
    checkVar(assignStmt.varId.toString());
    assignStmt.valueExpr.accept(this);
  }

  public void visit(ArrayAssign arrayAssign) {
    checkVar(arrayAssign.arrayId.toString());
    arrayAssign.indexExpr.accept(this);
    arrayAssign.valueExpr.accept(this);
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
    arrayLookup.arrayExpr.accept(this);
    arrayLookup.indexExpr.accept(this);
  }

  public void visit(ArrayLength arrayLength) {
    arrayLength.arrayExpr.accept(this);
  }

  public void visit(Call callStmt) {
    callStmt.objectExpr.accept(this);
    //checkMethod(callStmt.i.toString());

    for (Exp expr : callStmt.paramExprList.getList())
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
    newArrayExp.sizeExpr.accept(this);
  }

  public void visit(NewObject newObjectExp) {
    // deixa pra pegar o erro no type checking
  }

  public void visit(Not notExp) {
    notExp.boolExpr.accept(this);
  }

  public void visit(Identifier varId) {
    checkVar(varId.toString());
  }
}
