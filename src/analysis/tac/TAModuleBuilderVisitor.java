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
    Label trueLabel = new Label(NamePool.getLabelName());
    Label falseLabel = new Label(NamePool.getLabelName());

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
