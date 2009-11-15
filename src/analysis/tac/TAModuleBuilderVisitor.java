package analysis.tac;

import java.util.List;
import analysis.syntaxtree.*;
import analysis.tac.variables.*;
import analysis.tac.instructions.*;
import analysis.visitors.Visitor;
import analysis.symboltable.SymbolTable;

public class TAModuleBuilderVisitor implements Visitor {
  private TAModule module;
  private Variable lastTemp;
  private SymbolTable symbolTable;
 
  public TAModuleBuilderVisitor() {
    lastTemp = null;
    symbolTable = SymbolTable.getInstance();
    module = new TAModule();
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

    mainC.mainStmt.accept(this);

    module.closeProcedure();
    module.closeClass();
  }

  public void visit(ClassDeclSimple classD) {
    module.startClass(classD.classId.name);

    for (VarDecl varDecl : classD.fieldVarList.getList())
      module.addStaticVar(varDecl.varId.name);
    
    for (MethodDecl methodDecl : classD.methodList.getList())
      methodDecl.accept(this);
    
    module.closeClass();
  }

  public void visit(ClassDeclExtends classD) {
    module.startClass(classD.classId.name);

    for (VarDecl varDecl : classD.fieldVarList.getList())
      module.addStaticVar(varDecl.varId.name);

    for (MethodDecl methodDecl : classD.methodList.getList())
      methodDecl.accept(this);

    module.closeClass();
  }

  public void visit(VarDecl varD) { }

  public void visit(Formal param) { }

  public void visit(IntArrayType n) { }

  public void visit(BooleanType n) { }

  public void visit(IntegerType n) { }

  public void visit(IdentifierType n) { }

  public void visit(MethodDecl methodD) {
    NamePool.reset();
    module.startProcedure(methodD.methodNameId.name);

    for (Formal p : methodD.formalParamList.getList())
      module.addParameter(p.paramId.name);
      
    for (VarDecl v : methodD.localVarList.getList())
      module.addLocalVar(v.varId.name);

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
    Label trueL = NamePool.labelName("if_true");
    Label falseL = NamePool.labelName("if_false");
    Label next = NamePool.labelName("if_next");

    evalBooleanJump(ifStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);
    ifStmt.trueStmt.accept(this);
    module.addInstruction(new Jump(next));

    module.addInstruction(falseL);
    ifStmt.falseStmt.accept(this);

    module.addInstruction(next);
  }

  public void visit(While whileStmt) {
    Label loop = NamePool.labelName("loop");
    Label trueL = NamePool.labelName("while_true");
    Label falseL = NamePool.labelName("while_false");

    module.addInstruction(loop);
    evalBooleanJump(whileStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);
    whileStmt.stmt.accept(this);
    module.addInstruction(new Jump(loop));

    module.addInstruction(falseL);
  }

  public void visit(Print printStmt) {
    printStmt.intExpr.accept(this);
    module.addInstruction(new PrintInstruction(lastTemp));
  }

  public void visit(Assign assignStmt) {
    assignStmt.valueExpr.accept(this);

    Variable destiny = new NormalVar(assignStmt.varId.name);
    module.addInstruction(new Copy(destiny, lastTemp));
  }

  public void visit(ArrayAssign arrayAssign) {
    Variable arrayVar = new NormalVar(arrayAssign.arrayId.name);

    arrayAssign.indexExpr.accept(this);
    Variable indexVar = lastTemp;

    arrayAssign.valueExpr.accept(this);
    Variable valueVar = lastTemp;

    ArrayCellVar dest = new ArrayCellVar(arrayVar, indexVar);
    module.addInstruction(new Copy(dest, valueVar));
  }

