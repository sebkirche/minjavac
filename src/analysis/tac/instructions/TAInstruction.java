package analysis.tac.instructions;

import analysis.tac.TABasicBlockVisitor;
import java.util.Set;
import java.util.HashSet;
import analysis.tac.variables.TALocalVar;

public abstract class TAInstruction {
  public TAInstruction() {
    deadVars = new HashSet<TALocalVar>(20);
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

  public abstract void accept(TABasicBlockVisitor v);

  public void addDeadVar(TALocalVar var) {
    deadVars.add(var);
  }

  public Set<TALocalVar> deadVars() {
    return deadVars;
  }

  private Set<TALocalVar> deadVars;
}
