package minjavac.manualParser;

import java.io.*;
import java.util.*;
import minjavac.ParseException;
import minjavac.ParserConstants;
import minjavac.Token;
import minjavac.manualParser.grammar.Rule;

public class Parser implements ParserConstants {
  public static void main(String[] args) throws Exception {
    Parser p;

    if (args.length == 1) {
      p = new Parser(new FileInputStream(args[0]));
    }
    else if (args.length == 0) {
      p = new Parser(System.in);
    }
    else {
      System.out.println("Usage: minjavac input or minjavac < input.");
      return;
    }

    p.parse();
  }

  private enum CellType { Rule, Sync, Error };

  public Parser(InputStream input) throws Exception {
    lex = new minjavac.Parser(input);
    
    ll = new LL1Grammar();
    grammar = ll.processGrammar();
    first  = ll.getFirst();
    follow = ll.getFollow();
    terminalId = ll.terminalIds();
    nonTerminalId = ll.nonTerminalIds();

    numL = nonTerminalId.size();

    int max = 0;
    for (int v : terminalId.values())
      if (v > max) max = v;

    numC = max + 1;

    mountParseTable();
  }

  private void mountParseTable() throws Exception {
    cellType = new CellType[numL][numC];
    parseTable = (List<String>[][])java.lang.reflect.Array.newInstance(
        List.class, numL, numC
    );

    for (int i = 0; i < numL; ++i)
      for (int j = 0; j < numC; ++j) {
        cellType[i][j] = CellType.Error;
        parseTable[i][j] = null;
      }

    for (Rule r : grammar) {
      String A = r.getLhs();
      int id_A = nonTerminalId.get(A);
      List<String> beta = r.getRhs();

      for (String a : ll.filterLambda(ll.sentenceFirst(beta))) {
        int id_a = terminalId.get(a);
        parseTable[id_A][id_a] = beta;
        cellType[id_A][id_a] = CellType.Rule;
      }

      if (ll.sentenceFirst(beta).contains(ll.lambda)) {
        for (String b : follow.get(A)) {
          int id_b = terminalId.get(b);
          parseTable[id_A][id_b] = beta;
          cellType[id_A][id_b] = CellType.Rule;
        }
      }
    }
  }

  public void parse() throws Exception {
    Stack<String> stack = new Stack<String>();
    
    stack.push("EOF");
    stack.push(ll.firstSymbol());

    while (!stack.empty()) {
      String A = stack.pop();

      if (terminals().contains(A)) {
        if (lookahead(A)) {
          match(A);
          System.out.println("Matched " + A);
        }
        else
          throw new ParseException(
            "Unexpected " + getLookaheadToken() +
            " was expecting " + A
          );
      }
      else if (A.equals(ll.lambda)) {
        continue;
      }
      else {
        int A_id = nonTerminalId.get(A);
        int a_id = getLookaheadToken().kind;

        if (cellType[A_id][a_id].equals(CellType.Rule)) {
          List<String> beta = parseTable[A_id][a_id];
          
          System.out.printf("Using: %s ::= %s\n",A,beta.toString());

          for (int i = beta.size()-1; i >= 0; --i)
            stack.push(beta.get(i));
        }
        else {
          throw new ParseException(
            "Unexpected " + getLookaheadToken() +
            " was expecting one of " + ll.filterLambda(first.get(A))
          );
        }
      }
    }
  }

  private boolean lookahead(String tok) throws ParseException {
    int tok_kind = terminalId.get(tok);
    return lex.getToken(1).kind == tok_kind;
  }

  private void match(String tok) throws ParseException {
    lex.getNextToken();
    int tok_kind = terminalId.get(tok);

    if (lex.token.kind != tok_kind)
      throw new ParseException("Parse error: "
           + "unexpected " + lex.token.image + ", expected "
           + ParserConstants.tokenImage[terminalId.get(tok)]
      );
  }

  private Token getNextToken() throws ParseException {
    return lex.getNextToken();
  }

  private Token getLookaheadToken() throws ParseException {
    return lex.getToken(1);
  }

  private Set<String> terminals() {
    return terminalId.keySet();
  }

  private Set<String> nonTerminals() {
    return nonTerminalId.keySet();
  }
 
  private LL1Grammar ll;
  private List<Rule> grammar;
  private minjavac.Parser lex;
  private Map<String,Set<String>> first, follow;
  private Map<String,Integer> terminalId, nonTerminalId;

  private int numL, numC;
  private CellType[][] cellType;
  private List<String>[][] parseTable;
}
