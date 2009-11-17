package analysis.tac;

import analysis.tac.instructions.ConditionalJump;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ListIterator;
import analysis.tac.instructions.Label;
import analysis.tac.instructions.Jump;
import analysis.tac.instructions.TAInstruction;

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
          instructions.remove(p);
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

        Label l = j2.getTarget();
        labelCount.put(l.getLabel(), labelCount.get(l.getLabel())-1);
        instructions.remove(p+1);
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
}
