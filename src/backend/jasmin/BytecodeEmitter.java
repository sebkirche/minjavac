package backend.jasmin;

import analysis.syntaxtree.*;
import analysis.visitors.Visitor;

public class BytecodeEmitter implements Visitor {
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ClassDeclSimple n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ClassDeclExtends n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(VarDecl n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(MethodDecl n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Formal n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(IntArrayType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(BooleanType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(IntegerType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(VoidType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(IdentifierType n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Block n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(If n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(While n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(For n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Print n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(PrintString n) {
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

  public void visit(Or n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Equal n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(NotEqual n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(Greater n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(GreaterOrEqual n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(LessOrEqual n) {
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

  public void visit(Div n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ArrayLookup n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(ArrayLength n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(PrefixAdd n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(PrefixSub n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(PostfixAdd n) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void visit(PostfixSub n) {
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
