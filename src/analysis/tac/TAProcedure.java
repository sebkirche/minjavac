package analysis.tac;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Array;
import analysis.tac.instructions.Jump;
import analysis.tac.instructions.Label;
import analysis.tac.instructions.TAInstruction;

public class TAProcedure {
  private Label label;
  private List<TABasicBlock> code;
  private List<Integer>[] graph;

  public TAProcedure(String _label) {
    label = new Label(_label);
    code = new ArrayList<TABasicBlock>(20);
  }

  public Label getLabel() {
    return label;
  }

  public String getName() {
    return label.toString().split("::")[1].trim();
  }

  public List<TABasicBlock> getCode() {
    return code;
  }

  @Override
  public String toString() {
    String str = "procedure " + label;

    for (List<Integer> l : graph)
      str += "\n" + l.toString();

    boolean f = true;
    for (TABasicBlock b : code) {
      if (f) { f = false; str += "\n"; }
      else { str += "\n\n"; }
      str += b;
    }

    str += "\nend";
    return str;
  }

  public void setCode(List<TAInstruction> instructions) {
    buildBlocks(instructions);
    buildFlowGraph();
  }

  private void buildBlocks(List<TAInstruction> instr) {
    TABasicBlock block = new TABasicBlock();

    for (TAInstruction i : instr) {
      if (i.isLabel()) {
        if (!block.instructions().isEmpty()) {
          code.add(block);
          block = new TABasicBlock();
        }

        block.labels().add((Label)i);
      } else {
        block.instructions().add(i);

        if (i.isJump()) {
          code.add(block);
          block = new TABasicBlock();
        }
      }
    }

    if (!block.labels().isEmpty() || !block.instructions().isEmpty())
      code.add(block);
  }

  private void buildFlowGraph() {
    graph = (List<Integer>[]) Array.newInstance(List.class, code.size());

    for (int i = 0; i < code.size(); ++i) {
      TABasicBlock b1 = code.get(i);
      graph[i] = new ArrayList<Integer>(3);

      TAInstruction b1Last = b1.lastInstruction();

      if (b1Last == null) continue;

      if (b1Last.isJump()) {
        Label target = ((Jump)b1Last).getTarget();

        for (int j = 0; j < code.size(); ++j) if (j != i) {
          TABasicBlock b2 = code.get(j);
          if (b2.labels().contains(target)) {
            graph[i].add(j);
            break;
          }
        }
      }

      if (!b1Last.isGoto() && i < code.size()-1) {
        graph[i].add(i+1);
      }
    }
  }
}
