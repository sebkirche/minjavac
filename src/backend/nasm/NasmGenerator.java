package backend.nasm;

import java.util.*;
import java.io.Writer;
import analysis.tac.*;
import analysis.symboltable.*;
import java.io.IOException;

public class NasmGenerator {
  private List<NasmInstruction> code;
  private List<VirtualTable> virtualTables;

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

    for (ClassDescriptor c : symT.getClassDescriptors()) {
      for (MethodDescriptor m : c.getMethodDescriptors()) {
        if (c == symT.getMainClass())
          emit(Nasm.LABEL.make("_main"));

        emitMethodCode(m);
      }
    }
  }

  private void emitVirtualTableDefinitions() {
    virtualTables = NasmUtils.buildVirtualTables();

    for (VirtualTable vt : virtualTables) {
      String label = vt.getName();
      String array = vt.getMethodLabels().toString();
      array = array.substring(1, array.length()-1);

      emit(Nasm.OTHER.make(label + ": dd " + array));
    }
  }

  private void emitMethodCode(MethodDescriptor method) {
    emit(Nasm.LABEL.make(method.getLabel()));

    // prologue
    emit(Nasm.OP.make("push ebp"));
    emit(Nasm.OP.make("mov ebp, esp"));
    emit(Nasm.OP.make("sub esp, " + method.getLocalVars().size()));

    // code
    TAProcedure proc = TAModule.getInstance().getProcedure(method.getLabel());
    TABasicBlockEmitter blockEmitter = new TABasicBlockEmitter(
      code, virtualTables
    );

    for (TABasicBlock block : proc.getCode()) {
      code.add(Nasm.OTHER.make("\n\n"));
      blockEmitter.visit(block, method);
    }

    // epilogue
    emit(Nasm.OP.make("mov esp, ebp"));
    emit(Nasm.OP.make("pop ebp"));
    emit(Nasm.OP.make("ret"));
  }

  private void emit(NasmInstruction i) {
    code.add(i);
  }

  public void writeTo(Writer out) throws IOException {
    for (NasmInstruction i : code)
      out.write(i + "\n");
  }
}
