package backend.jasmin;

import util.NamePool;
import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.visitors.Visitor;

public class BytecodeEmitter implements Visitor {
  private SymbolTable symT;
  private String currentClass;
  private String currentMethod;

  public BytecodeEmitter() {
    symT = SymbolTable.getInstance();
  }

  private void emitStandardConstructor(String baseClass) {
    Bytecode.directive(".method public <init>()V");

    Bytecode.code(withConstant("iload", 0));
    Bytecode.code(
      "invokenonvirtual " + baseClass + "/<init>()V"
    );
    Bytecode.code("return");

    Bytecode.directive(".end method");
  }

  private int setupLocalsArray(MethodDescriptor md) {
    int pos = 0;

    for (VariableDescriptor vd : md.getParameters())
      vd.setOffset(++pos);

    for (VariableDescriptor vd : md.getLocalVars())
      vd.setOffset(++pos);

    return pos+1;
  }

  private MethodDescriptor getMethodDescriptor() {
    return symT.getMethod(currentMethod, currentClass);
  }

  private boolean isReferenceType(Type t) {
    return !(t instanceof IntegerType || t instanceof BooleanType);
  }

  private boolean isFieldVar(String varN) {
    ClassDescriptor cd = symT.getClass(currentClass);
    return cd.isInScope(varN);
  }

  private String withConstant(String cmd, int c) {
    int lim = cmd.equals("iconst") ? 5 : 3;
    return cmd + (c >= 0 && c <= lim ? '_' : ' ') + c;
  }

  private String typeDescriptor(Type t) {
    if (t instanceof IntegerType)
      return "I";
    else if (t instanceof BooleanType)
      return "B";
    else if (t instanceof IntArrayType)
      return "[I";
    else if (t instanceof IdentifierType)
      return "L" + (IdentifierType)t + ";";

    return null;
  }

  private String methodSignatureDescriptor(MethodDescriptor md) {
    String d = "(";

    for (VariableDescriptor vd : md.getParameters())
      d += typeDescriptor(vd.type());

    d += ")" + typeDescriptor(md.getReturnType());
    return d;
  }

  public void visit(Program program) {
    Bytecode.init();
    program.mainC.accept(this);
    Bytecode.save();

    for (ClassDecl classD : program.classList.getList()) {
      Bytecode.init();
      classD.accept(this);
      Bytecode.save();
    }
  }

  public void visit(MainClass mainC) {
    String classN = mainC.classNameId.name;
    Bytecode.setClassName(classN);

    Bytecode.directive(".class public " + classN);
    Bytecode.directive(".super java/lang/Object");
    
    Bytecode.newline2();
    emitStandardConstructor("java/lang/Object");

    MethodDescriptor methodD = symT.getMethod("main", classN);

    Bytecode.newline2();
    Bytecode.directive(".method public static main([Ljava/lang/String;)V");
    Bytecode.label(".limit locals " + setupLocalsArray(methodD));

    for (Statement stmt : mainC.statements.getList())
      stmt.accept(this);

    Bytecode.code("return");
    Bytecode.directive(".end method");
  }

  public void visit(ClassDeclSimple cd) {
    ClassDeclExtends cde = new ClassDeclExtends(
      cd.classId, new Identifier("java/lang/Object", null),
      cd.fieldVarList, cd.methodList
    );

    visit(cde);
  }

  public void visit(ClassDeclExtends classDecl) {
    String classN = classDecl.classId.name;
    String baseClassN = classDecl.baseClassId.name;
    currentClass = classN;

    Bytecode.directive(".class public " + classN);
    Bytecode.directive(".super " + baseClassN);

    for (VarDecl field : classDecl.fieldVarList.getList()) {
      Bytecode.newline();
      field.accept(this);
    }

    Bytecode.newline2();
    emitStandardConstructor(baseClassN);

    for (MethodDecl methodD : classDecl.methodList.getList()) {
      Bytecode.newline2();
      methodD.accept(this);
    }
  }

  public void visit(VarDecl varDecl) {
    String fieldN = varDecl.varId.name;
    String fieldT = typeDescriptor(varDecl.varType);
    
    Bytecode.directive(".field protected " + fieldN + " " + fieldT);
  }

