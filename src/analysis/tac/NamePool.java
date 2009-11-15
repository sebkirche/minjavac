package analysis.tac;

import analysis.tac.instructions.Label;
import analysis.tac.variables.NormalVar;
import analysis.tac.variables.Variable;

public class NamePool {
  public static Label labelName(String s) {
    return new Label("._" + s + "_" + nextCode());
  }

  public static Variable tempName(String s) {
    return new NormalVar(".t_" + s + "_" + nextCode());
  }

  public static void reset() {
    count = 0;
  }

  private static String nextCode() {
    String str = "";

    int n = count++;

    if (n == 0)
      return "A";

    while (n != 0) {
      str += table[n % 10];
      n /= 10;
    }

    return str;
  }

  private static int count = 10;
  private static final char[] table = {
    'A','B','C','D','E','F','G','H','I','J'
  };
}
