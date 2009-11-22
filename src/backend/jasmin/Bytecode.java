package backend.jasmin;

import java.io.*;
import java.util.*;

public class Bytecode {
  private static String className;
  private static List<String> lines;

  public static void init() {
    lines = new ArrayList<String>(300);
  }

  public static void setClassName(String name) {
    className = name;
  }

  public static void newline() {
    lines.add("");
  }

  public static void newline2() {
    lines.add("\n");
  }

  public static void directive(String s) {
    lines.add(s);
  }
  
  public static void label(String l) {
    if (!lines.get(lines.size()-1).trim().isEmpty())
      newline();
    
    lines.add("  " + l + ":");
  }

  public static void code(String c) {
    lines.add("   " + c);
  }

  public static void save() {
    String filename = className + ".jasmin";
    
    try {
      FileWriter writer = new FileWriter(filename);

      for (String line : lines) {
        writer.append(line);
        writer.append('\n');
      }

      writer.close();
    } catch (IOException e) { }
  }
}
