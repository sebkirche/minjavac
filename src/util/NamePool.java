package util;

import java.util.Map;
import java.util.HashMap;

public class NamePool {
  public static void reset() {
    counts.clear();
  }

  public static String nextCode(String s) {
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
