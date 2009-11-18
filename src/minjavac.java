import parser.*;
import analysis.tac.*;
import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.typechecker.*;

public class minjavac {
  public static void main(String[] args) throws Exception {
    if (args.length == 1) {
      new Parser(new java.io.FileInputStream(args[0]));
    }
    else if (args.length == 0) {
      new Parser(System.in);
    }
    else {
      System.out.println(
        "Usage: Parser < input filename or Parser input filename."
      );
      return;
    }

    Program program = null;

    try {
      program = Parser.Goal();
    }
    catch (ParseException e) {
      System.out.println(e.getMessage());
    }

    SymbolTableBuilderVisitor symtBuilder = new SymbolTableBuilderVisitor();
    TypeCheckerVisitor typeChecker = new TypeCheckerVisitor();
    TAModuleBuilderVisitor tacBuilder = new TAModuleBuilderVisitor();

    System.out.println("Building symbol table...");
    program.accept(symtBuilder);

    System.out.println("Typechecking...");
    program.accept(typeChecker);

    System.out.println("Building IR...");
    program.accept(tacBuilder);

    System.out.println("IR:");
    System.out.println("\n" + TAModule.getInstance());

    System.out.println("\nOk!");
  }
}
