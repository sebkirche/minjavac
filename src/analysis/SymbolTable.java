package analysis;

import analysis.syntaxtree.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

class SymbolTable {
  private Map<String,Class> classMap;

  public SymbolTable() {
    classMap = new Hashtable<String,Class>(40);
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

  public boolean containsClass(String id) {
    return classMap.containsKey(id);
  }

  public Type getVarType(Method m, Class c, String varId) {
    if (m != null) {
      if (m.getVar(varId) != null)
        return m.getVar(varId).type();
      else if (m.getParam(varId) != null)
        return m.getParam(varId).type();
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

  public Method getMethod(String id, String classScope) {
    if (getClass(classScope) == null) {
      System.out.println("Class " + classScope + " not defined");
      System.exit(0);
    }

    Class c = getClass(classScope);
    while (c != null) {
      if (c.getMethod(id) != null) {
        return c.getMethod(id);
      } else {
        if (c.parent() == null) {
          c = null;
        } else {
          c = getClass(c.parent());
        }
      }
    }


    System.out.println("Method " + id + " not defined in class " + classScope);

    System.exit(0);
    return null;
  }

  public Type getMethodType(String id, String classScope) {
    if (getClass(classScope) == null) {
      System.out.println("Class " + classScope + " not defined");
      System.exit(0);
    }

    Class c = getClass(classScope);
    while (c != null) {
      if (c.getMethod(id) != null) {
        return c.getMethod(id).type();
      } else {
        if (c.parent() == null) {
          c = null;
        } else {
          c = getClass(c.parent());
        }
      }
    }

    System.out.println("Method " + id + " not defined in class " + classScope);
    System.exit(0);
    return null;
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

      Class c = getClass(i2.s);
      while (c != null) {
        if (i1.s.equals(c.getId())) {
          return true;
        } else {
          if (c.parent() == null) {
            return false;
          }
          c = getClass(c.parent());
        }
      }
    }
    return false;
  }
}//SymbolTable

class Class {

  String id;
  Hashtable methods;
  Hashtable globals;
  String parent;
  Type type;

  public Class(String id, String p) {
    this.id = id;
    parent = p;
    type = new IdentifierType(id);
    methods = new Hashtable();
    globals = new Hashtable();
  }

  public Class() {
  }

  public String getId() {
    return id;
  }

  public Type type() {
    return type;
  }

  public boolean addMethod(String id, Type type) {
    if (containsMethod(id)) {
      return false;
    } else {
      methods.put(id, new Method(id, type));
      return true;
    }
  }

  public Enumeration getMethods() {
    return methods.keys();
  }

  public Method getMethod(String id) {
    if (containsMethod(id)) {
      return (Method) methods.get(id);
    } else {
      return null;
    }
  }

  public boolean addVar(String id, Type type) {
    if (globals.containsKey(id)) {
      return false;
    } else {
      globals.put(id, new Variable(id, type));
      return true;
    }
  }

  public Variable getVar(String id) {
    if (containsVar(id)) {
      return (Variable) globals.get(id);
    } else {
      return null;
    }
  }

  public boolean containsVar(String id) {
    return globals.containsKey(id);
  }

  public boolean containsMethod(String id) {
    return methods.containsKey(id);
  }

  public String parent() {
    return parent;
  }
} // Class

class Variable {

  String id;
  Type type;

  public Variable(String id, Type type) {
    this.id = id;
    this.type = type;
  }

  public String id() {
    return id;
  }

  public Type type() {
    return type;
  }
} // Variable

class Method {

  String id;
  Type type;
  Vector params;
  Hashtable vars;

  public Method(String id, Type type) {
    this.id = id;
    this.type = type;
    vars = new Hashtable();
    params = new Vector();
  }

  public String getId() {
    return id;
  }

  public Type type() {
    return type;
  }

  public boolean addParam(String id, Type type) {
    if (containsParam(id)) {
      return false;
    } else {
      params.addElement(new Variable(id, type));
      return true;
    }
  }

  public Enumeration getParams() {
    return params.elements();
  }

  public Variable getParamAt(int i) {
    if (i < params.size()) {
      return (Variable) params.elementAt(i);
    } else {
      return null;
    }
  }

  public boolean addVar(String id, Type type) {
    if (vars.containsKey(id)) {
      return false;
    } else {
      vars.put(id, new Variable(id, type));
      return true;
    }
  }

  public boolean containsVar(String id) {
    return vars.containsKey(id);
  }

  public boolean containsParam(String id) {
    for (int i = 0; i < params.size(); i++) {
      if (((Variable) params.elementAt(i)).id.equals(id)) {
        return true;
      }
    }
    return false;
  }

  public Variable getVar(String id) {
    if (containsVar(id)) {
      return (Variable) vars.get(id);
    } else {
      return null;
    }
  }

  public Variable getParam(String id) {

    for (int i = 0; i < params.size(); i++) {
      if (((Variable) params.elementAt(i)).id.equals(id)) {
        return (Variable) (params.elementAt(i));
      }
    }

    return null;
  }
} // Method



