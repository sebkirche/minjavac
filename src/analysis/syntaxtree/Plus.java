package analysis.syntaxtree;import analysis.visitors.TypeVisitor;import analysis.visitors.Visitor;public class Plus implements Exp {  public Exp e1, e2;  public Plus(Exp ae1, Exp ae2) {    e1 = ae1;    e2 = ae2;  }  public void accept(Visitor v) {    v.visit(this);  }  public Type accept(TypeVisitor v) {    return v.visit(this);  }}