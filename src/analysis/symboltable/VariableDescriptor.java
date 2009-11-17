package analysis.symboltable;

import analysis.syntaxtree.Type;

public class VariableDescriptor {
  String name;
  Type type;
  int offset;

  public VariableDescriptor(String varName, Type varType) {
    name = varName;
    type = varType;
  }

  public String name() {
    return name;
  }

  public Type type() {
    return type;
  }

  public void setOffset(int n) {
    offset = n;
  }

  public int getOffset() {
    return offset;
  }
}
