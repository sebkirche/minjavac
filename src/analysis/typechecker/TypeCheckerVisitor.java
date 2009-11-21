package analysis.typechecker;

import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.visitors.TypeVisitor;

public class TypeCheckerVisitor implements TypeVisitor {
  private SymbolTable symbolTable;
  private analysis.symboltable.ClassDescriptor currentClass;
  private analysis.symboltable.MethodDescriptor currentMethod;

  public TypeCheckerVisitor() { 
    symbolTable = SymbolTable.getInstance();
    currentClass = null; 
    currentMethod = null;
  }

  private void error(Type expected, Type inferred) {
    System.out.println("Expected type " + expected + ", got " + inferred);
    Thread.dumpStack();
    System.exit(1);
  }

  private void checkType(Type type) {
    if (!(type instanceof IdentifierType))
      return;

    IdentifierType it = (IdentifierType)type;

    if (symbolTable.getClass(it.className) == null) {
      System.out.println("Unknown class: " + it.className);
      System.exit(1);
    }
  }

  private Type getVarType(Identifier i) {
    return symbolTable.getVarType(currentMethod, currentClass, i.name);
  }

  public Type visit(Program program) {
    program.mainC.accept(this);

    for (ClassDecl classD : program.classList.getList())
      classD.accept(this);

    return null;
  }

  public Type visit(MainClass mainC) {
    currentClass = symbolTable.getClass(mainC.classNameId.toString());
    currentMethod = currentClass.getMethod("main");

    for (VarDecl vl : mainC.localVars.getList())
      vl.accept(this);

    for (Statement stmt : mainC.statements.getList())
      stmt.accept(this);

    currentMethod = null;
    currentClass = null;
    return null;
  }

  public Type visit(ClassDeclSimple classDecl) {
    String name = classDecl.classId.toString();
    currentClass = symbolTable.getClass(name);

    for (VarDecl varD : classDecl.fieldVarList.getList())
      varD.accept(this);

    for (MethodDecl methodD : classDecl.methodList.getList())
      methodD.accept(this);

    currentClass = null;
    return null;
  }

  public Type visit(ClassDeclExtends classDecl) {
    String name = classDecl.classId.toString();
    currentClass = symbolTable.getClass(name);

    for (VarDecl varD : classDecl.fieldVarList.getList())
      varD.accept(this);

    for (MethodDecl methodD : classDecl.methodList.getList())
      methodD.accept(this);

    currentClass = null;
    return null;
  }

  public Type visit(VarDecl varDecl) {
    checkType(varDecl.varType);
    return null;
  }

  public Type visit(MethodDecl methodDecl) {
    checkType(methodDecl.methodReturnT);
    currentMethod = currentClass.getMethod(methodDecl.methodNameId.toString());

    for (Formal param : methodDecl.formalParamList.getList())
      param.accept(this);

    for (VarDecl varDecl : methodDecl.localVarList.getList())
      varDecl.accept(this);

    for (Statement stmt : methodDecl.statementList.getList())
      stmt.accept(this);

    Type methodType = methodDecl.methodReturnT;
    Type returnType = methodDecl.returnExpr.accept(this);

    if (!symbolTable.compareTypes(methodType, returnType))
      error(methodType, returnType);

    currentMethod = null;
    return null;
  }

  public Type visit(Formal param) {
    checkType(param.paramType);
    return null;
  }

  public Type visit(IntArrayType n) {
    return null;
  }

  public Type visit(BooleanType n) {
    return null;
  }

  public Type visit(IntegerType n) {
    return null;
  }

  public Type visit(VoidType v) {
    return null;
  }

  public Type visit(IdentifierType n) {
    return null;
  }

  public Type visit(Block block) {
    for (Statement stmt : block.stmtList.getList())
      stmt.accept(this);
    return null;
  }

  public Type visit(If ifStmt) {
    Type expT = ifStmt.boolExpr.accept(this);
    Type booleanT = BooleanType.instance();

    if (!symbolTable.compareTypes(booleanT, expT))
      error(booleanT, expT);

    ifStmt.trueStmt.accept(this);
    ifStmt.falseStmt.accept(this);
    return null;
  }

  public Type visit(While whileStmt) {
    Type expT = whileStmt.boolExpr.accept(this);
    Type booleanT = BooleanType.instance();

    if (!symbolTable.compareTypes(booleanT, expT))
      error(booleanT, expT);

    whileStmt.stmt.accept(this);
    return null;
  }

