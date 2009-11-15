package analysis.tac;

import java.util.List;
import java.util.ArrayList;
import analysis.tac.instructions.Label;
import analysis.tac.instructions.Instruction;
import analysis.tac.variables.NormalVar;

public class TAModule {
  private TAClass openClass;
  private TAProcedure openProcedure;
  private TABasicBlock openBasicBlock;
  private List<TAClass> classes;

  public TAModule() {
    classes = new ArrayList<TAClass>(5);
    openClass = null;
    openProcedure = null;
    openBasicBlock = null;
  }

  public void startClass(String name) {
    openClass = new TAClass(name); 
  }

  public void addStaticVar(String name) {
    openClass.getStaticVars().add(new NormalVar(name));
  }

  public void startProcedure(String name) {
    openProcedure = new TAProcedure(name);
    startBasicBlock();
  }

  public void addParameter(String name) {
    openProcedure.getParameters().add(new NormalVar(name));
  }

  public void addLocalVar(String name) {
    openProcedure.getLocals().add(new NormalVar(name));
  }

  public void addInstruction(Instruction i) {
    if (i.isLabel()) {
      if (!openBasicBlock.instructions().isEmpty())
        startBasicBlock();

      openBasicBlock.labels().add((Label)i);
    }
    else {
      openBasicBlock.instructions().add(i);

      if (i.isJump())
        startBasicBlock();
    }
  }

  public void closeProcedure() {
    closeBasicBlock();
    openClass.getProcedures().add(openProcedure);
  }

  public void closeClass() {
    classes.add(openClass);
  }

  private void startBasicBlock() {
    closeBasicBlock();
    openBasicBlock = new TABasicBlock();
  }

  private void closeBasicBlock() {
    if (openBasicBlock != null && !openBasicBlock.instructions().isEmpty())
      openProcedure.getCode().add(openBasicBlock);
    openBasicBlock = null;
  }

  @Override
  public String toString() {
    String str = "";

    for (TAClass c : classes)
      str += "\n\n" + c;

    return str.trim();
  }
}
