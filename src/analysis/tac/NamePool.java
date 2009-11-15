package analysis.tac;

public class NamePool {
  public static String getLabelName() {
    return ".label_" + nextCode();
  }

  public static String getTempVarName() {
    return ".t_" + nextCode();
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
