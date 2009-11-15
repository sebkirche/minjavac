package analysis.tac;

import analysis.syntaxtree.*;
import analysis.tac.variables.*;
import analysis.tac.instructions.*;
import analysis.visitors.Visitor;
import analysis.symboltable.SymbolTable;

public class TAModuleBuilderVisitor implements Visitor {
  private TAModule module;
  private Variable lastTemp;
  private SymbolTable symbolTable;
 
  public TAModuleBuilderVisitor(SymbolTable symT) {
    lastTemp = null;
    symbolTable = symT;
    module = new TAModule();
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

    evalBooleanJump(ifStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);
    ifStmt.trueStmt.accept(this);

    module.addInstruction(falseL);
    ifStmt.falseStmt.accept(this);
  }

  public void visit(While whileStmt) {
    Label trueL = NamePool.labelName("while_true");
    Label falseL = NamePool.labelName("while_false");

    evalBooleanJump(whileStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);
    whileStmt.stmt.accept(this);

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
    Variable temp = NamePool.tempName("add"), a, b;

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
    Variable temp = NamePool.tempName("add"), a, b;

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

  private void evalBooleanJump(Exp e, Label trueL, Label falseL) {
    if (e instanceof False) {
      module.addInstruction(new Jump(falseL));
    } else if (e instanceof True) {
      module.addInstruction(new Jump(trueL));
    } else if (e instanceof Not) {
      evalBooleanJump(((Not)e).boolExpr, falseL, trueL);
    } else if (e instanceof And) {
      And andExpr = (And)e;

      Label cont = NamePool.labelName("and_cont");
      evalBooleanJump(andExpr.e1, cont, falseL);

      module.addInstruction(cont);
      evalBooleanJump(andExpr.e2, trueL, falseL);
    } else if (e instanceof LessThan) {
      LessThan lessExpr = (LessThan)e;

      lessExpr.e1.accept(this);
      Variable a = lastTemp;

      lessExpr.e2.accept(this);
      Variable b = lastTemp;

      module.addInstruction(new ConditionalJump(
        Condition.LESS_THAN, a, b, trueL
      ));

      module.addInstruction(new Jump(falseL));
    } else {
      e.accept(this);
    }
  }
}
