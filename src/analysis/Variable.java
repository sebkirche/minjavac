package analysis;

import analysis.syntaxtree.Type;

class Variable {
  String name;
  Type type;

  public Variable(String varName, Type varType) {
    name = varName;
    type = varType;
  }

  public String name() {
    return name;
  }

  public Type type() {
    return type;
  }
}
