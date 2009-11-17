package analysis.tac;

import java.util.List;
import java.util.ArrayList;
import analysis.tac.instructions.*;

public class TABasicBlock {
  private List<Label> labels;
  private List<TAInstruction> instructions;

  public TABasicBlock() {
    labels = new ArrayList<Label>(7);
    instructions = new ArrayList<TAInstruction>(20);
  }

  public List<Label> labels() {
    return labels;
  }

  public List<TAInstruction> instructions() {
    return instructions;
  }

  public TAInstruction lastInstruction() {
    if (instructions.isEmpty())
      return null;

    return instructions.get(instructions.size()-1);
  }

  @Override
  public String toString() {
    String str = "";

    for (Label l : labels())
      str += " " + l + ":\n";

    for (TAInstruction i : instructions())
      str += "   " + i + ";\n";

    str = str.trim();

    if (!labels().isEmpty())
      str = " " + str;
    else
      str = "   " + str;

    return str;
  }
}
