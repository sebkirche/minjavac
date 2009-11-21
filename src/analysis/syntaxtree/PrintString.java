package analysis.syntaxtree;

import analysis.visitors.Visitor;
import analysis.visitors.TypeVisitor;

public class PrintString implements Statement {
  public String str;

  public PrintString(String s) {
    str = s;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
