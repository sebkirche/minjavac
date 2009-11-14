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

  private void error(Type expected, Type inferred) {
    System.out.println("Expected type " + expected + ", got " + inferred);
    System.exit(1);
  }

  private void checkType(Type type) {
    if (!(type instanceof IdentifierType))
      return;

    IdentifierType it = (IdentifierType)type;
    String className = it.s;

    if (symbolTable.getClass(className) == null) {
      System.out.println("Unknown class: " + className);
      System.exit(1);
    }
  }

  private Type getVarType(Identifier i) {
    return symbolTable.getVarType(currentMethod, currentClass, i.s);
  }

  public Type visit(Program program) {
    program.m.accept(this);

    for (ClassDecl classD : program.cl.getList())
      classD.accept(this);

    return null;
  }

  public Type visit(MainClass mainC) {
    currentClass = symbolTable.getClass(mainC.i1.toString());
    currentMethod = currentClass.getMethod("main");

    mainC.s.accept(this);

    currentMethod = null;
    currentClass = null;
    return null;
  }

  public Type visit(ClassDeclSimple classDecl) {
    String name = classDecl.i.toString();
    currentClass = symbolTable.getClass(name);

    for (VarDecl varD : classDecl.vl.getList())
      varD.accept(this);

    for (MethodDecl methodD : classDecl.ml.getList())
      methodD.accept(this);

    currentClass = null;
    return null;
  }

  public Type visit(ClassDeclExtends classDecl) {
    String name = classDecl.i.toString();
    currentClass = symbolTable.getClass(name);

    for (VarDecl varD : classDecl.vl.getList())
      varD.accept(this);

    for (MethodDecl methodD : classDecl.ml.getList())
      methodD.accept(this);

    currentClass = null;
    return null;
  }

  public Type visit(VarDecl varDecl) {
    checkType(varDecl.t);
    return null;
  }

  public Type visit(MethodDecl methodDecl) {
    checkType(methodDecl.t);
    currentMethod = currentClass.getMethod(methodDecl.i.toString());

    for (Formal param : methodDecl.fl.getList())
      param.accept(this);

    for (VarDecl varDecl : methodDecl.vl.getList())
      varDecl.accept(this);

    for (Statement stmt : methodDecl.sl.getList())
      stmt.accept(this);

    Type methodType = methodDecl.t;
    Type returnType = methodDecl.e.accept(this);

    if (!symbolTable.compareTypes(methodType, returnType))
      error(methodType, returnType);

    currentMethod = null;
    return null;
  }

  public Type visit(Formal param) {
    checkType(param.t);
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

  public Type visit(IdentifierType n) {
    return null;
  }

  public Type visit(Block block) {
    for (Statement stmt : block.sl.getList())
      stmt.accept(this);
    return null;
  }

  public Type visit(If ifStmt) {
    Type expT = ifStmt.e.accept(this);
    Type booleanT = new BooleanType();

    if (!symbolTable.compareTypes(booleanT, expT))
      error(booleanT, expT);

    ifStmt.s1.accept(this);
    ifStmt.s2.accept(this);
    return null;
  }

  public Type visit(While whileStmt) {
    Type expT = whileStmt.e.accept(this);
    Type booleanT = new BooleanType();

    if (!symbolTable.compareTypes(booleanT, expT))
      error(booleanT, expT);

    whileStmt.s.accept(this);
    return null;
  }

  public Type visit(Print printStmt) {
    Type expT = printStmt.e.accept(this);
    Type intT = new IntegerType();

    if (!symbolTable.compareTypes(intT, expT))
      error(intT, expT);

    return null;
  }

  public Type visit(Assign assignStmt) {
    Type varT = getVarType(assignStmt.i);
    Type expT = assignStmt.e.accept(this);

    if (!symbolTable.compareTypes(varT, expT))
      error(varT, expT);

    return null;
  }

  public Type visit(ArrayAssign arrayAssign) {
    Type intT = new IntegerType();
    Type indexT = arrayAssign.e1.accept(this);
    Type expT = arrayAssign.e2.accept(this);

    if (!symbolTable.compareTypes(intT, indexT))
      error(intT, indexT);

    if (!symbolTable.compareTypes(intT, expT))
      error(intT, expT);

    return null;
  }

  public Type visit(And andExp) {
    Type expA = andExp.e1.accept(this);
    Type expB = andExp.e2.accept(this);
    Type boolT = new BooleanType();

    if (!symbolTable.compareTypes(boolT, expA))
      error(boolT, expA);

    if (!symbolTable.compareTypes(boolT, expB))
      error(boolT, expB);

    return boolT;
  }

  public Type visit(LessThan lessExp) {
    Type expA = lessExp.e1.accept(this);
    Type expB = lessExp.e2.accept(this);
    Type intT = new IntegerType();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    return new BooleanType();
  }

  public Type visit(Plus plusExp) {
    Type expA = plusExp.e1.accept(this);
    Type expB = plusExp.e2.accept(this);
    Type intT = new IntegerType();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    return intT;
  }

  public Type visit(Minus minusExp) {
    Type expA = minusExp.e1.accept(this);
    Type expB = minusExp.e2.accept(this);
    Type intT = new IntegerType();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    return intT;
  }

  public Type visit(Times timesExp) {
    Type expA = timesExp.e1.accept(this);
    Type expB = timesExp.e2.accept(this);
    Type intT = new IntegerType();

    if (!symbolTable.compareTypes(intT, expA))
      error(intT, expA);

    if (!symbolTable.compareTypes(intT, expB))
      error(intT, expB);

    return intT;
  }

  public Type visit(ArrayLookup arrayLookup) {
    Type expArr = arrayLookup.e1.accept(this);
    Type expInd = arrayLookup.e2.accept(this);
    Type arrayT = new IntArrayType();
    Type intT = new IntegerType();

    if (!symbolTable.compareTypes(arrayT, expArr))
      error(arrayT, expArr);

    if (!symbolTable.compareTypes(intT, expInd))
      error(intT, expInd);

    return intT;
  }

  public Type visit(ArrayLength arrayLength) {
    Type expArr = arrayLength.e.accept(this);
    Type arrayT = new IntArrayType();

    if (!symbolTable.compareTypes(arrayT, expArr))
      error(arrayT, expArr);

    return new IntegerType();
  }

  public Type visit(Call callStmt) {
    Type objT = callStmt.e.accept(this);

    if (!(objT instanceof IdentifierType))
      error(new IdentifierType(""), objT);

    String className = ((IdentifierType)objT).s;
    String methodName = callStmt.i.s;
    Method method = symbolTable.getMethod(methodName, className);

    if (method.getParameters().size() != callStmt.el.size()) {
      System.out.println("Wrong call signature: different sizes");
      System.exit(1);
    }

    for (int i = 0; i < method.getParameters().size(); ++i) {
      Variable param = method.getParameterAt(i);
      Exp exp = callStmt.el.elementAt(i);

      Type paramT = param.type();
      Type expT = exp.accept(this);

      if (!symbolTable.compareTypes(paramT, expT))
        error(paramT, expT);
    }

    return method.getReturnType();
  }

  public Type visit(IntegerLiteral n) {
    return new IntegerType();
  }

  public Type visit(True n) {
    return new BooleanType();
  }

  public Type visit(False n) {
    return new BooleanType();
  }

  public Type visit(This n) {
    return currentClass.getType();
  }

  public Type visit(NewArray newArrayExp) {
    Type expT = newArrayExp.e.accept(this);
    Type intT = new IntegerType();

    if (!symbolTable.compareTypes(intT, expT))
      error(intT, expT);

    return new IntArrayType();
  }

  public Type visit(NewObject newObjectExp) {
    Type identT = new IdentifierType(newObjectExp.i.s);
    checkType(identT);
    return identT;
  }

  public Type visit(Not notExp) {
    Type expT = notExp.e.accept(this);
    Type boolT = new BooleanType();

    if (!symbolTable.compareTypes(boolT, expT))
      error(boolT, expT);
    
    return boolT;
  }

  public Type visit(Identifier n) {
    return null;
  }
}
