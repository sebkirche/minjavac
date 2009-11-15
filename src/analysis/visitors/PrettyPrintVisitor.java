package analysis.visitors;

import analysis.syntaxtree.*;
import analysis.visitors.Visitor;

public class PrettyPrintVisitor implements Visitor {

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.mainC.accept(this);
    for (int i = 0; i < n.classList.size(); i++) {
      System.out.println();
      n.classList.elementAt(i).accept(this);
    }
  }

  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    System.out.print("class ");
    n.classNameId.accept(this);
    System.out.println(" {");
    System.out.print("  public static void main (String [] ");
    n.argId.accept(this);
    System.out.println(") {");
    System.out.print("    ");
    n.mainStmt.accept(this);
    System.out.println("  }");
    System.out.println("}");
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    System.out.print("class ");
    n.classId.accept(this);
    System.out.println(" { ");
    for (int i = 0; i < n.fieldVarList.size(); i++) {
      System.out.print("  ");
      n.fieldVarList.elementAt(i).accept(this);
      if (i + 1 < n.fieldVarList.size()) {
        System.out.println();
      }
    }
    for (int i = 0; i < n.methodList.size(); i++) {
      System.out.println();
      n.methodList.elementAt(i).accept(this);
    }
    System.out.println();
    System.out.println("}");
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    System.out.print("class ");
    n.classId.accept(this);
    System.out.println(" extends ");
    n.baseClassId.accept(this);
    System.out.println(" { ");
    for (int i = 0; i < n.fieldVarList.size(); i++) {
      System.out.print("  ");
      n.fieldVarList.elementAt(i).accept(this);
      if (i + 1 < n.fieldVarList.size()) {
        System.out.println();
      }
    }
    for (int i = 0; i < n.methodList.size(); i++) {
      System.out.println();
      n.methodList.elementAt(i).accept(this);
    }
    System.out.println();
    System.out.println("}");
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.varType.accept(this);
    System.out.print(" ");
    n.varId.accept(this);
    System.out.print(";");
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    System.out.print("  public ");
    n.methodReturnT.accept(this);
    System.out.print(" ");
    n.methodNameId.accept(this);
    System.out.print(" (");
    for (int i = 0; i < n.formalParamList.size(); i++) {
      n.formalParamList.elementAt(i).accept(this);
      if (i + 1 < n.formalParamList.size()) {
        System.out.print(", ");
      }
    }
    System.out.println(") { ");
    for (int i = 0; i < n.localVarList.size(); i++) {
      System.out.print("    ");
      n.localVarList.elementAt(i).accept(this);
      System.out.println("");
    }
    for (int i = 0; i < n.statementList.size(); i++) {
      System.out.print("    ");
      n.statementList.elementAt(i).accept(this);
      if (i < n.statementList.size()) {
        System.out.println("");
      }
    }
    System.out.print("    return ");
    n.returnExpr.accept(this);
    System.out.println(";");
    System.out.print("  }");
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    n.paramType.accept(this);
    System.out.print(" ");
    n.paramId.accept(this);
  }

  public void visit(IntArrayType n) {
    System.out.print("int []");
  }

  public void visit(BooleanType n) {
    System.out.print("boolean");
  }

  public void visit(IntegerType n) {
    System.out.print("int");
  }

  // String s;
  public void visit(IdentifierType n) {
    System.out.print(n.className);
  }

  // StatementList sl;
  public void visit(Block n) {
    System.out.println("{ ");
    for (int i = 0; i < n.stmtList.size(); i++) {
      System.out.print("      ");
      n.stmtList.elementAt(i).accept(this);
      System.out.println();
    }
    System.out.print("    } ");
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    System.out.print("if (");
    n.boolExpr.accept(this);
    System.out.println(") ");
    System.out.print("    ");
    n.trueStmt.accept(this);
    System.out.println();
    System.out.print("    else ");
    n.falseStmt.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    System.out.print("while (");
    n.boolExpr.accept(this);
    System.out.print(") ");
    n.stmt.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
    System.out.print("System.out.println(");
    n.intExpr.accept(this);
    System.out.print(");");
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.varId.accept(this);
    System.out.print(" = ");
    n.valueExpr.accept(this);
    System.out.print(";");
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.arrayId.accept(this);
    System.out.print("[");
    n.indexExpr.accept(this);
    System.out.print("] = ");
    n.valueExpr.accept(this);
    System.out.print(";");
  }

  // Exp e1,e2;
  public void visit(And n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" && ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" < ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" + ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" - ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Times n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" * ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.arrayExpr.accept(this);
    System.out.print("[");
    n.indexExpr.accept(this);
    System.out.print("]");
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.arrayExpr.accept(this);
    System.out.print(".length");
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.objectExpr.accept(this);
    System.out.print(".");
    n.methodId.accept(this);
    System.out.print("(");
    for (int i = 0; i < n.paramExprList.size(); i++) {
      n.paramExprList.elementAt(i).accept(this);
      if (i + 1 < n.paramExprList.size()) {
        System.out.print(", ");
      }
    }
    System.out.print(")");
  }

  // int i;
  public void visit(IntegerLiteral n) {
    System.out.print(n.value);
  }

  public void visit(True n) {
    System.out.print("true");
  }

  public void visit(False n) {
    System.out.print("false");
  }

  public void visit(This n) {
    System.out.print("this");
  }

  // Exp e;
  public void visit(NewArray n) {
    System.out.print("new int [");
    n.sizeExpr.accept(this);
    System.out.print("]");
  }

  // Identifier i;
  public void visit(NewObject n) {
    System.out.print("new ");
    System.out.print(n.classNameId.name);
    System.out.print("()");
  }

  // Exp e;
  public void visit(Not n) {
    System.out.print("!");
    n.boolExpr.accept(this);
  }

  // String s;
  public void visit(Identifier n) {
    System.out.print(n.name);
  }
}
