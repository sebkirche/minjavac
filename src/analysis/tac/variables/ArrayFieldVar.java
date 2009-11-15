package analysis.tac.variables;

public class ArrayFieldVar extends Variable {
  private Variable array, index;

  public ArrayFieldVar(Variable a, Variable i) {
    array = a;
    index = i;
  }

  public Variable getArrayVar() {
    return array;
  }

  public Variable getIndexVar() {
    return index;
  }

  @Override
  public String toString() {
    return array + "[" + index + "]";
  }
}
