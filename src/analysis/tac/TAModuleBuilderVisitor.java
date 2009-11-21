package analysis.tac;

import java.util.List;
import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.tac.variables.*;
import analysis.visitors.Visitor;
import analysis.tac.instructions.*;

public class TAModuleBuilderVisitor implements Visitor {
  private TAModule module;
  private TAVariable lastTemp;
  private SymbolTable symbolTable;
 
  public TAModuleBuilderVisitor() {
    lastTemp = null;
    symbolTable = SymbolTable.getInstance();
    module = new TAModule();
    TAModule.setInstance(module);
  }

  public TAModule getModule() {
    return module;
  }

  public void visit(Program program) {
    program.mainC.accept(this);

    for (ClassDecl c : program.classList.getList())
      c.accept(this);
  }

  public void visit(MainClass mainC) {
    module.startClass(mainC.classNameId.name);
    module.startProcedure("main");

    for (Statement stmt : mainC.statements.getList())
      stmt.accept(this);

    module.closeProcedure();
    module.closeClass();
  }

  public void visit(ClassDeclSimple classD) {
    module.startClass(classD.classId.name);
    
    for (MethodDecl methodDecl : classD.methodList.getList())
      methodDecl.accept(this);
    
    module.closeClass();
  }

  public void visit(ClassDeclExtends classD) {
    module.startClass(classD.classId.name);

    for (MethodDecl methodDecl : classD.methodList.getList())
      methodDecl.accept(this);

    module.closeClass();
  }

  public void visit(MethodDecl methodD) {
    TANamePool.reset();
    module.startProcedure(methodD.methodNameId.name);

    for (Statement stmt : methodD.statementList.getList())
      stmt.accept(this);

    methodD.returnExpr.accept(this);
    module.addInstruction(new Return(lastTemp));
    module.closeProcedure();
  }

  public void visit(Block block) {
    for (Statement stmt : block.stmtList.getList())
      stmt.accept(this);
  }

  public void visit(If ifStmt) {
    Label trueL = TANamePool.newLabel("if_true");
    Label falseL = TANamePool.newLabel("if_false");
    Label next = TANamePool.newLabel("if_next");

    evalBooleanJump(ifStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);
    ifStmt.trueStmt.accept(this);
    module.addInstruction(new Jump(next));

    module.addInstruction(falseL);
    ifStmt.falseStmt.accept(this);

    module.addInstruction(next);
  }

  public void visit(While whileStmt) {
    Label loop = TANamePool.newLabel("loop");
    Label trueL = TANamePool.newLabel("while_true");
    Label falseL = TANamePool.newLabel("while_false");

    module.addInstruction(loop);
    evalBooleanJump(whileStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);
    whileStmt.stmt.accept(this);
    module.addInstruction(new Jump(loop));

    module.addInstruction(falseL);
  }

  public void visit(For forStmt) {
    for (Statement stmt : forStmt.init.getList())
      stmt.accept(this);

    Label loop = TANamePool.newLabel("loop");
    Label trueL = TANamePool.newLabel("for_true");
    Label falseL = TANamePool.newLabel("for_false");

    module.addInstruction(loop);
    evalBooleanJump(forStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);

    forStmt.body.accept(this);
    for (Statement stmt : forStmt.step.getList())
      stmt.accept(this);
    
    module.addInstruction(new Jump(loop));
    module.addInstruction(falseL);
  }

  public void visit(Print printStmt) {
    printStmt.intExpr.accept(this);

    module.addInstruction(new Action(Opcode.SAVE_C_CTX));
    module.addInstruction(new ParameterSetup(lastTemp));

    module.addInstruction(new ProcedureCall(
      TANamePool.newVar("void", IntegerType.instance()), new Label("_print_int")
    ));
    
    module.addInstruction(new Action(Opcode.LOAD_C_CTX));
  }

  public void visit(PrintString printStr) {
    String handle = module.addStringLiteral(printStr.str);
    TALabelPointerVar var = new TALabelPointerVar(handle);

    module.addInstruction(new Action(Opcode.SAVE_C_CTX));
    module.addInstruction(new ParameterSetup(var));

    module.addInstruction(new ProcedureCall(
      TANamePool.newVar("void", IntegerType.instance()), new Label("_print_str")
    ));

    module.addInstruction(new Action(Opcode.LOAD_C_CTX));
  }

