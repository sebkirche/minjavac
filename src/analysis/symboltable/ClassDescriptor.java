package analysis.symboltable;

import java.util.Map;
import java.util.HashMap;
import analysis.syntaxtree.Type;
import analysis.syntaxtree.IdentifierType;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ClassDescriptor {
  int size;
  Type classType;
  String name, baseClass;
  Map<String,MethodDescriptor> methodMap;
  Map<String,VariableDescriptor> variableMap;
  
  public ClassDescriptor(String id, String p) {
    name = id;
    baseClass = p;

    classType = new IdentifierType(id);
    methodMap = new HashMap<String, MethodDescriptor>(20);
    variableMap =  new HashMap<String, VariableDescriptor>(20);
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return classType;
  }

  public void setSize(int sz) {
    size = sz;
  }

  public int getSize() {
    return size;
  }

  public boolean addMethod(String mName, Type mType) {
    if (containsMethod(mName))
      return false;

    methodMap.put(mName, new MethodDescriptor(mName, mType, this));
    return true;
  }

  public Set<String> getMethods() {
    return methodMap.keySet();
  }

  public MethodDescriptor getMethod(String mName) {
    if (containsMethod(mName))
      return methodMap.get(mName);

    return null;
  }

  public boolean addVar(String varName, Type varType) {
    if (variableMap.containsKey(varName))
      return false;

    variableMap.put(varName, new VariableDescriptor(varName, varType));
    return true;
  }

  public List<String> getVariables() {
    SymbolTable symT = SymbolTable.getInstance();
    LinkedList<String> vars = new LinkedList<String>();

    if (baseClass != null) {
      vars.addAll(symT.getClass(baseClass).getVariables());
    }

    vars.addAll(variableMap.keySet());
    return vars;
  }

  public VariableDescriptor getVar(String varName) {
    if (containsVar(varName))
      return variableMap.get(varName);

    return null;
  }

  public boolean containsVar(String varName) {
    return variableMap.containsKey(varName);
  }

  public boolean containsMethod(String methodName) {
    return methodMap.containsKey(methodName);
  }

  public String parent() {
    return baseClass;
  }
}
