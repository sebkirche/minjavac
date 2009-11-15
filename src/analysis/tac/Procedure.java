package analysis.tac;

import java.util.List;
import java.util.ArrayList;
import analysis.tac.instructions.Label;

public class Procedure {
  private Label name;
  private List<BasicBlock> code;

  public Procedure(String _name) {
    name = new Label(_name);
    code = new ArrayList<BasicBlock>(20);
  }

  public Label getName() {
    return name;
  }

  public List<BasicBlock> getCode() {
    return code;
  }

  @Override
  public String toString() {
    String str = "procedure " + name + ":";

    for (BasicBlock b : code)
      str += "\n" + b;

    str += "\nend";
    return str;
  }
}