  public Type visit(For forStmt) {
    for (Statement stmt : forStmt.init.getList())
      stmt.accept(this);

    Type expT = forStmt.boolExpr.accept(this);
    Type booleanT = BooleanType.instance();

    if (!symbolTable.compareTypes(booleanT, expT))
      error(booleanT, expT);

    for (Statement stmt : forStmt.step.getList())
      stmt.accept(this);

    forStmt.body.accept(this);
    return null;
  }

  public Type visit(Print printStmt) {
    Type expT = printStmt.intExpr.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expT))
      error(intT, expT);

    return null;
  }

  public Type visit(PrintString printStmt) {
    return null;
  }

  public Type visit(Assign assignStmt) {
    Type varT = getVarType(assignStmt.varId);
    Type expT = assignStmt.valueExpr.accept(this);

    if (!symbolTable.compareTypes(varT, expT))
      error(varT, expT);

    return null;
  }

  public Type visit(ArrayAssign arrayAssign) {
    Type intT = IntegerType.instance();
    Type indexT = arrayAssign.indexExpr.accept(this);
    Type expT = arrayAssign.valueExpr.accept(this);

    if (!symbolTable.compareTypes(intT, indexT))
      error(intT, indexT);

    if (!symbolTable.compareTypes(intT, expT))
      error(intT, expT);

    return null;
  }

  public Type visit(And andExp) {
    Type expA = andExp.e1.accept(this);
    Type expB = andExp.e2.accept(this);
    Type boolT = BooleanType.instance();

    if (!symbolTable.compareTypes(boolT, expA))
      error(boolT, expA);

    if (!symbolTable.compareTypes(boolT, expB))
      error(boolT, expB);

    andExp.setType(boolT);
    return boolT;
  }

  public Type visit(Or orExp) {
    Type expA = orExp.e1.accept(this);
    Type expB = orExp.e2.accept(this);
    Type boolT = BooleanType.instance();

    if (!symbolTable.compareTypes(boolT, expA))
      error(boolT, expA);

    if (!symbolTable.compareTypes(boolT, expB))
      error(boolT, expB);

    orExp.setType(boolT);
    return boolT;
  }

  public Type visit(LessThan lessExp) {
    Type expA = lessExp.e1.accept(this);
    Type expB = lessExp.e2.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    Type boolT = BooleanType.instance();
    lessExp.setType(boolT);
    return boolT;
  }

  public Type visit(LessOrEqual lessOrEqualExp) {
    Type expA = lessOrEqualExp.e1.accept(this);
    Type expB = lessOrEqualExp.e2.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    Type boolT = BooleanType.instance();
    lessOrEqualExp.setType(boolT);
    return boolT;
  }

  public Type visit(Greater greaterExp) {
    Type expA = greaterExp.e1.accept(this);
    Type expB = greaterExp.e2.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    Type boolT = BooleanType.instance();
    greaterExp.setType(boolT);
    return boolT;
  }

  public Type visit(GreaterOrEqual greaterOrEqualExp) {
    Type expA = greaterOrEqualExp.e1.accept(this);
    Type expB = greaterOrEqualExp.e2.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    Type boolT = BooleanType.instance();
    greaterOrEqualExp.setType(boolT);
    return boolT;
  }

  public Type visit(Equal equalExp) {
    Type expA = equalExp.e1.accept(this);
    Type expB = equalExp.e2.accept(this);

    if (!symbolTable.compareTypes(expA, expB))
      error(expA, expB);

    Type boolT = BooleanType.instance();
    equalExp.setType(boolT);
    return boolT;
  }

  public Type visit(NotEqual notEqualExp) {
    Type expA = notEqualExp.e1.accept(this);
    Type expB = notEqualExp.e2.accept(this);

    if (!symbolTable.compareTypes(expA, expB))
      error(expA, expB);

    Type boolT = BooleanType.instance();
    notEqualExp.setType(boolT);
    return boolT;
  }

  public Type visit(Plus plusExp) {
    Type expA = plusExp.e1.accept(this);
    Type expB = plusExp.e2.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    plusExp.setType(intT);
    return intT;
  }

  public Type visit(Minus minusExp) {
    Type expA = minusExp.e1.accept(this);
    Type expB = minusExp.e2.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    minusExp.setType(intT);
    return intT;
  }

  public Type visit(Times timesExp) {
    Type expA = timesExp.e1.accept(this);
    Type expB = timesExp.e2.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    timesExp.setType(intT);
    return intT;
  }

  public Type visit(Div divExp) {
    Type expA = divExp.e1.accept(this);
    Type expB = divExp.e2.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    divExp.setType(intT);
    return intT;
  }

  public Type visit(ArrayLookup arrayLookup) {
    Type expArr = arrayLookup.arrayExpr.accept(this);
    Type expInd = arrayLookup.indexExpr.accept(this);
    Type intT = IntegerType.instance();
    Type arrayT = IntArrayType.instance();

    if (!symbolTable.compareTypes(arrayT, expArr))
      error(arrayT, expArr);

    if (!symbolTable.compareTypes(intT, expInd))
      error(intT, expInd);

    arrayLookup.setType(intT);
    return intT;
  }

  public Type visit(ArrayLength arrayLength) {
    Type expArr = arrayLength.arrayExpr.accept(this);
    Type arrayT = IntArrayType.instance();

    if (!symbolTable.compareTypes(arrayT, expArr))
      error(arrayT, expArr);

    Type intT = IntegerType.instance();
    arrayLength.setType(intT);
    return intT;
  }

  public Type visit(Call callStmt) {
    Type objT = callStmt.objectExpr.accept(this);

    if (!(objT instanceof IdentifierType))
      error(new IdentifierType(""), objT);

    String classNameStr = ((IdentifierType)objT).className;
    String methodName = callStmt.methodId.name;
    MethodDescriptor method = symbolTable.getMethod(methodName, classNameStr);

    if (method.getParameters().size() != callStmt.paramExprList.size()) {
      System.out.println("Wrong call signature: different sizes");
      System.exit(1);
    }

    for (int i = 0; i < method.getParameters().size(); ++i) {
      VariableDescriptor param = method.getParameterAt(i);
      Exp exp = callStmt.paramExprList.elementAt(i);

      Type paramT = param.type();
      Type expT = exp.accept(this);

      if (!symbolTable.compareTypes(paramT, expT))
        error(paramT, expT);
    }

    callStmt.setType(method.getReturnType());
    return callStmt.getType();
  }

  public Type visit(IntegerLiteral intLit) {
    Type intT = IntegerType.instance();
    intLit.setType(intT);
    return intT;
  }

  public Type visit(True t) {
    Type boolT = BooleanType.instance();
    t.setType(boolT);
    return boolT;
  }

  public Type visit(False f) {
    Type boolT = BooleanType.instance();
    f.setType(boolT);
    return boolT;
  }

  public Type visit(This t) {
    t.setType(currentClass.getType());
    return t.getType();
  }

  public Type visit(NewArray newArrayExp) {
    Type expT = newArrayExp.sizeExpr.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, expT))
      error(intT, expT);

    Type arrType = IntArrayType.instance();
    newArrayExp.setType(arrType);
    return arrType;
  }

  public Type visit(NewObject newObjectExp) {
    Type identT = new IdentifierType(newObjectExp.classNameId.name);
    checkType(identT);
    newObjectExp.setType(identT);
    return identT;
  }

  public Type visit(Not notExp) {
    Type expT = notExp.boolExpr.accept(this);
    Type boolT = BooleanType.instance();

    if (!symbolTable.compareTypes(boolT, expT))
      error(boolT, expT);

    notExp.setType(boolT);
    return boolT;
  }

  public Type visit(Identifier id) {
    Type t = symbolTable.getVarType(
      currentMethod, currentClass, id.toString()
    );
    id.setType(t);
    return t;
  }

  public Type visit(PrefixAdd prefixAdd) {
    Type varT = prefixAdd.exp.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, varT))
      error(intT, varT);

    prefixAdd.setType(intT);
    return intT;
  }

  public Type visit(PrefixSub prefixSub) {
    Type varT = prefixSub.exp.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, varT))
      error(intT, varT);

    prefixSub.setType(intT);
    return intT;
  }

  public Type visit(PostfixAdd postfixAdd) {
    Type varT = postfixAdd.exp.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, varT))
      error(intT, varT);

    postfixAdd.setType(intT);
    return intT;
  }

  public Type visit(PostfixSub postfixSub) {
    Type varT = postfixSub.exp.accept(this);
    Type intT = IntegerType.instance();

    if (!symbolTable.compareTypes(intT, varT))
      error(intT, varT);

    postfixSub.setType(intT);
    return intT;
  }
}
