package backend.nasm;

import java.util.*;
import java.io.Writer;
import analysis.symboltable.*;

public class NasmGenerator {
  private List<NasmInstruction> code;

  public NasmGenerator() {
    code = new ArrayList<NasmInstruction>(400);
  }

  public void generate() {
    NasmUtils.calculateOffsets();
    SymbolTable symT = SymbolTable.getInstance();

    emit(Nasm.COMMENT.make("Virtual table definitions:"));
    emit(Nasm.DATA_SEGMENT.make());
    
    emitVirtualTableDefinitions();

    emit(Nasm.COMMENT.make("Method definitions:"));
    emit(Nasm.TEXT_SEGMENT.make());
    emit(Nasm.OTHER.make("\n"));

    for (ClassDescriptor c : symT.getClassDescriptors())
      for (MethodDescriptor m : c.getMethodDescriptors())
        emitMethodCode(m);

    emit(Nasm.COMMENT.make("Main definition:"));
    emitAsmMain(symT.getMainClass());
  }

  private void emitVirtualTableDefinitions() {
    List<VirtualTable> virtualTables = NasmUtils.buildVirtualTables();

    for (VirtualTable vt : virtualTables) {
      String label = vt.getName();
      String array = vt.getMethodLabels().toString();
      array = array.substring(1, vt.getSize()-1);

      emit(Nasm.OTHER.make(label + ": dd " + array));
    }
  }

  private void emitMethodCode(MethodDescriptor method) {
    emit(Nasm.LABEL.make(method.getLabel()));
    // here
  }

  private void emitAsmMain(ClassDescriptor mainClass) {
    
  }

  private void emit(NasmInstruction i) {
    code.add(i);
  }

  public void writeTo(String sourceFilename, Writer out) {
    // nasm output
  }
}