  public void visit(Assign assignStmt) {
    assignStmt.valueExpr.accept(this);

    String varN = assignStmt.varId.name;
    String classN = module.getOpenClass().getName();
    ClassDescriptor cd = symbolTable.getClass(classN);

    TAVariable destiny;
    if (cd.isInScope(varN))
      destiny = new TAFieldVar(varN);
    else
      destiny = getTAVariable(varN);

    module.addInstruction(new Copy(destiny, lastTemp));
  }

  public void visit(ArrayAssign arrayAssign) {
    TAVariable arrayVar = getTAVariable(arrayAssign.arrayId.name);

    arrayAssign.indexExpr.accept(this);
    TAVariable indexVar = lastTemp;

    arrayAssign.valueExpr.accept(this);
    TAVariable valueVar = lastTemp;

    TAArrayCellVar dest = new TAArrayCellVar(arrayVar, indexVar);
    module.addInstruction(new Copy(dest, valueVar));
  }

  public void visit(And andExpr) {
    TAVariable a, b;
    TAVariable temp = TANamePool.newVar("and", BooleanType.instance());

    andExpr.e1.accept(this);
    a = lastTemp;

    andExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.AND, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(Or orExpr) {
    TAVariable a, b;
    TAVariable temp = TANamePool.newVar("or", BooleanType.instance());

    orExpr.e1.accept(this);
    a = lastTemp;

    orExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.OR, temp, a, b
    ));

