import java.io.*;
import java.util.*;

import parser.*;
import analysis.tac.*;
import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.typechecker.*;
import backend.nasm.NasmGenerator;
import backend.jasmin.BytecodeEmitter;

public class minjavac {
  private static String sourceName;
  private static Properties config;

  public static void main(String[] args) throws Exception {
    assertUsage(
      args.length == 1 || args.length == 3
    );

    String backend;
    config = new Properties();
    config.load(minjavac.class.getResourceAsStream("config.txt"));

    int p = args[0].lastIndexOf('.');
    sourceName = args[0].substring(0, p);

    new Parser(new FileInputStream(args[0]));

    if (args.length == 3) {
      assertUsage(args[1].equals("-backend"));
      assertUsage(args[2].equals("jvm") || args[2].equals("nasm"));
      backend = args[2];
    }
    else {
      backend = "nasm";
    }

    Program program = null;

    try {
      System.out.println("Parsing ...");
      program = Parser.Goal();
    }
    catch (ParseException e) {
      System.out.println(e.getMessage());
      return;
    }

    SymbolTableBuilderVisitor symtBuilder = new SymbolTableBuilderVisitor();
    TypeCheckerVisitor typeChecker = new TypeCheckerVisitor();

    System.out.println("Building symbol table ...");
    program.accept(symtBuilder);

    System.out.println("Typechecking ...");
    program.accept(typeChecker);

    if (backend.equals("nasm")) {
      nasmBuild(program);
    }
    else if (backend.equals("jvm")) {
      jvmBuild(program);
    }

    System.out.println("\nDone!");
  }

  private static void assertUsage(boolean b) {
    if (b) return;

    System.out.println("Usage: minjavac filename [options]");
    System.out.println("Options:");
    System.out.println("  -backend (jvm | nasm)");
    System.exit(1);
  }

  private static void nasmBuild(Program program) throws Exception {
    int ret;
    String cmd;

    TAModuleBuilderVisitor tacBuilder = new TAModuleBuilderVisitor();
    NasmGenerator nasm = new NasmGenerator();

    System.out.println("Making IR ...");
    program.accept(tacBuilder);

    /*FileWriter irFile = new FileWriter(sourceName + ".tac");
    irFile.write(TAModule.getInstance().toString());
    irFile.close();*/

    System.out.println("Making asm ...");
    nasm.generate();

    String asmFileName = sourceName + ".nasm";
    FileWriter asmFile = new FileWriter(asmFileName);
    nasm.writeTo(asmFileName, asmFile);
    asmFile.close();

    System.out.println("Assembling ...\n");

    String objFileName = sourceName + ".o";
    cmd = config.getProperty("nasm_cmd");
    cmd = String.format(cmd, asmFileName, objFileName);
    System.out.println(cmd);

    ret = Runtime.getRuntime().exec(cmd).waitFor();
    if (ret != 0) {
      System.out.println("Assembly error");
      return;
    }

    System.out.println("Linking ...\n");
    cmd = config.getProperty("compiler_cmd");
    String runtime_lib = config.getProperty("runtime_lib_path");
    String exeFileName = sourceName + ".exe";
    cmd = String.format(cmd, objFileName, runtime_lib, exeFileName);
    
    System.out.println(cmd);

    ret = Runtime.getRuntime().exec(cmd).waitFor();
    if (ret != 0) {
      System.out.println("Linker error");
      return;
    }
  }

  private static void jvmBuild(Program program) throws Exception {
    System.out.println("Building class assembly ...\n");
    
    BytecodeEmitter emitter = new BytecodeEmitter();
    program.accept(emitter);

    String jasmin_path = config.getProperty("jasmin_path");

    for (ClassDescriptor c : SymbolTable.getInstance().getClassDescriptors()) {
      String classN = c.getName();
      String jasmin_class = classN + ".jasmin";

      String cmd = "java -jar " + jasmin_path + " " + jasmin_class;
      System.out.println(cmd);

      Process proc = Runtime.getRuntime().exec(cmd);

      String stdout = readInputStream(proc.getInputStream());
      String stderr = readInputStream(proc.getErrorStream());

      if (!stdout.isEmpty())
        System.out.println(stdout);

      if (!stderr.isEmpty())
        System.out.println(stderr);
    }
  }

  private static String readInputStream(InputStream stream) throws Exception {
    Reader r = new InputStreamReader(stream, "ASCII");

    int n;
    StringBuffer sb = new StringBuffer(200);
    char[] buff = new char[100];

    while ((n = r.read(buff)) != -1) {
      sb.append(buff, 0, n);
    }

    return sb.toString();
  }
}
