package minjavac.manualParser;

import java.io.*;
import java.util.*;
import java.lang.reflect.Field;
import minjavac.ParserConstants;
import minjavac.manualParser.grammar.*;

public class LL1Grammar implements ParserConstants {
  private final static String minJavaGrammar = 
          "<goal> ::= ((<ADD> | <ASSIGN>) <NUM>)*;"
  ;

  private String firstSymbol;
  private List<Rule> grammar;
  private Map<String,Set<String>> first, follow;
  private Map<String,Integer> terminalId, nonTerminalId;
  public static final String lambda = "#";

  public LL1Grammar() { }

  public List<Rule> processGrammar() throws Exception {
    GrammarParser gp = new GrammarParser(new StringReader(minJavaGrammar));
    grammar = gp.parse();
    firstSymbol = gp.getFirstSymbol();

    terminalId = new HashMap<String,Integer>();
    nonTerminalId = new HashMap<String,Integer>();

    for (Field f : ParserConstants.class.getDeclaredFields()) {
      String name = f.getName();
      if (name.equals("tokenImage") || name.equals("DEFAULT")) continue;

      terminalId.put(name, f.getInt(this));
    }

    for (Rule r : grammar) {
      String lhs = r.getLhs();

      if (terminalId.containsKey(lhs))
        throw new Exception("Terminal appears on left side of rule: " + r);

      if (!nonTerminalId.containsKey(lhs))
        nonTerminalId.put(lhs, nonTerminalId.size());
    }

    for (Rule r : grammar) for (String rhs : r.getRhs())
      if (!(terminalId.containsKey(rhs)    ||
            nonTerminalId.containsKey(rhs) ||
            rhs.equals(lambda))
          )
        throw new Exception("Unknown label: " + rhs + ", on rule: " + r);

    mountFirst();
    mountFollow();
    checkLL1();

    System.out.println("grammar = ");
    for (Rule r : grammar)
      System.out.println("\t" + r);

    return grammar;
  }

  public String firstSymbol() {
    return firstSymbol;
  }

  public Map<String,Set<String>> getFirst() {
    return first;
  }

  public Map<String,Set<String>> getFollow() {
    return follow;
  }

  public Map<String,Integer> terminalIds() {
    return terminalId;
  }

  public Map<String,Integer> nonTerminalIds() {
    return nonTerminalId;
  }

  private void mountFirst() {
    first = new HashMap<String,Set<String>>();

    for (String terminal : terminals()) {
      Set<String> conj = new HashSet<String>();
      conj.add(terminal);
      first.put(terminal, conj);
    }

    Set<String> conj = new HashSet<String>();
    conj.add(lambda);
    first.put(lambda, conj);

    for (String var : nonTerminals()) {
      first.put(var, new HashSet<String>());
    }

    boolean changed = true;

    while (changed) {
      changed = false;

      for (Rule r : grammar) {
        String a = r.getLhs();
        Set<String> old = new HashSet(first.get(a));

        List<String> rhs = r.getRhs();

        Set<String> firstA = first.get(a);

        int i = 0;
        firstA.addAll(filterLambda(first.get(rhs.get(i))));

        while (i < rhs.size() - 1 && first.get(rhs.get(i)).contains(lambda))
          firstA.addAll(filterLambda(first.get(rhs.get(++i))));

        if (i == rhs.size()-1 && first.get(rhs.get(i)).contains(lambda))
          firstA.add(lambda);

        if (!changed && !old.equals(firstA))
          changed = true;
      }
    }
  }

  private void mountFollow() {
    follow = new HashMap<String,Set<String>>();

    for (String t : terminals())
      follow.put(t, new HashSet<String>());

    for (String v : nonTerminals())
      follow.put(v, new HashSet<String>());

    follow.put(lambda, new HashSet<String>());
    follow.get(grammar.get(0).getLhs()).add("EOF");

    boolean changed = true;

    while (changed) {
      changed = false;

      for (Rule r : grammar) {
        String a = r.getLhs();
        List<String> rhs = r.getRhs();

        Set<String> followA = follow.get(a);
        Set<String> followBk = follow.get(rhs.get(rhs.size()-1));
        Set<String> oldFollowBk = new HashSet<String>(followBk);
        Set<String> trailer = new HashSet<String>();

        followBk.addAll(followA);
        trailer.addAll(followA);

        if (!changed && !oldFollowBk.equals(followBk))
          changed = true;

        for (int i = rhs.size()-1; i >= 1; --i) {
          Set<String> firstBi = first.get(rhs.get(i));
          Set<String> followBi1 = follow.get(rhs.get(i-1));
          Set<String> oldFollowBi1 = new HashSet<String>(followBi1);

          followBi1.addAll(filterLambda(firstBi));

          if (firstBi.contains(lambda))
            followBi1.addAll(trailer);
          else
            trailer = firstBi;

          if (!changed && !oldFollowBi1.equals(followBi1))
            changed = true;
        }
      }
    }
  }

  private void checkLL1() throws Exception {
    int sz = grammar.size();
    for (int i = 0; i < sz; ++i)
      for (int j = 0; j < sz; ++j) {
        if (i == j) continue;

        Rule r1 = grammar.get(i);
        Rule r2 = grammar.get(j);

        if (!(r1.getLhs().equals(r2.getLhs()))) continue;

        String a = r1.getLhs();

        List<String> alpha = r1.getRhs(), beta = r2.getRhs();

        Set<String> followA = follow.get(a);
        Set<String> firstAlpha = sentenceFirst(alpha);
        Set<String> firstBeta = sentenceFirst(beta);

        if (!intersection(firstAlpha, firstBeta).isEmpty())
          throw new Exception(
            "LL(1) conflict: first sets intersection on rules " +
            r1 + " and " + r2
          );

        if (firstBeta.contains(lambda) &&
            !intersection(firstAlpha, followA).isEmpty())
          throw new Exception(
            "LL(1) conflict: rule " + r2 + " can be lambda and " +
            " rule " + r1 + " conflicts with follow of " + r1.getLhs()
          );
      }
  }

  private Set<String> terminals() {
    return terminalId.keySet();
  }

  private Set<String> nonTerminals() {
    return nonTerminalId.keySet();
  }

  public Set<String> filterLambda(Set<String> s) {
    if (s.contains(lambda)) {
      Set<String> ns = new HashSet<String>(s);
      ns.remove(lambda);
      return ns;
    }
    else {
      return s;
    }
  }

  public Set<String> sentenceFirst(List<String> l) {
    Set<String> sfirst = new HashSet<String>();

    int i = 0;
    sfirst.addAll(filterLambda(first.get(l.get(i))));

    while (i < l.size()-1 && first.get(l.get(i)).contains(lambda))
      sfirst.addAll(filterLambda(first.get(l.get(++i))));

    if (i == l.size()-1 && first.get(l.get(i)).contains(lambda))
      sfirst.add(lambda);

    return sfirst;
  }

  private <T> Set<T> intersection(Set<T> a, Set<T> b) {
    Set<T> s = new HashSet<T>(a);
    s.retainAll(b);
    return s;
  }
}
