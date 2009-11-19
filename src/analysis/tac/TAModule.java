package analysis.tac;

import analysis.tac.optimizer.TAOptimizer;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import analysis.syntaxtree.Type;
import analysis.symboltable.SymbolTable;
import analysis.symboltable.MethodDescriptor;
import analysis.tac.instructions.TAInstruction;

public class TAModule {
  private TAClass openClass;
  private List<TAClass> classes;
  private TAProcedure openProcedure;
  private List<TAInstruction> instructions;

  public TAModule() {
    classes = new ArrayList<TAClass>(5);
    openClass = null;
    openProcedure = null;
  }

  public TAClass getOpenClass() {
    return openClass;
  }

  public TAProcedure getOpenProcedure() {
    return openProcedure;
  }

  public void startClass(String name) {
    openClass = new TAClass(name); 
  }

  public void startProcedure(String procName) {
    String procLabel = openClass.getName() + "#" + procName;
    openProcedure = new TAProcedure(procLabel);
    instructions = new LinkedList<TAInstruction>();
  }

  public void addTemporaryVar(String name, Type type) {
    String classN = openClass.getName();
    String methodN = openProcedure.getName();
    SymbolTable symT = SymbolTable.getInstance();
    MethodDescriptor methodD = symT.getMethod(methodN, classN);
    methodD.addLocalVar(name, type);
  }

  public void addInstruction(TAInstruction i) {
    instructions.add(i);
  }

  public void closeProcedure() {
    TAOptimizer.peepholeOptimization(instructions);
    openProcedure.setCode(instructions);
    openClass.getProcedures().add(openProcedure);
    instructions = null;
    openProcedure = null;
  }

  public void closeClass() {
    classes.add(openClass);
    openClass = null;
  }

  public TAClass getClass(String className) {
    for (TAClass c : classes)
      if (c.getName().equals(className))
        return c;

    return null;
  }

  public TAProcedure getProcedure(String label) {
    for (TAClass c : classes) {
      TAProcedure proc = c.getProcedure(label);
      if (proc != null)
        return proc;
    }

    return null;
  }

  @Override
  public String toString() {
    String str = "";

    for (TAClass c : classes)
      str += "\n\n" + c;

    return str.trim();
  }

  private static TAModule instance;

  public static void setInstance(TAModule module) {
    instance = module;
  }

  public static TAModule getInstance() {
    return instance;
  }
}
