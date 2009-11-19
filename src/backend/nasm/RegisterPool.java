package backend.nasm;

import java.util.*;
import analysis.tac.*;
import analysis.symboltable.*;
import analysis.tac.variables.TALocalVar;
import analysis.tac.instructions.TAInstruction;

public class RegisterPool {
  public static final List<String> registerNames;
  
  static {
    registerNames = new ArrayList<String>(4);
    registerNames.add("ebx");
    registerNames.add("ecx");
    registerNames.add("eax");
    //registerNames.add("edx"); (this)
    //registerNames.add("esi");
    //registerNames.add("edi");
  }

  public class VarGenDescriptor extends HashSet<String> {
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

  public class RegGenDescriptor extends HashSet<String> {
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
    registers = new HashMap<String,RegGenDescriptor>();
    variables = new HashMap<String,VarGenDescriptor>();
    descriptors = new HashMap<String,VariableDescriptor>();

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
  public void killVar(String var) {
    for (String reg : varDescriptor(var))
      regDescriptor(reg).remove(var);

    varDescriptor(var).clear();
  }

  public void spillRegister(String reg) {
    for (String var : copy(regDescriptor(reg)))
      removeFromRegister(reg, var);
  }

  public boolean regNeedSaving(String reg) {
    for (String var : regDescriptor(reg)) {
      VarGenDescriptor varD = varDescriptor(var);
      if (!isDead(var) && varD.size() == 1 && !varD.onMemory())
        return true;
    }

    return false;
  }

  /**
   * descarta o valor de var armazenado em reg
   */
  public void removeFromRegister(String reg, String var) {
    if (isDead(var)) {
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
  public void prepareDestiny(String reg, String var) {
    for (String _var : copy(regDescriptor(reg)))
      if (!var.equals(_var))
        removeFromRegister(reg, _var);

    varDescriptor(var).setOnly(reg);
    regDescriptor(reg).setOnly(var);
  }

  /**
   * prepara reg para receber o valor de var no lado direito
   */
  public void prepareSource(String reg, String var) {
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

  public String getRegForDestiny(String var) {
    return getRegForDestiny(var, emptySet());
  }

  public String getRegForDestiny(String var, Set<String> fr) {
    return minSpills(var, fr);
  }

  public String getRegForSource(String var) {
    return getRegForSource(var, emptySet());
  }

  public String getRegForSource(String var, Set<String> fr) {
    for (String reg : registerNames)
      if (regDescriptor(reg).contains(var))
        return reg;

    return minSpills("", fr);
  }

  public void saveForExit(TABasicBlock block) {
    for (TALocalVar v : block.liveVars()) {
      String var = v.getName();
      VarGenDescriptor varD = varDescriptor(var);
      
      if (!varNeedSaving(var)) continue;

      String reg = varD.iterator().next();

      String mov = String.format(
        "mov [%s], %s", varD.getMemoryId(var), reg
      );

      emit(Nasm.OP.make(mov));
    }
  }

  public String getVariableHandle(String var) {
    VarGenDescriptor varD = varDescriptor(var);

    if (!varD.isEmpty())
      return varD.iterator().next();
    else
      return '[' + varD.getMemoryId(var) + ']';
  }

  public String minSpills(String deadVar) {
    return minSpills(deadVar, emptySet());
  }

  /**
   * returns the local best register for holding some new
   * value, disregarding the value of deadVar
   */
  public String minSpills(String deadVar, Set<String> fr) {
    int min_cost = Integer.MAX_VALUE;
    String best_reg = "";

    for (String reg : registerNames) {
      if (fr.contains(reg)) continue;

      int cost = 0;

      for (String var : regDescriptor(reg)) {
        if (var.equals(deadVar) || isDead(var)) continue;
        ++cost;
      }

      if (cost < min_cost) {
        min_cost = cost;
        best_reg = reg;
      }

      if (cost == 0) break;
    }

    return best_reg;
  }

  public VarGenDescriptor varDescriptor(String var) {
    return variables.get(var);
  }

  public RegGenDescriptor regDescriptor(String reg) {
    return registers.get(reg);
  }

  private boolean varNeedSaving(String var) {
    VarGenDescriptor varD = varDescriptor(var);
    return varD.size() == 1 && !varD.onMemory();
  }

  private boolean isDead(String var) {
    return instruction.deadVars().contains(new TALocalVar(var));
  }

  public void emit(NasmInstruction i) {
    code.add(i);
  }

  private Set<String> copy(Set<String> s) {
    return new HashSet<String>(s);
  }

  private Set<String> emptySet() {
    return new HashSet<String>();
  }
}
