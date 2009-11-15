import parser.*;
import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.tac.TAModuleBuilderVisitor;
import analysis.typechecker.*;

public class minjavac {
  public static void main(String[] args) throws Exception {
    Parser parser = null ;

    if (args.length == 1) {
        parser = new Parser(new java.io.FileInputStream(args[0]));
    }
    else if (args.length == 0) {
      parser = new Parser(System.in);
    }
    else {
      System.out.println("Usage: Parser < input filename or Parser input filename.");
      return;
    }

    Program program = null ;

    try {
      program = parser.Goal();
    }
    catch (ParseException e) {
      System.out.println("Exiting.");
      throw e;
    }

    SymbolTableBuilderVisitor symtBuilder = new SymbolTableBuilderVisitor();
    SymbolTable symT = symtBuilder.getSymbolTable();

    TypeCheckerVisitor typeChecker = new TypeCheckerVisitor(symT);
    TAModuleBuilderVisitor tacBuilder = new TAModuleBuilderVisitor(symT);

    System.out.println("Building symbol table...");
    program.accept(symtBuilder);

    System.out.println("Typechecking...");
    program.accept(typeChecker);

    System.out.println("Building IR...");
    program.accept(tacBuilder);

    System.out.println("IR:");
    System.out.println("\n" + tacBuilder.getModule());

    System.out.println("\nOk!");
  }
}
