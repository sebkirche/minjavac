package backend.nasm;

import java.util.*;
import java.io.Writer;
import analysis.tac.*;
import analysis.symboltable.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NasmGenerator {
  private List<NasmInstruction> code;
  private List<VirtualTable> virtualTables;

  public NasmGenerator() {
    code = new ArrayList<NasmInstruction>(400);
  }

  public void generate() {
    NasmUtils.calculateOffsets();
    SymbolTable symT = SymbolTable.getInstance();

    emit(Nasm.OTHER.make("; c runtime"));
    emit(Nasm.OTHER.make("extern _alloc, _new_array, _print_int, _print_str"));
    emit(Nasm.OTHER.make("\n"));

    if (TAModule.getInstance().hasConstants()) {
      emit(Nasm.OTHER.make("; constant definitions"));
      emit(Nasm.DATA_SEGMENT.make());
      emitConstantDefinitions();
      emit(Nasm.OTHER.make("\n"));
    }

    emit(Nasm.OTHER.make("; vt definitions"));
    emit(Nasm.DATA_SEGMENT.make());
    emitVirtualTableDefinitions();
    emit(Nasm.OTHER.make("\n"));

    emit(Nasm.OTHER.make("; constructors"));
    emit(Nasm.TEXT_SEGMENT.make());
    emitClassConstructors();
    emit(Nasm.OTHER.make("\n"));
    
    emit(Nasm.OTHER.make("; code"));
    emit(Nasm.TEXT_SEGMENT.make());
    emit(Nasm.OTHER.make(" global _asmMain"));
    emit(Nasm.OTHER.make(""));

    for (ClassDescriptor c : symT.getClassDescriptors()) {
      for (MethodDescriptor m : c.getMethodDescriptors()) {
        if (c == symT.getMainClass())
          emit(Nasm.LABEL.make("_asmMain"));

        emitMethodCode(m);

        if (c == symT.getMainClass()) {
          emit(Nasm.OTHER.make(""));
          emit(Nasm.OP.make("mov esp, ebp"));
          emit(Nasm.OP.make("pop ebp"));
          emit(Nasm.OP.make("ret"));
        }

        emit(Nasm.OTHER.make("\n"));
      }
    }
  }

  private void emitVirtualTableDefinitions() {
    virtualTables = NasmUtils.buildVirtualTables();

    for (VirtualTable vt : virtualTables) {
      String label = vt.getName();
      String array = vt.getMethodLabels().toString();
      array = array.substring(1, array.length()-1);

      emit(Nasm.OTHER.make(
        NasmUtils.labelPad + String.format("%-15s : dd %s", label, array)
      ));
    }
  }

  private void emitConstantDefinitions() {
    TAModule module = TAModule.getInstance();

    for (Map.Entry<String,String> entry : module.getStringPool()) {
      String handle = entry.getValue();
      byte[] str = null;

      try {
        str = entry.getKey().getBytes("ASCII");
      } catch (UnsupportedEncodingException e) {
        throw new Error(e);
      }

      String[] chars = new String[str.length+1];
      for (int i = 0; i < str.length; ++i)
        chars[i] = NasmUtils.byteToHex(str[i]);
      chars[str.length] = "0x00";

      String line = String.format("%-6s : db ", handle);

      for (int i = 0; i < chars.length; ++i) {
        if (i != 0) line += ", ";
        line += chars[i];
      }

      emit(Nasm.OTHER.make(
        NasmUtils.labelPad + line
      ));
    }
  }

  private void emitClassConstructors() {
    SymbolTable symT = SymbolTable.getInstance();

    for (ClassDescriptor c : symT.getClassDescriptors()) {
      emit(Nasm.OTHER.make(""));
      emit(Nasm.LABEL.make(c.getName() + "@@new"));
      emit(Nasm.OP.make("push dword " + c.getSize()));
      emit(Nasm.OP.make("call _alloc"));
      emit(Nasm.OP.make("add esp, 4"));
      emit(Nasm.OP.make("mov [eax+0], dword " + c.getName() + "@@vt"));
      emit(Nasm.OP.make("ret"));
    }
  }

  private void emitMethodCode(MethodDescriptor method) {
    emit(Nasm.LABEL.make(method.getLabel()));

    // prologue
    emit(Nasm.OP.make("push ebp"));
    emit(Nasm.OP.make("mov ebp, esp"));

    if (!method.getLocalVars().isEmpty())
      emit(Nasm.OP.make("sub esp, " + 4*method.getLocalVars().size()));

    // code
    TAProcedure proc = TAModule.getInstance().getProcedure(method.getLabel());
    TABasicBlockEmitter blockEmitter = new TABasicBlockEmitter(
      code, virtualTables
    );

    for (TABasicBlock block : proc.getCode()) {
      code.add(Nasm.OTHER.make(""));
      blockEmitter.visit(block, method);
    }
  }

  private void emit(NasmInstruction i) {
    code.add(i);
  }

  public void writeTo(String filename, Writer out) throws IOException {
    out.write("; " + filename + "\n\n");
    for (NasmInstruction i : code)
      out.write(i + "\n");
  }
}
