package analysis.symboltable;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import analysis.syntaxtree.Type;
import analysis.syntaxtree.IdentifierType;

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

  public List<VariableDescriptor> getVariableDescriptors() {
    List<String> sl = getVariables();
    List<VariableDescriptor> l = new ArrayList<VariableDescriptor>(sl.size());

    for (String name : sl)
      l.add(getVarInScope(name));

    return l;
  }

  public VariableDescriptor getVar(String varName) {
    if (containsVar(varName))
      return variableMap.get(varName);

    return null;
  }

  public VariableDescriptor getVarInScope(String name) {
    if (containsVar(name))
      return getVar(name);
    else
      return SymbolTable.getInstance().getClass(parent()).getVarInScope(name);
  }

  public boolean containsVar(String varName) {
    return variableMap.containsKey(varName);
  }

  public boolean isInScope(String varName) {
    ClassDescriptor c = this;

    while (c != null) {
      if (c.getVar(varName) != null)
        return true;
      else if (c.parent() == null)
        break;

      c = SymbolTable.getInstance().getClass(c.parent());
    }

    return false;
  }

  public boolean containsMethod(String methodName) {
    return methodMap.containsKey(methodName);
  }

  public String parent() {
    return baseClass;
  }

  public List<String> getAccessibleMethodLabels() {
    List<String> ml = new ArrayList<String>(10);

    if (baseClass != null) {
      ClassDescriptor base = SymbolTable.getInstance().getClass(baseClass);
      ml.addAll(base.getAccessibleMethodLabels());
    }

    for (String m1 : getMethods()) {
      boolean done = false;
      String m1_label = getName() + "::" + m1;

      for (int i = 0; i < ml.size() && !done; ++i) {
        String m2 = ml.get(i);

        if (m1.equals(Util.getDefinedName(m2))) {
          ml.set(i, m1_label);
          done = true;
        }
      }

      if (!done) {
        ml.add(m1_label);
      }
    }

    return ml;
  }
}
