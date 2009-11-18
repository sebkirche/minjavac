package backend.nasm;

public class NasmInstruction {
  private Nasm type;
  private String text;
  private String comment;

  public NasmInstruction(Nasm _type) {
    this(_type, null, "");
  }

  public NasmInstruction(Nasm _type, String _text, String _c) {
    type = _type;
    text = _text;
    comment = _c;
  }

  public Nasm getStatement() {
    return type;
  }

  public String getText() {
    return text;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public String toString() {
    String str = "";

    switch (type) {
      case DATA_SEGMENT:
        str = "segment .data"; break;
      case BSS_SEGMENT:
        str = "segment .bss"; break;
      case TEXT_SEGMENT:
        str = "segment .text"; break;
      case GLOBAL_SYMBOL:
        str = NasmUtils.stmtPad + "global " + text; break;
      case LABEL:
        str = NasmUtils.labelPad + text + ":"; break;
      case INSTRUCTION:
        str = NasmUtils.stmtPad + text; break;
      case COMMENT:
        str = "# " + text; break;
      case OTHER:
        str = text; break;
    }

    if (!comment.isEmpty())
      str += " # " + comment;

    return str;
  }
}
