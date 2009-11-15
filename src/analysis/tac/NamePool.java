package analysis.tac;

import analysis.tac.instructions.Label;
import analysis.tac.variables.NormalVar;
import analysis.tac.variables.Variable;
import java.util.HashMap;
import java.util.Map;

public class NamePool {
  public static Label labelName(String s) {
    return new Label(nextName(s));
  }

  public static Variable tempName(String s) {
    return new NormalVar(nextName(s));
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
