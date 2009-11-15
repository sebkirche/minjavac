package analysis.symboltable;

import java.util.Map;
import java.util.HashMap;
import analysis.syntaxtree.Type;
import analysis.syntaxtree.IdentifierType;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Class {
  Type classType;
  String name, baseClass;
  Map<String,Method> methodMap;
  Map<String,Variable> variableMap;
  
  public Class(String id, String p) {
    name = id;
    baseClass = p;

    classType = new IdentifierType(id);
    methodMap = new HashMap<String, Method>(20);
    variableMap =  new HashMap<String, Variable>(20);
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return classType;
  }

  public boolean addMethod(String mName, Type mType) {
    if (containsMethod(mName))
      return false;

    methodMap.put(mName, new Method(mName, mType));
    return true;
  }

  public Set<String> getMethods() {
    return methodMap.keySet();
  }

  public Method getMethod(String mName) {
    if (containsMethod(mName))
      return methodMap.get(mName);

    return null;
  }

  public boolean addVar(String varName, Type varType) {
    if (variableMap.containsKey(varName))
      return false;

    variableMap.put(varName, new Variable(varName, varType));
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

  public Variable getVar(String varName) {
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
