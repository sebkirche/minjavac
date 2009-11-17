package analysis.tac;

import java.util.List;
import analysis.tac.instructions.*;
import java.util.ArrayList;

public class TABasicBlock {
  private List<Label> _labels;
  private List<TAInstruction> _instructions;

  public TABasicBlock() {
    _labels = new ArrayList<Label>(7);
    _instructions = new ArrayList<TAInstruction>(20);
  }

  public List<Label> labels() {
    return _labels;
  }

  public List<TAInstruction> instructions() {
    return _instructions;
  }

  @Override
  public String toString() {
    String str = "";

    for (Label l : labels())
      str += " " + l + ":\n";

    for (TAInstruction i : instructions())
      str += "    " + i + ";\n";

    str = str.trim();

    if (!labels().isEmpty())
      str = " " + str;
    else
      str = "    " + str;

    return str;
  }
}
