package analysis.tac.variables;

public abstract class TAVariable {
  public boolean isLocalVar() {
    return this instanceof TALocalVar;
  }

  public boolean isConstant() {
    return this instanceof TAConstantVar;
  }
}
