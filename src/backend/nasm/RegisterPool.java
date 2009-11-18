package backend.nasm;

import java.util.*;
import analysis.symboltable.*;
import analysis.tac.variables.TALocalVar;
import analysis.tac.instructions.TAInstruction;

public class RegisterPool {
  private static final Set<String> registerNames;
  
  static {
    registerNames = new HashSet<String>(20);
    registerNames.add("eax");
    registerNames.add("ebx");
    registerNames.add("ecx");
    registerNames.add("edx");
    registerNames.add("esi");
    registerNames.add("edi");
  }

  private class VarGenDescriptor extends HashSet<String> {
    private boolean onMem;

    public VarGenDescriptor() {
      this(false);
    }

    public VarGenDescriptor(boolean mem) {
      setOnMemory(mem);
    }

    public boolean onMemory() {
      return onMem;
    }

    public void setOnMemory(boolean mem) {
      onMem = mem;
    }

    public String getMemoryId(String varName) {
      VariableDescriptor vd = descriptors.get(varName);

      int offset = vd.getOffset();

      if (offset < 0)
        return "ebp-" + Math.abs(offset);
      else
        return "ebp+" + Math.abs(offset);
    }

    @Override
    public void clear() {
      super.clear();
      onMem = false;
    }

    public void setOnlyOnMemory() {
      clear();
      onMem = true;
    }

    public void setOnly(String reg) {
      clear();
      add(reg);
    }
  }

  private class RegGenDescriptor extends HashSet<String> {
    public void setOnly(String var) {
      clear();
      add(var);
    }
  }

  private List<NasmInstruction> code;
  private TAInstruction instruction;
  private Map<String,RegGenDescriptor> registers;
  private Map<String,VarGenDescriptor> variables;
  private Map<String,VariableDescriptor> descriptors;

  public RegisterPool(MethodDescriptor methodD, List<NasmInstruction> c) {
    code = c;
    instruction = null;

    for (String reg : registerNames)
      registers.put(reg, new RegGenDescriptor());

    for (VariableDescriptor param : methodD.getParameters()) {
      descriptors.put(param.name(), param);
      variables.put(param.name(), new VarGenDescriptor(true));
    }

    for (VariableDescriptor local : methodD.getLocalVars()) {
      descriptors.put(local.name(), local);
      variables.put(local.name(), new VarGenDescriptor(false));
    }
  }

  public void setCurrentInstruction(TAInstruction i) {
    instruction = i;
  }

  /**
   * descarta valores antigos desta variável
   */
  private void killVar(String var) {
    for (String reg : varDescriptor(var))
      regDescriptor(reg).remove(var);

    varDescriptor(var).clear();
  }

  /**
   * descarta o valor de var armazenado em reg
   */
  private void removeFromRegister(String reg, String var) {
    if (instruction.deadVars().contains(new TALocalVar(var))) {
      varDescriptor(var).clear();
    } else {
      VarGenDescriptor varD = varDescriptor(var);

      if (varD.size() == 1 && !varD.onMemory()) {
        String mov = String.format(
          "mov [%s], %s", varD.getMemoryId(var), reg
        );
        
        emit(Nasm.OP.make(mov));
        varD.setOnlyOnMemory();
      }

      varD.remove(reg);
    }

    regDescriptor(reg).remove(var);
  }

  /**
   * prepara reg para receber o valor de var no lado esquerdo
   */
  private void prepareDestiny(String reg, String var) {
    for (String _var : copy(regDescriptor(reg)))
      if (!var.equals(_var))
        removeFromRegister(reg, _var);

    varDescriptor(var).setOnly(reg);
    regDescriptor(reg).setOnly(var);
  }

  /**
   * prepara reg para receber o valor de var no lado direito
   */
  private void prepareSource(String reg, String var) {
    if (!regDescriptor(reg).contains(var)) {
      for (String _var : copy(regDescriptor(reg)))
        removeFromRegister(reg, _var);

      VarGenDescriptor varD = varDescriptor(var);

      String mov = String.format(
        "mov %s, [%s]", reg, varD.getMemoryId(var)
      );

      emit(Nasm.OP.make(mov));
    }

    regDescriptor(reg).setOnly(var);
    varDescriptor(var).add(reg);
  }

  private VarGenDescriptor varDescriptor(String var) {
    return variables.get(var);
  }

  private RegGenDescriptor regDescriptor(String reg) {
    return registers.get(reg);
  }

  private void emit(NasmInstruction i) {
    code.add(i);
  }

  private Set<String> copy(Set<String> s) {
    return new HashSet<String>(s);
  }
}
