package analysis.tac;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import analysis.tac.instructions.*;
import analysis.tac.variables.TALocalVar;
import analysis.tac.variables.TAVariable;

public class TAOptimizer {
  public static void peepholeOptimize(List<TAInstruction> instructions) {
    Map<String,Integer> labelCount = new HashMap<String, Integer>(20);

    for (TAInstruction i : instructions) if (i.isJump()) {
      String label = ((Jump)i).getTarget().toString();
      Integer c = labelCount.get(label);
      labelCount.put(label, c == null ? 1 : (c+1));
    }

    ListIterator<TAInstruction> it = instructions.listIterator();

    while (it.hasNext()) {
      int p = it.nextIndex();
      TAInstruction i1 = it.next(), i2 = null, i3 = null;

      if (it.hasNext()) {
        i2 = it.next();
        
        if (it.hasNext()) {
          i3 = it.next();
          it.previous();
        }
        
        it.previous();
      }

      if (i2 == null)
        continue;

      if (i1.isJump() && i2.isLabel()) {
        Jump j = (Jump)i1;
        Label l = (Label)i2;

        if (j.getTarget().equals(l)) {
          it.previous();
          it.remove();
          labelCount.put(l.getLabel(), labelCount.get(l.getLabel())-1);
          continue;
        }
      }

      if (i3 == null)
        continue;

      if (i1.isConditionalJump() && i2.isGoto() && i3.isLabel()) {
        ConditionalJump j1 = (ConditionalJump)i1;
        Jump j2 = (Jump)i2;
        Label l1 = (Label)i3;
        Label l2 = j2.getTarget();

        if (!j1.getTarget().equals(l1))
          continue;

        j1.setInverseCondition();
        j1.setTarget(l2);

        labelCount.put(l1.getLabel(), labelCount.get(l1.getLabel())-1);
        it.remove();
      }
    }

    it = instructions.listIterator();

    while (it.hasNext()) {
      TAInstruction i = it.next();

      if (!i.isLabel()) continue;

      if (labelCount.get(((Label)i).toString()).intValue() == 0) {
        it.remove();
      }
    }
  }

  public static void calculateLiveness(
      List<TABasicBlock> code, List<Integer>[] graph) {
    
    
  }
}

class UsedLocalVarsVisitor implements TABasicBlockVisitor {
  private Set<TALocalVar> readVars, writeVars;

  public  UsedLocalVarsVisitor() {
    readVars = new HashSet<TALocalVar>(30);
    writeVars = new HashSet<TALocalVar>(30);
  }

  public Set<TALocalVar> readVars() {
    return readVars;
  }

  public Set<TALocalVar> writeVars() {
    return writeVars;
  }

  public void visit(TABasicBlock block) {
    for (TAInstruction i : block.instructions())
      i.accept(this);
  }

  public void visit(Copy copy) {
    visitWrite(copy.getDestiny());
    visitRead(copy.getSource());
  }

  public void visit(Jump jump) {
    if (jump.isConditionalJump()) {
      ConditionalJump j = (ConditionalJump)jump;
      visitRead(j.getA());
      if (j.getB() != null) visitRead(j.getB());
    }
  }

  public void visit(Operation op) {
    visitWrite(op.getDestiny());
    visitRead(op.getA());
    if (op.getB() != null) visitRead(op.getB());
  }

  public void visit(ParameterSetup param) {
    visitRead(param.getParameter());
  }

  public void visit(PrintInstruction print) {
    visitRead(print.getVar());
  }

  public void visit(ProcedureCall proc) {
    visitWrite(proc.getDestiny());
  }

  public void visit(Return ret) {
    visitRead(ret.getReturnVariable());
  }

  public void visit(Action action) { }

  public void visit(Label label) { }
  
  private void visitRead(TAVariable v) {
    if (v instanceof TALocalVar)
      readVars.add((TALocalVar)v);
  }

  private void visitWrite(TAVariable v) {
    if (v instanceof TALocalVar)
      writeVars.add((TALocalVar)v);
  }
}
