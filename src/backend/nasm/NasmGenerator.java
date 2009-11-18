package backend.nasm;

import java.util.*;

public class NasmGenerator {
  public static void generate() {
    NasmUtils.calculateOffsets();
    List<VirtualTable> virtualTables = NasmUtils.buildVirtualTables();

    
  }
}
