import java.io.*;
import parser.*;
import analysis.tac.*;
import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.typechecker.*;
import backend.nasm.NasmGenerator;

public class minjavac {
  public static void main(String[] args) throws Exception {
    String sourceName;

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

    FileWriter irFile = new FileWriter(sourceName + ".tac");
    irFile.write(TAModule.getInstance().toString());
    irFile.close();

    System.out.println("Making asm ...");
    nasm.generate();

    FileWriter asmFile = new FileWriter(sourceName + ".nasm");
    nasm.writeTo(asmFile);
    asmFile.close();

    System.out.println("\nDone!");
  }
}
