package backend.nasm;

public enum Nasm {
  OTHER,
  COMMENT,
  SEPARATOR,
  DATA_SEGMENT,
  BSS_SEGMENT,
  TEXT_SEGMENT,
  GLOBAL_SYMBOL,
  LABEL,
  INSTRUCTION;

  public NasmInstruction make() {
    return make(null);
  }

  public NasmInstruction make(String t) {
    return make(t, "");
  }

  public NasmInstruction make(String t, String c) {
    return new NasmInstruction(this, t, c);
  }
}