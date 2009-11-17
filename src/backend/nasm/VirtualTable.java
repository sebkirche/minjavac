package backend.nasm;

public class VirtualTable {
  private String[] table;

  public VirtualTable(int size) {
    table = new String[size];
  }

  public void setMethod(int methodPos, String methodLabel) {
    table[methodPos] = methodLabel;
  }

  public String[] getTable() {
    return table;
  }
}
