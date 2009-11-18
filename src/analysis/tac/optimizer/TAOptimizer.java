package analysis.tac.optimizer;

import java.util.*;
import analysis.tac.*;
import analysis.tac.variables.*;
import analysis.tac.instructions.*;

public class TAOptimizer {
  public static void peepholeOptimization(List<TAInstruction> instructions) {
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

  public static void livenessCalculation(
      List<TABasicBlock> code, List<Integer>[] graph) {

    TABasicBlockVisitor usedVars = new UsedLocalVarsVisitor();
    TABasicBlockVisitor deadVars = new DeadVarsVisitor();

    for (TABasicBlock block : code)
      block.accept(usedVars);

    boolean[] visited = new boolean[code.size()];

    for (int i = 0; i < code.size(); ++i) {
      TABasicBlock block = code.get(i);
      
      Set<TALocalVar> candidates = new HashSet<TALocalVar>(block.writeVars());
      Set<TALocalVar> liveVars = block.liveVars();

      for (int j = 0; j < code.size(); ++j) visited[j] = false;

      for (Integer j : graph[i])
        livenessCalcRec(j, visited, code, graph, candidates, liveVars);
    }

    for (TABasicBlock block : code)
      block.accept(deadVars);
  }

  /*
   * dfs no flow graph do procedimento a partir do bloco i
   */
  private static void livenessCalcRec(
          int i, boolean[] visited,
          List<TABasicBlock> code, List<Integer>[] graph,
          Set<TALocalVar> candidates, Set<TALocalVar> liveVars) {
    
    TABasicBlock block = code.get(i);

    if (visited[i])
      return;

    visited[i] = true;

    Set<TALocalVar> set = new HashSet<TALocalVar>(candidates);
    set.retainAll(block.firstReadVars());

    // set tem todos os candidatos que tem firstRead em block
    liveVars.addAll(set);
    candidates.removeAll(set);

    // variáveis que sao lidas, mas nao no inicio, nao podem ser vivas
    candidates.removeAll(block.readVars());

    if (candidates.isEmpty())
      return;

    for (Integer j : graph[i])
      livenessCalcRec(j, visited, code, graph, candidates, liveVars);
  }
}
