package analysis.tac.variables;

public class ArrayCellVar extends Variable {
  private Variable array, index;

  public ArrayCellVar(Variable a, Variable i) {
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
