package analysis.tac;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import analysis.tac.instructions.*;
import analysis.tac.variables.TALocalVar;
import java.util.HashSet;

public class TABasicBlock {
  private List<Label> labels;
  private List<TAInstruction> instructions;
  private Set<TALocalVar> writeVars, readVars, firstReadVars, liveVars;

  public TABasicBlock() {
    labels = new ArrayList<Label>(7);
    instructions = new ArrayList<TAInstruction>(20);
    writeVars = new HashSet<TALocalVar>(10);
    readVars = new HashSet<TALocalVar>(10);
    firstReadVars = new HashSet<TALocalVar>(10);
    liveVars = new HashSet<TALocalVar>(10);
  }

  public List<Label> labels() {
    return labels;
  }

  public List<TAInstruction> instructions() {
    return instructions;
  }

  /*
   * Set of variables whose values are renewed
   * in this block
   */
  public Set<TALocalVar> writeVars() {
    return writeVars;
  }

  /*
   * Set of variables whose values are read
   * in this block
   */
  public Set<TALocalVar> readVars() {
    return readVars;
  }

  /*
   * Set of variables whose first appearance
   * in this block is a read
   */
  public Set<TALocalVar> firstReadVars() {
    return firstReadVars;
  }

  /*
   * Set of variables that are live
   * at the end of this basic block
   */
  public Set<TALocalVar> liveVars() {
    return liveVars;
  }

  public TAInstruction lastInstruction() {
    if (instructions.isEmpty())
      return null;

    return instructions.get(instructions.size()-1);
  }

  @Override
  public String toString() {
    String str = "";
    String pad = "   ";
    String cpad = pad + "# ";

    str += cpad + "write     : " + writeVars + "\n";
    str += cpad + "read      : " + readVars + "\n";
    str += cpad + "firstRead : " + firstReadVars + "\n";
    str += cpad + "live      : " + liveVars + "\n";

    for (Label l : labels())
      str += " " + l + ":\n";

    for (TAInstruction i : instructions())
      str += pad + i + ";\n";

    str = pad + str.trim();
    return str;
  }

  public void accept(TABasicBlockVisitor v) {
    v.visit(this);
  }
}
