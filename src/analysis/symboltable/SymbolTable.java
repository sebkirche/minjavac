package analysis.symboltable;

import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import analysis.syntaxtree.*;

public class SymbolTable {
  private Map<String,Class> classMap;

  public SymbolTable() {
    classMap = new HashMap<String, Class>(20);
  }

  public boolean addClass(String id, String parent) {
    if (containsClass(id))
      return false;

    classMap.put(id, new Class(id, parent));
    return true;
  }

  public Class getClass(String id) {
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

  public Type getVarType(Method m, Class c, String varId) {
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

    System.out.println("Variable " + varId + " not defined in current scope");
    System.exit(0);
    return null;
  }

  public Method getMethod(String methodName, String className) {
    if (getClass(className) == null) {
      System.out.println("Class " + className + " not defined");
      System.exit(0);
    }

    Class c = getClass(className);
    while (c != null) {
      if (c.getMethod(methodName) != null)
        return c.getMethod(methodName);

      if (c.parent() == null)
        break;
      
      c = getClass(c.parent());
    }

    System.out.println("Method " + methodName + " not defined in class " + className);
    System.exit(0);
    return null;
  }

  public Type getMethodType(String methodName, String className) {
    if (getClass(className) == null) {
      System.out.println("Class " + className + " not defined");
      System.exit(0);
    }

    Class c = getClass(className);
    while (c != null) {
      if (c.getMethod(methodName) != null)
        return c.getMethod(methodName).getReturnType();

      if (c.parent() == null)
        break;

      c = getClass(c.parent());
    }

    System.out.println("Method " + methodName + " not defined in class " + className);
    System.exit(0);
    return null;
  }

  public Class getMethodClass(String instanceClass, String methodName) {
    String className = instanceClass;
    Class c = getClass(className);

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

      Class c = getClass(i2.className);
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
