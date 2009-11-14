package analysis.symboltable;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import analysis.syntaxtree.Type;

public class Method {
  String name;
  Type returnType;
  List<Variable> paramList;
  Map<String, Variable> localVarMap;

  public Method(String mName, Type mReturnType) {
    name = mName;
    returnType = mReturnType;
    paramList = new ArrayList<Variable>(20);
    localVarMap = new HashMap<String, Variable>(20);
  }

  public String getName() {
    return name;
  }

  public Type getReturnType() {
    return returnType;
  }

  public boolean addParameter(String paramName, Type paramType) {
    if (constainsParameter(paramName)) {
      return false;
    }

    paramList.add(new Variable(paramName, paramType));
    return true;
  }

  public List<Variable> getParameters() {
    return paramList;
  }

  public Variable getParameterAt(int i) {
    if (i < paramList.size()) {
      return paramList.get(i);
    } else {
      return null;
    }
  }

  public boolean addLocalVar(String varName, Type varType) {
    if (localVarMap.containsKey(varName)) {
      return false;
    }

    localVarMap.put(varName, new Variable(varName, varType));
    return true;
  }

  public boolean containsVar(String varName) {
    return localVarMap.containsKey(varName);
  }

  public boolean constainsParameter(String pName) {
    for (Variable v : paramList) {
      if (v.name().equals(pName)) {
        return true;
      }
    }

    return false;
  }

  public Variable getLocalVar(String varName) {
    return localVarMap.get(varName);
  }

  public Variable getParameter(String pName) {
    for (Variable v : paramList) {
      if (v.name().equals(pName)) {
        return v;
      }
    }
    return null;
  }
}
