package analysis.tac;

import java.util.Map;
import java.util.HashMap;
import analysis.syntaxtree.Type;
import analysis.tac.instructions.Label;
import analysis.tac.variables.TALocalVar;
import analysis.tac.variables.TAVariable;

public class NamePool {
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
    counts.clear();
  }

  private static String nextCode(String s) {
    Integer i = counts.remove(s);

    if (i == null) {
      counts.put(s, 1);
      i = 0;
    } else {
      counts.put(s, i.intValue()+1);
    }

    String str = "_";
    int n = i;

    if (n == 0)
      return "";

    while (n != 0) {
      str += table[n % 10];
      n /= 10;
    }

    return str;
  }

  private static Map<String,Integer> counts =
          new HashMap<String, Integer>(40);
  private static final char[] table = {
    '0', 'A','B','C','D','E','F','G','H','I','J'
  };
}
