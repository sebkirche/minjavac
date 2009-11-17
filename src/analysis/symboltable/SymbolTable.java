package analysis.symboltable;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import analysis.syntaxtree.*;

public class SymbolTable {
  private Map<String,ClassDescriptor> classMap;

  public SymbolTable() {
    classMap = new HashMap<String, ClassDescriptor>(20);
  }

  public boolean addClass(String id, String parent) {
    if (containsClass(id))
      return false;

    classMap.put(id, new ClassDescriptor(id, parent));
    return true;
  }

  public ClassDescriptor getClass(String id) {
    if (containsClass(id))
      return classMap.get(id);

    return null;
  }

  public Set<String> getClasses() {
    return classMap.keySet();
  }

  public boolean containsClass(String id) {
    return classMap.containsKey(id);
  }

  public Type getVarType(MethodDescriptor m, ClassDescriptor c, String varId) {
    if (m != null) {
      if (m.getLocalVar(varId) != null)
        return m.getLocalVar(varId).type();
      else if (m.getParameter(varId) != null)
        return m.getParameter(varId).type();
    }

    while (c != null) {
      if (c.getVar(varId) != null)
        return c.getVar(varId).type();
      else if (c.parent() == null)
        break;

      c = getClass(c.parent());
    }

    throw new Error("Variable " + varId + " not defined in current scope");
  }

  public MethodDescriptor getMethod(String methodN, String classN) {
    if (getClass(classN) == null)
      throw new Error("Class " + classN + " not defined");

    ClassDescriptor c = getClass(classN);
    while (c != null) {
      if (c.getMethod(methodN) != null)
        return c.getMethod(methodN);

      if (c.parent() == null)
        break;
      
      c = getClass(c.parent());
    }

    throw new Error("Method " + methodN + " not defined in class " + classN);
  }

  public Type getMethodType(String methodN, String classN) {
    if (getClass(classN) == null)
      throw new Error("Class " + classN + " not defined");

    ClassDescriptor c = getClass(classN);
    while (c != null) {
      if (c.getMethod(methodN) != null)
        return c.getMethod(methodN).getReturnType();

      if (c.parent() == null)
        break;

      c = getClass(c.parent());
    }

    throw new Error("Method " + methodN + " not defined in class " + classN);
  }

  public ClassDescriptor getMethodClass(String instanceClass, String methodName) {
    String className = instanceClass;
    ClassDescriptor c = getClass(className);

    while (true) {
      if (c.containsMethod(methodName))
        return c;
      else
        c = getClass(c.baseClass);
    }
  }

  public boolean compareTypes(Type t1, Type t2) {
    if (t1 == null || t2 == null) {
      return false;
    }

    if (t1 instanceof IntegerType && t2 instanceof IntegerType) {
      return true;
    }
    if (t1 instanceof BooleanType && t2 instanceof BooleanType) {
      return true;
    }
    if (t1 instanceof IntArrayType && t2 instanceof IntArrayType) {
      return true;
    }
    if (t1 instanceof IdentifierType && t2 instanceof IdentifierType) {
      IdentifierType i1 = (IdentifierType) t1;
      IdentifierType i2 = (IdentifierType) t2;

      ClassDescriptor c = getClass(i2.className);
      while (c != null) {
        if (i1.className.equals(c.getName()))
          return true;

        if (c.parent() == null)
          return false;

        c = getClass(c.parent());
      }
    }
    return false;
  }

  private static SymbolTable instance = null;

  public static void setInstance(SymbolTable symT) {
    instance = symT;
  }

  public static SymbolTable getInstance() {
    return instance;
  }
}