  public void visit(And andExpr) {
    Variable temp = NamePool.tempName("and"), a, b;

    andExpr.e1.accept(this);
    a = lastTemp;

    andExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.AND, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(LessThan lessExpr) {
    Variable temp = NamePool.tempName("less_than"), a, b;

    lessExpr.e1.accept(this);
    a = lastTemp;

    lessExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.IS_LESS, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(Plus plusExpr) {
    Variable temp = NamePool.tempName("add"), a, b;

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
    Variable temp = NamePool.tempName("sub"), a, b;

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
    Variable temp = NamePool.tempName("mult"), a, b;

    timesExpr.e1.accept(this);
    a = lastTemp;

    timesExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.MULT, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(ArrayLookup lookup) {
    Variable temp = NamePool.tempName("array_lookup");

    lookup.indexExpr.accept(this);
    Variable index = lastTemp;

    lookup.arrayExpr.accept(this);
    Variable array = lastTemp;

    ArrayCellVar cell = new ArrayCellVar(array, index);

    module.addInstruction(new Copy(temp, cell));
    lastTemp = temp;
  }

  public void visit(ArrayLength length) {
    Variable temp = NamePool.tempName("array_length");

    length.arrayExpr.accept(this);
    Variable arrayVar = lastTemp;

    module.addInstruction(new Operation(
      Opcode.ARRAY_LENGTH, temp, arrayVar
    ));

    lastTemp = temp;
  }

  public void visit(Call callStmt) {
    Variable temp = NamePool.tempName("call");

    callStmt.objectExpr.accept(this);
    Variable objectRef = lastTemp;

    module.addInstruction(new Action(Opcode.SAVE_CTX));
    module.addInstruction(new ParameterSetup(objectRef));

    List<Exp> params = callStmt.paramExprList.getList();
    for (int i = params.size()-1; i >= 0; --i) {
      params.get(i).accept(this);
      module.addInstruction(new ParameterSetup(lastTemp));
    }

    IdentifierType objType = (IdentifierType)callStmt.objectExpr.getType();
    String objClassName = objType.className;
    String methodName = callStmt.methodId.name;
    String classN = symbolTable.getMethodClass(objClassName, methodName).getName();

    Label procLabel = new Label(classN + "%" + methodName);
    
    module.addInstruction(new ProcedureCall(temp, procLabel));
    module.addInstruction(new Action(Opcode.LOAD_CTX));

    lastTemp = temp;
  }

  public void visit(IntegerLiteral intLiteral) {
    lastTemp = new ConstantVar(intLiteral.value);
  }

  public void visit(True t) {
    lastTemp = new ConstantVar(1);
  }

  public void visit(False f) {
    lastTemp = new ConstantVar(0);
  }

  public void visit(This thisExp) {
    lastTemp = new NormalVar("this");
  }

  public void visit(NewArray newArray) {
    Variable temp = NamePool.tempName("new_array");
    
    newArray.sizeExpr.accept(this);
    Variable size = lastTemp;

    module.addInstruction(new Operation(
     Opcode.NEW_ARRAY, temp, size
    ));

    lastTemp = temp;
  }

  public void visit(NewObject newObj) {
    Variable temp = NamePool.tempName("new_" + newObj.classNameId);

    module.addInstruction(new Operation(
      Opcode.NEW_OBJECT, temp, new NormalVar(newObj.classNameId.name)
    ));
            
    lastTemp = temp;
  }

  public void visit(Not notExp) {
    Variable temp = NamePool.tempName("not");

    notExp.boolExpr.accept(this);

    module.addInstruction(new Operation(
      Opcode.NOT, temp, lastTemp
    ));

    lastTemp = temp;
  }

  public void visit(Identifier varId) {
    lastTemp = new NormalVar(varId.name);
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

      Label cont = NamePool.labelName("and_cont");
      evalBooleanJump(andExpr.e1, cont, falseL);

      module.addInstruction(cont);
      evalBooleanJump(andExpr.e2, trueL, falseL);
    }
    else if (e instanceof LessThan) {
      LessThan lessExpr = (LessThan)e;

      lessExpr.e1.accept(this);
      Variable a = lastTemp;

      lessExpr.e2.accept(this);
      Variable b = lastTemp;

      module.addInstruction(new ConditionalJump(
        Condition.LESS_THAN, a, b, trueL
      ));

      module.addInstruction(new Jump(falseL));
    }
    else if (e instanceof Identifier) {
      Variable boolVar = new NormalVar(((Identifier)e).name);

      module.addInstruction(new ConditionalJump(
        Condition.IS_TRUE, boolVar, null, trueL
      ));

      module.addInstruction(new Jump(falseL));
    }
    else if (e instanceof Call) {
      e.accept(this);
      Variable returnVar = lastTemp;
      
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
