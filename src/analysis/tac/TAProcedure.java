package analysis.tac;

import java.util.List;
import java.util.ArrayList;
import analysis.tac.instructions.Label;
import analysis.tac.instructions.TAInstruction;

public class TAProcedure {
  private Label label;
  private List<TABasicBlock> code;

  public TAProcedure(String _label) {
    label = new Label(_label);
    code = new ArrayList<TABasicBlock>(20);
  }

  public Label getLabel() {
    return label;
  }

  public String getName() {
    return label.toString().split("%")[1].trim();
  }

  public List<TABasicBlock> getCode() {
    return code;
  }

  @Override
  public String toString() {
    String str = "procedure " + label;

    boolean f = true;
    for (TABasicBlock b : code) {
      if (f) { f = false; str += "\n"; }
      else { str += "\n\n"; }
      str += b;
    }

    str += "\nend";
    return str;
  }

  public void setCode(List<TAInstruction> instr) {
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
}
