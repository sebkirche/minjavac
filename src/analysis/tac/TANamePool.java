package analysis.tac;

import util.NamePool;
import analysis.syntaxtree.Type;
import analysis.tac.instructions.Label;
import analysis.tac.variables.TALocalVar;
import analysis.tac.variables.TAVariable;

public class TANamePool {
  public static Label newLabel(String s) {
    return new Label(nextName(s));
  }

  public static TAVariable newVar(String s, Type t) {
    String varName = nextName(s);
    TAModule.getInstance().addTemporaryVar(varName, t);
    return new TALocalVar(varName);
  }

  public static String nextName(String s) {
    return "." + s + nextCode(s);
  }

  public static void reset() {
    NamePool.reset();
  }

  private static String nextCode(String s) {
    return NamePool.nextCode(s);
  }
}