    lastTemp = temp;
  }

  private void booleanTest(Exp e1, Exp e2, Condition cond) {
    TAVariable a, b;
    TAVariable temp = TANamePool.newVar(cond + "_than", BooleanType.instance());

    e1.accept(this);
    a = lastTemp;

    e2.accept(this);
    b = lastTemp;

    Label trueL = TANamePool.newLabel("is_" + cond);
    Label nextL = TANamePool.newLabel("next");

    module.addInstruction(new ConditionalJump(
      cond, a, b, trueL
    ));

    module.addInstruction(new Copy(temp, new TAConstantVar(0)));
    module.addInstruction(new Jump(nextL));
    module.addInstruction(trueL);
    module.addInstruction(new Copy(temp, new TAConstantVar(1)));
    module.addInstruction(nextL);

    lastTemp = temp;
  }

  public void visit(LessThan lessExpr) {
    booleanTest(lessExpr.e1, lessExpr.e2, Condition.LESS_THAN);
  }

  public void visit(LessOrEqual lessOrEqual) {
    booleanTest(lessOrEqual.e1, lessOrEqual.e2, Condition.LESS_OR_EQUAL);
  }

  public void visit(Greater greater) {
    booleanTest(greater.e1, greater.e2, Condition.GREATER);
  }

  public void visit(GreaterOrEqual greaterOrEqual) {
    booleanTest(greaterOrEqual.e1, greaterOrEqual.e2, Condition.GREATER_OR_EQUAL);
  }

  public void visit(Equal equal) {
    booleanTest(equal.e1, equal.e2, Condition.EQUAL);
  }

  public void visit(NotEqual notEqual) {
    booleanTest(notEqual.e1, notEqual.e2, Condition.NOT_EQUAL);
  }

  public void visit(Plus plusExpr) {
    TAVariable a, b;
    TAVariable temp = TANamePool.newVar("add", IntegerType.instance());

    plusExpr.e1.accept(this);
    a = lastTemp;

    plusExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.ADD, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(Minus minusExpr) {
    TAVariable a, b;
    TAVariable temp = TANamePool.newVar("sub", IntegerType.instance());

    minusExpr.e1.accept(this);
    a = lastTemp;

    minusExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.SUB, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(Times timesExpr) {
    TAVariable a, b;
    TAVariable temp = TANamePool.newVar("mult", IntegerType.instance());

    timesExpr.e1.accept(this);
    a = lastTemp;

    timesExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.MULT, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(Div div) {
    TAVariable a, b;
    TAVariable temp = TANamePool.newVar("div", IntegerType.instance());

    div.e1.accept(this);
    a = lastTemp;

    div.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.DIV, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(ArrayLookup lookup) {
    TAVariable temp = TANamePool.newVar("array_lookup", IntegerType.instance());

    lookup.indexExpr.accept(this);
    TAVariable index = lastTemp;

    lookup.arrayExpr.accept(this);
    TAVariable array = lastTemp;

    TAArrayCellVar cell = new TAArrayCellVar(array, index);

    module.addInstruction(new Copy(temp, cell));
    lastTemp = temp;
  }

  public void visit(ArrayLength length) {
    TAVariable temp = TANamePool.newVar("array_length", IntegerType.instance());

    length.arrayExpr.accept(this);
    TAVariable arrayVar = lastTemp;

    module.addInstruction(new Operation(
      Opcode.ARRAY_LENGTH, temp, arrayVar
    ));

    lastTemp = temp;
  }

  public void visit(PrefixAdd prefixAdd) {
    Identifier var = (Identifier)prefixAdd.exp;
    Exp add = new Plus(var, new IntegerLiteral(1));
    Assign assign = new Assign(var, add);

    visit(assign);
    visit(var);
  }

  public void visit(PrefixSub prefixSub) {
    Identifier var = (Identifier)prefixSub.exp;
    Exp minus = new Minus(var, new IntegerLiteral(1));
    Assign assign = new Assign(var, minus);

    visit(assign);
    visit(var);
  }

  public void visit(PostfixAdd postfixAdd) {
    Identifier var = (Identifier)postfixAdd.exp;
    Type intT = IntegerType.instance();
    
    TAVariable temp = TANamePool.newVar("old_" + var, intT);

    visit(var);
    module.addInstruction(new Copy(temp, lastTemp));

    Exp add = new Plus(var, new IntegerLiteral(1));
    Assign assign = new Assign(var, add);
    visit(assign);

    lastTemp = temp;
  }

  public void visit(PostfixSub postifxSub) {
    Identifier var = (Identifier)postifxSub.exp;
    Type intT = IntegerType.instance();

    TAVariable temp = TANamePool.newVar("old_" + var, intT);

    visit(var);
    module.addInstruction(new Copy(temp, lastTemp));

    Exp minus = new Minus(var, new IntegerLiteral(1));
    Assign assign = new Assign(var, minus);
    visit(assign);

    lastTemp = temp;
  }

  public void visit(Call callStmt) {
    callStmt.objectExpr.accept(this);
    TAVariable objectRef = lastTemp;

    module.addInstruction(new Action(Opcode.SAVE_CTX));

    List<Exp> params = callStmt.paramExprList.getList();
    for (int i = params.size()-1; i >= 0; --i) {
      params.get(i).accept(this);
      module.addInstruction(new ParameterSetup(lastTemp));
    }

    module.addInstruction(new ParameterSetup(
      new TAThisReferenceVar(objectRef)
    ));

    IdentifierType objType = (IdentifierType)callStmt.objectExpr.getType();
    String classN = objType.className;
    String methodN = callStmt.methodId.name;
    MethodDescriptor methodD = symbolTable.getMethod(methodN, classN);

    Label procLabel = new Label(classN + "@" + methodN);
    TAVariable temp = TANamePool.newVar("call", methodD.getReturnType());

    module.addInstruction(new ProcedureCall(temp, procLabel));
    module.addInstruction(new Action(Opcode.LOAD_CTX));

    lastTemp = temp;
  }

  public void visit(IntegerLiteral intLiteral) {
    lastTemp = new TAConstantVar(intLiteral.value);
  }

  public void visit(True t) {
    lastTemp = new TAConstantVar(1);
  }

  public void visit(False f) {
    lastTemp = new TAConstantVar(0);
  }

  public void visit(This thisExp) {
    lastTemp = new TAThisVar();
  }

  public void visit(NewArray newArray) {
    TAVariable temp = TANamePool.newVar("new_array", IntArrayType.instance());
    
    newArray.sizeExpr.accept(this);
    TAVariable size = lastTemp;

    module.addInstruction(new Action(Opcode.SAVE_C_CTX));
    module.addInstruction(new ParameterSetup(size));
    module.addInstruction(new ProcedureCall(
      temp, new Label("_new_array")
    ));
    module.addInstruction(new Action(Opcode.LOAD_C_CTX));

    lastTemp = temp;
  }

  public void visit(NewObject newObj) {
    String classN = newObj.classNameId.name;
    Type tempT = new IdentifierType(classN);
    TAVariable temp = TANamePool.newVar("new_" + newObj.classNameId, tempT);

    String procLabel = newObj.classNameId + "@@new";

    module.addInstruction(new Action(Opcode.SAVE_CTX));
    module.addInstruction(new ProcedureCall(
      temp, new Label(procLabel)
    ));
    module.addInstruction(new Action(Opcode.LOAD_CTX));
            
    lastTemp = temp;
  }

  public void visit(Not notExp) {
    TAVariable temp = TANamePool.newVar("not", BooleanType.instance());

    notExp.boolExpr.accept(this);

    module.addInstruction(new Operation(
      Opcode.NOT, temp, lastTemp
    ));

    lastTemp = temp;
  }

  public void visit(Identifier varId) {
    lastTemp = getTAVariable(varId.name);
  }

  public void visit(VarDecl varD) {
    throw new IllegalArgumentException("visit@VarDecl");
  }

  public void visit(Formal param) {
    throw new IllegalArgumentException("visit@Formal");
  }

  public void visit(IntArrayType n) {
    throw new IllegalArgumentException("visit@IntArrayType");
  }

  public void visit(BooleanType n) {
    throw new IllegalArgumentException("visit@BooleanType");
  }

  public void visit(IntegerType n) {
    throw new IllegalArgumentException("visit@IntegerType");
  }

  public void visit(VoidType n) {
    throw new IllegalArgumentException("visit@IntegerType");
  }

  public void visit(IdentifierType n) {
    throw new IllegalArgumentException("visit@IdentifierType");
  }

  private TAVariable getTAVariable(String name) {
    TAClass c = module.getOpenClass();
    ClassDescriptor cd = symbolTable.getClass(c.getName());
    
    if (!cd.isInScope(name))
      return new TALocalVar(name);

    TAFieldVar fv = new TAFieldVar(name);
    TAVariable temp = TANamePool.newVar(name, cd.getVarInScope(name).type());

    module.addInstruction(new Copy(temp, fv));

    return temp;
  }

  private void evalConditionJump(
        Condition cond, Exp e1, Exp e2, Label trueL, Label falseL) {
    
      e1.accept(this);
      TAVariable a = lastTemp;

      e2.accept(this);
      TAVariable b = lastTemp;

      module.addInstruction(new ConditionalJump(
        cond, a, b, trueL
      ));

      module.addInstruction(new Jump(falseL));
  }

  private void evalBooleanJump(Exp e, Label trueL, Label falseL) {
    if (e instanceof False) {
      module.addInstruction(new Jump(falseL));
    }
    else if (e instanceof True) {
      module.addInstruction(new Jump(trueL));
    }
    else if (e instanceof Not) {
      evalBooleanJump(((Not)e).boolExpr, falseL, trueL);
    }
    else if (e instanceof And) {
      And andExpr = (And)e;
      Label cont = TANamePool.newLabel("and_cont");

      evalBooleanJump(andExpr.e1, cont, falseL);
      module.addInstruction(cont);
      evalBooleanJump(andExpr.e2, trueL, falseL);
    }
    else if (e instanceof Or) {
      Or orExpr = (Or)e;
      Label cont = TANamePool.newLabel("or_cont");

      evalBooleanJump(orExpr.e1, trueL, cont);
      module.addInstruction(cont);
      evalBooleanJump(orExpr.e2, trueL, falseL);
    }
    else if (e instanceof LessThan) {
      LessThan l = (LessThan)e;
      evalConditionJump(Condition.LESS_THAN, l.e1, l.e2, trueL, falseL);
    }
    else if (e instanceof LessOrEqual) {
      LessOrEqual le = (LessOrEqual)e;
      evalConditionJump(Condition.LESS_OR_EQUAL, le.e1, le.e2, trueL, falseL);
    }
    else if (e instanceof Greater) {
      Greater g = (Greater)e;
      evalConditionJump(Condition.GREATER, g.e1, g.e2, trueL, falseL);
    }
    else if (e instanceof GreaterOrEqual) {
      GreaterOrEqual ge = (GreaterOrEqual)e;
      evalConditionJump(Condition.GREATER_OR_EQUAL, ge.e1, ge.e2, trueL, falseL);
    }
    else if (e instanceof Equal) {
      Equal eq = (Equal)e;
      evalConditionJump(Condition.EQUAL, eq.e1, eq.e2, trueL, falseL);
    }
    else if (e instanceof NotEqual) {
      NotEqual neq = (NotEqual)e;
      evalConditionJump(Condition.NOT_EQUAL, neq.e1, neq.e2, trueL, falseL);
    }
    else if (e instanceof Identifier) {
      TAVariable boolVar = getTAVariable(((Identifier)e).name);

      module.addInstruction(new ConditionalJump(
        Condition.IS_TRUE, boolVar, null, trueL
      ));

      module.addInstruction(new Jump(falseL));
    }
    else if (e instanceof Call) {
      e.accept(this);
      TAVariable returnVar = lastTemp;

      module.addInstruction(new ConditionalJump(
        Condition.IS_TRUE, returnVar, null, trueL
      ));

      module.addInstruction(new Jump(falseL));
    }
    else {
      throw new IllegalArgumentException("evalBooleanJump");
    }
  }
}