  public void visit(MethodDecl mdecl) {
    String methodN = mdecl.methodNameId.name;
    MethodDescriptor md = symT.getMethod(methodN, currentClass);
    String methodSig = methodSignatureDescriptor(md);
    currentMethod = methodN;

    NamePool.reset();

    Bytecode.directive(".method public " + methodN + methodSig);
    Bytecode.label(".limit locals " + setupLocalsArray(md));

    for (Statement stmt : mdecl.statementList.getList())
      stmt.accept(this);

    mdecl.returnExpr.accept(this);

    if (isReferenceType(mdecl.methodReturnT))
      Bytecode.code("areturn");
    else
      Bytecode.code("ireturn");

    Bytecode.directive(".end method");
  }

  public void visit(Block block) {
    for (Statement stmt : block.stmtList.getList())
      stmt.accept(this);
  }

  public void visit(If ifStmt) {
    String trueL = NamePool.nextCode("if_true");
    String falseL = NamePool.nextCode("if_false");
    String nextL = NamePool.nextCode("if_next");

    evalBooleanJump(ifStmt.boolExpr, trueL, falseL);

    Bytecode.label(trueL);
    ifStmt.trueStmt.accept(this);
    Bytecode.code("goto " + nextL);

    Bytecode.label(falseL);
    ifStmt.falseStmt.accept(this);

    Bytecode.label(nextL);
  }

  public void visit(While whileStmt) {
    String loop = NamePool.nextCode("loop");
    String trueL = NamePool.nextCode("while_true");
    String falseL = NamePool.nextCode("while_false");

    Bytecode.label(loop);
    evalBooleanJump(whileStmt.boolExpr, trueL, falseL);

    Bytecode.label(trueL);
    whileStmt.stmt.accept(this);
    Bytecode.code("goto " + loop);

    Bytecode.label(falseL);
  }

  public void visit(For forStmt) {
    for (Statement stmt : forStmt.init.getList())
      stmt.accept(this);
    
    String loop = NamePool.nextCode("loop");
    String trueL = NamePool.nextCode("for_true");
    String falseL = NamePool.nextCode("for_false");

    Bytecode.label(loop);
    evalBooleanJump(forStmt.boolExpr, trueL, falseL);

    Bytecode.label(trueL);

    forStmt.body.accept(this);
    for (Statement stmt : forStmt.step.getList())
      stmt.accept(this);

    Bytecode.code("goto " + loop);
    Bytecode.label(falseL);
  }

  public void visit(Print printInt) {
    Bytecode.code("getstatic java/lang/System/out Ljava/io/PrintStream;");

    printInt.intExpr.accept(this);
    Bytecode.code(
      "invokestatic java/lang/String/valueOf(I)Ljava/lang/String;"
    );

    Bytecode.code(
      "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V"
    );
  }

  public void visit(PrintString printStr) {
    Bytecode.code("getstatic java/lang/System/out Ljava/io/PrintStream;");
    Bytecode.code("ldc \"" + printStr.str + "\"");
    Bytecode.code(
      "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V"
    );
  }

  public void visit(Assign assign) {
    assign.valueExpr.accept(this);

    String varN = assign.varId.name;
    String classN = currentClass;
    ClassDescriptor cd = symT.getClass(classN);

    if (varN.equals("@void")) {
      Bytecode.code("pop");
    }
    else if (cd.isInScope(varN)) {
      // field var
      Bytecode.code(withConstant("aload", 0));
      assign.valueExpr.accept(this);

      VariableDescriptor vd = cd.getVarInScope(varN);
      String varTypeD = typeDescriptor(vd.type());

      Bytecode.code("putfield " + classN + "/" + varN + " " + varTypeD);
    }
    else {
      // local var
      assign.valueExpr.accept(this);

      VariableDescriptor vd = getMethodDescriptor().getVar(varN);

      String cmd = isReferenceType(vd.type()) ? "astore" : "istore";
      Bytecode.code(withConstant(cmd, vd.getOffset()));
    }
  }

  public void visit(ArrayAssign arrayAssign) {
    arrayAssign.arrayId.accept(this);
    arrayAssign.indexExpr.accept(this);
    arrayAssign.valueExpr.accept(this);
    Bytecode.code("iastore");
  }

  public void visit(And and) {
    and.e1.accept(this);
    and.e2.accept(this);
    Bytecode.code("iand");
  }

  public void visit(Or or) {
    or.e1.accept(this);
    or.e2.accept(this);
    Bytecode.code("ior");
  }

  public void visit(Not not) {
    Bytecode.code(withConstant("iconst", 1));
    not.boolExpr.accept(this);
    Bytecode.code("isub");
  }

  public void visit(Equal e) {
    visitComparison(e, "equal");
  }

  public void visit(NotEqual ne) {
    visitComparison(ne, "not_equal");
  }

  public void visit(Greater g) {
    visitComparison(g, "greater");
  }

