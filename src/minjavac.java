import java.io.*;
import java.util.Properties;
import parser.*;
import analysis.tac.*;
import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.typechecker.*;
import backend.nasm.NasmGenerator;

public class minjavac {
  public static void main(String[] args) throws Exception {
    int ret;
    String sourceName, cmd;

    Properties config = new Properties();
    config.load(minjavac.class.getResourceAsStream("config.txt"));

    if (args.length == 1) {
      new Parser(new java.io.FileInputStream(args[0]));

      int p = args[0].lastIndexOf('.');
      sourceName = args[0].substring(0, p);
    }
    else if (args.length == 0) {
      new Parser(System.in);
      sourceName = "a";
    }
    else {
      System.out.println(
        "Usage: Parser < input filename or Parser input filename."
      );
      return;
    }

    Program program = null;

    try {
      System.out.println("Parsing ...");
      program = Parser.Goal();
    }
    catch (ParseException e) {
      System.out.println(e.getMessage());
    }

    SymbolTableBuilderVisitor symtBuilder = new SymbolTableBuilderVisitor();
    TypeCheckerVisitor typeChecker = new TypeCheckerVisitor();
    TAModuleBuilderVisitor tacBuilder = new TAModuleBuilderVisitor();
    NasmGenerator nasm = new NasmGenerator();

    System.out.println("Building symbol table ...");
    program.accept(symtBuilder);

    System.out.println("Typechecking ...");
    program.accept(typeChecker);

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

    System.out.print("Assembling ... ");

    String objFileName = sourceName + ".o";
    cmd = config.getProperty("nasm_cmd");
    cmd = String.format(cmd, asmFileName, objFileName);
    System.out.println(cmd);

    ret = Runtime.getRuntime().exec(cmd).waitFor();
    if (ret != 0) {
      System.out.println("Assembly error");
      return;
    }

    System.out.print("Linking ...");
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

    System.out.println("\nDone!");
  }
}
