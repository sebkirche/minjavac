package analysis.tac.instructions;

import java.util.Set;
import java.util.HashSet;

public abstract class TAInstruction {
  public TAInstruction() {
    deadVars = new HashSet<String>(20);
  }

  public boolean isLabel() {
    return this instanceof Label;
  }

  public boolean isJump() {
    return this instanceof Jump;
  }

  public boolean isGoto() {
    return isJump() && !isConditionalJump();
  }

  public boolean isConditionalJump() {
    return this instanceof ConditionalJump;
  }

  public void addDeadVar(String var) {
    deadVars.add(var);
  }

  public Set<String> deadVars() {
    return deadVars;
  }

  private Set<String> deadVars;
}