  public void visit(GreaterOrEqual ge) {
    visitComparison(ge, "greater_or_equal");
  }

  public void visit(LessOrEqual le) {
    visitComparison(le, "less_or_equal");
  }

  public void visit(LessThan l) {
    visitComparison(l, "less_than");
  }

  public void visit(Plus add) {
    add.e1.accept(this);
    add.e2.accept(this);
    Bytecode.code("iadd");
  }

  public void visit(Minus minus) {
    minus.e1.accept(this);
    minus.e2.accept(this);
    Bytecode.code("isub");
  }

  public void visit(Times mult) {
    mult.e1.accept(this);
    mult.e2.accept(this);
    Bytecode.code("imul");
  }

  public void visit(Div div) {
    div.e1.accept(this);
    div.e2.accept(this);
    Bytecode.code("idiv");
  }

  public void visit(ArrayLookup lookup) {
    lookup.arrayExpr.accept(this);
    lookup.indexExpr.accept(this);
    Bytecode.code("iaload");
  }

  public void visit(ArrayLength length) {
    length.arrayExpr.accept(this);
    Bytecode.code("arraylength");
  }

  public void visit(PrefixAdd preAdd) {
    Identifier var = (Identifier)preAdd.exp;

    if (!isFieldVar(var.name)) {
      MethodDescriptor md = getMethodDescriptor();
      VariableDescriptor vd = md.getVar(var.name);

      Bytecode.code("iinc " + vd.getOffset() + " 1");
    }
    else {
      Exp plus = new Plus(var, new IntegerLiteral(1));
      new Assign(var, plus).accept(this);
    }

    var.accept(this);
  }

  public void visit(PrefixSub preSub) {
    Identifier var = (Identifier)preSub.exp;

    if (!isFieldVar(var.name)) {
      MethodDescriptor md = getMethodDescriptor();
      VariableDescriptor vd = md.getVar(var.name);

      Bytecode.code("iinc " + vd.getOffset() + " -1");
    }
    else {
      Exp sub = new Minus(var, new IntegerLiteral(1));
      new Assign(var, sub).accept(this);
    }

    var.accept(this);
  }

  public void visit(PostfixAdd posAdd) {
    Identifier var = (Identifier)posAdd.exp;
    var.accept(this);

    if (!isFieldVar(var.name)) {
      MethodDescriptor md = getMethodDescriptor();
      VariableDescriptor vd = md.getVar(var.name);

      Bytecode.code("iinc " + vd.getOffset() + " 1");
    }
    else {
      Exp plus = new Plus(var, new IntegerLiteral(1));
      new Assign(var, plus).accept(this);
    }
  }

  public void visit(PostfixSub posSub) {
    Identifier var = (Identifier)posSub.exp;
    var.accept(this);

    if (!isFieldVar(var.name)) {
      MethodDescriptor md = getMethodDescriptor();
      VariableDescriptor vd = md.getVar(var.name);

      Bytecode.code("iinc " + vd.getOffset() + " -1");
    }
    else {
      Exp minus = new Minus(var, new IntegerLiteral(1));
      new Assign(var, minus).accept(this);
    }
  }

  public void visit(Call call) {
    IdentifierType objType = (IdentifierType)call.objectExpr.getType();
    String classN = objType.className;
    String methodN = call.methodId.name;
    MethodDescriptor md = symT.getMethod(methodN, classN);

    String callDescriptor = classN + "/" + methodN
                          + methodSignatureDescriptor(md);
    
    call.objectExpr.accept(this);
    for (Exp param : call.paramExprList.getList())
      param.accept(this);

    Bytecode.code("invokevirtual " + callDescriptor);
  }

  public void visit(IntegerLiteral intLit) {
    Bytecode.code(withConstant("iconst", intLit.value));
  }

  public void visit(True trueV) {
    Bytecode.code(withConstant("iconst", 1));
  }

  public void visit(False falseV) {
    Bytecode.code(withConstant("iconst", 0));
  }

  public void visit(This thisV) {
    Bytecode.code(withConstant("aload", 0));
  }

  public void visit(NewArray newArray) {
    newArray.sizeExpr.accept(this);
    Bytecode.code("newarray I");
  }

  public void visit(NewObject newObj) {
    Bytecode.code("new " + newObj.classNameId);
  }

