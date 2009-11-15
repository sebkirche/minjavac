package backend.nasm;

import java.util.List;
import java.util.LinkedList;
import analysis.symboltable.SymbolTable;
import java.util.ArrayList;

class CodeDescriptor {
  private static SymbolTable symT;
  private List<ClassDescriptor> classes;

  private CodeDescriptor(SymbolTable symT) {
    classes = new LinkedList<ClassDescriptor>();
  }

  public List<ClassDescriptor> getClasses() {
    return classes;
  }

  public static CodeDescriptor getDescriptor(SymbolTable s) {
    symT = s;
    CodeDescriptor descr = new CodeDescriptor(symT);

    for (String className : symT.getClasses()) {
      descr.getClasses().add(getClassDescriptor(className));
    }

    return descr;
  }

  private static ClassDescriptor getClassDescriptor(String name) {
    analysis.symboltable.Class c = symT.getClass(name);
    ClassDescriptor cd = new ClassDescriptor(name);

    int p = 4;
    for (String v : c.getVariables()) {
      FieldDescriptor field = new FieldDescriptor(v, cd, p);
      cd.getFields().add(field);
      p += 4;
    }

    

    cd.setSize(p);
    return cd;
  }

  
}


class ClassDescriptor {
  private int size;
  private String className;
  private List<FieldDescriptor> fields;
  private List<ProcedureDescriptor> procedures;

  public ClassDescriptor(String name) {
    className = name;
    fields = new ArrayList<FieldDescriptor>(20);
    procedures = new ArrayList<ProcedureDescriptor>(10);
  }

  public String getClassName() {
    return className;
  }

  public List<FieldDescriptor> getFields() {
    return fields;
  }

  public List<ProcedureDescriptor> getProcedures() {
    return procedures;
  }

  public void setSize(int s) {
    size = s;
  }

  public int getSize() {
    return size;
  }
}

class ProcedureDescriptor {
  private String label;
  private List<VariableDescriptor> locals; // params \/ locals
  
  public ProcedureDescriptor(String l) {
    label = l;
    locals = new ArrayList<VariableDescriptor>(20);
  }

  public String getLabel() {
    return label;
  }

  public List<VariableDescriptor> getLocals() {
    return locals;
  }
}

class VariableDescriptor {
  private String name;
  private int offset; // &var = ebp + offset;

  public VariableDescriptor(String name, int offset) {
    this.name = name;
    this.offset = offset;
  }

  public String getName() {
    return name;
  }

  public int getOffset() {
    return offset;
  }
}

class FieldDescriptor {
  private int offset; // &field = this + offset
  private String name;
  private ClassDescriptor classD;

  public FieldDescriptor(String s, ClassDescriptor c, int n) {
    name = s;
    classD = c;
    offset = n;
  }

  public String getName() {
    return name;
  }

  public ClassDescriptor getClassD() {
    return classD;
  }

  public int getOffset() {
    return offset;
  }
}
