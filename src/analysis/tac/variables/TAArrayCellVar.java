package analysis.tac.variables;

public class TAArrayCellVar extends TAVariable {
  private TAVariable array, index;

  public TAArrayCellVar(TAVariable a, TAVariable i) {
    array = a;
    index = i;
  }

  public TAVariable getArrayVar() {
    return array;
  }

  public TAVariable getIndexVar() {
    return index;
  }

  @Override
  public String toString() {
    return array + "[" + index + "]";
  }
}