  public void visit(Identifier id) {
    String varN = id.name;

    if (isFieldVar(varN)) {
      ClassDescriptor cd = symT.getClass(currentClass);
      VariableDescriptor vd = cd.getVarInScope(varN);
      String varTypeD = typeDescriptor(vd.type());

      Bytecode.code(withConstant("aload", 0));
      Bytecode.code("getfield " + currentClass + "/" + varN + " " + varTypeD);
    }
    else {
      MethodDescriptor md = getMethodDescriptor();
      VariableDescriptor vd = md.getVar(varN);

      String cmd = isReferenceType(vd.type()) ? "aload" : "iload";
      Bytecode.code(withConstant(cmd, vd.getOffset()));
    }
  }

  public void visit(Formal n) {
    throw new IllegalArgumentException("BytecodeEmitter::Formal");
  }

  public void visit(IntArrayType n) {
    throw new IllegalArgumentException("BytecodeEmitter::IntArrayType");
  }

  public void visit(BooleanType n) {
    throw new IllegalArgumentException("BytecodeEmitter::BooleanType");
  }

  public void visit(IntegerType n) {
    throw new IllegalArgumentException("BytecodeEmitter::IntegerType");
  }

  public void visit(VoidType n) {
    throw new IllegalArgumentException("BytecodeEmitter::VoidType");
  }

  public void visit(IdentifierType n) {
    throw new IllegalArgumentException("BytecodeEmitter::IdentifierType");
  }

  private void evalBooleanJump(Exp e, String trueL, String falseL) {
    if (e instanceof False) {
      Bytecode.code("goto " + trueL);
    }
    else if (e instanceof False) {
      Bytecode.code("goto " + falseL);
    }
    else if (e instanceof Not) {
      evalBooleanJump(((Not)e).boolExpr, falseL, trueL);
    }
    else if (e instanceof And) {
      And and = (And)e;
      String cont = NamePool.nextCode("and_cont");

      evalBooleanJump(and.e1, cont, falseL);
      Bytecode.label(cont);
      evalBooleanJump(and.e2, trueL, falseL);
    }
    else if (e instanceof Or) {
      Or or = (Or)e;
      String cont = NamePool.nextCode("or_Cont");

      evalBooleanJump(or.e1, trueL, cont);
      Bytecode.label(cont);
      evalBooleanJump(or.e2, trueL, falseL);
    }
    else if (e instanceof LessThan) {
      LessThan l = (LessThan)e;
      evalConditionJump("lt", l.e1, l.e2, trueL, falseL);
    }
    else if (e instanceof LessOrEqual) {
      LessOrEqual le = (LessOrEqual)e;
      evalConditionJump("le", le.e1, le.e2, trueL, falseL);
    }
    else if (e instanceof Greater) {
      Greater g = (Greater)e;
      evalConditionJump("gt", g.e1, g.e2, trueL, falseL);
    }
    else if (e instanceof GreaterOrEqual) {
      GreaterOrEqual ge = (GreaterOrEqual)e;
      evalConditionJump("ge", ge.e1, ge.e2, trueL, falseL);
    }
    else if (e instanceof Equal) {
      Equal eq = (Equal)e;
      evalConditionJump("eq", eq.e1, eq.e2, trueL, falseL);
    }
    else if (e instanceof NotEqual) {
      NotEqual neq = (NotEqual)e;
      evalConditionJump("ne", neq.e1, neq.e2, trueL, falseL);
    }
    else if (e instanceof Identifier) {
      Exp notEq = new NotEqual(e, new IntegerLiteral(0));
      evalBooleanJump(notEq, trueL, falseL);
    }
    else if (e instanceof Call) {
      Exp notEq = new NotEqual(e, new IntegerLiteral(0));
      evalBooleanJump(notEq, trueL, falseL);
    }
    else {
      throw new IllegalArgumentException("evalBooleanJump");
    }
  }

  private void evalConditionJump(
      String cond, Exp e1, Exp e2, String trueL, String falseL) {

    e1.accept(this);
    e2.accept(this);

    char typeCode = isReferenceType(e1.getType()) ? 'a' : 'i';
    String cmd = "if_" + typeCode + "cmp" + cond;

    Bytecode.code(cmd + ' ' + trueL);
    Bytecode.code("goto " + falseL);
  }

  private void visitComparison(Exp cmp, String s) {
    String trueL = NamePool.nextCode(s);
    String falseL = NamePool.nextCode("not_" + s);
    String nextL = NamePool.nextCode("next");

    evalBooleanJump(cmp, trueL, falseL);

    Bytecode.label(trueL);
    Bytecode.code(withConstant("iconst", 1));
    Bytecode.code("goto " + nextL);

    Bytecode.label(falseL);
    Bytecode.code(withConstant("iconst", 0));

    Bytecode.label(nextL);
  }
}
