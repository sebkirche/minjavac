package analysis.tac;

import java.util.List;
import analysis.tac.instructions.*;
import java.util.ArrayList;

public class TABasicBlock {
  private List<Label> _labels;
  private List<Instruction> _instructions;

  public TABasicBlock() {
    _labels = new ArrayList<Label>(7);
    _instructions = new ArrayList<Instruction>(20);
  }

  public List<Label> labels() {
    return _labels;
  }

  public List<Instruction> instructions() {
    return _instructions;
  }

  @Override
  public String toString() {
    String str = "";

    for (Label l : labels())
      str += l;

    for (Instruction i : instructions())
      str += "\t" + i + "\n";

    str = str.trim();

    if (labels().isEmpty())
      str = "\t" + str;

    return str;
  }
}
