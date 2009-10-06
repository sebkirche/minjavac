package minjavac.manualParser.grammar;

import java.util.LinkedList;
import java.util.List;

public class Rule {
  public Rule(String lhs) {
    this(lhs, new LinkedList<String>());
  }

  public Rule(String lhs, List<String> rhs) {
    this.lhs = lhs.substring(1, lhs.length()-1);

    this.rhs = new LinkedList<String>();

    for (String s : rhs)
      this.rhs.add(s.substring(1, s.length()-1));
  }

  public String getLhs() {
    return lhs;
  }

  public List<String> getRhs() {
    if (isLambda()) {
      List<String> l = new LinkedList<String>();
      l.add("#");
      return l;
    }
    else {
      return rhs;
    }
  }

  public boolean isLambda() {
    return rhs.isEmpty();
  }

  @Override
  public String toString() {
    String str = lhs + " ::=";

    for (String s : rhs)
      str += " " + s;

    if (rhs.isEmpty()) str += " #";

    return str + " ;";
  }

  private String lhs;
  private List<String> rhs;
}
