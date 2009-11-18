package analysis.symboltable;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import analysis.syntaxtree.Type;

public class MethodDescriptor {
  String name;
  Type returnType;
  ClassDescriptor classDescr;
  List<VariableDescriptor> paramList;
  Map<String, VariableDescriptor> localVarMap;

  public MethodDescriptor(String n, Type retType, ClassDescriptor classD) {
    name = n;
    returnType = retType;
    classDescr = classD;
    paramList = new ArrayList<VariableDescriptor>(20);
    localVarMap = new HashMap<String, VariableDescriptor>(20);
  }

  public String getName() {
    return name;
  }

  public String getLabel() {
    return classDescr.getName() + "#" + getName();
  }

  public Type getReturnType() {
    return returnType;
  }

  public ClassDescriptor getClassDescriptor() {
    return classDescr;
  }

  public boolean addParameter(String paramName, Type paramType) {
    if (containsParameter(paramName)) {
      return false;
    }

    paramList.add(new VariableDescriptor(paramName, paramType));
    return true;
  }

  public List<VariableDescriptor> getParameters() {
    return paramList;
  }

  public Collection<VariableDescriptor> getLocalVars() {
    return localVarMap.values();
  }

  public VariableDescriptor getParameterAt(int i) {
    if (i < paramList.size()) {
      return paramList.get(i);
    } else {
      return null;
    }
  }

  public boolean addLocalVar(String varName, Type varType) {
    if (containsVar(varName) || containsParameter(varName))
      return false;

    localVarMap.put(varName, new VariableDescriptor(varName, varType));
    return true;
  }

  public boolean containsVar(String varName) {
    return localVarMap.containsKey(varName);
  }

  public boolean containsParameter(String pName) {
    for (VariableDescriptor v : paramList) {
      if (v.name().equals(pName)) {
        return true;
      }
    }

    return false;
  }

  public VariableDescriptor getLocalVar(String varName) {
    return localVarMap.get(varName);
  }

  public VariableDescriptor getParameter(String pName) {
    for (VariableDescriptor v : paramList) {
      if (v.name().equals(pName)) {
        return v;
      }
    }
    return null;
  }
}
