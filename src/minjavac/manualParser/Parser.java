package minjavac.manualParser;

import java.io.InputStream;
import minjavac.Token;
import minjavac.ParseException;
import minjavac.ParserConstants;
import minjavac.SimpleCharStream;
import minjavac.ParserTokenManager;

public class Parser implements ParserConstants {
  public static void main (String[] args) throws Exception {
    Parser parser = null;

    if (args.length == 1) {
        parser = new Parser(new java.io.FileInputStream(args[0]));
    }
    else if (args.length == 0) {
      parser = new Parser(System.in);
    }
    else {
      System.out.println("Usage: Parser < input filename or Parser input filename.");
      return;
    }

    try {
      parser.Goal();
    }
    catch (ParseException e) {
      System.out.println("Exiting.");
      throw e;
    }

    System.out.println("Input program is syntatically correct.");
  }

  public Parser(InputStream input) {
    new ParserTokenManager(new SimpleCharStream(input));
    advance();
  }

  private void advance() {
    token = ParserTokenManager.getNextToken();
  }

  private boolean lookahead(int tok) {
    return token.kind == tok;
  }

  private void match() throws ParseException {
    match(-1);
  }

  private void match(int tok) throws ParseException {
    if (lookahead(tok) || tok == -1) {
      advance();
    }
    else {
      throw new ParseException(
        "Unexpected " + token.image +
        " at line " + token.beginLine +
        ", column " + token.beginColumn
      );
    }
  }

  private void Goal() throws ParseException {
    MainClass();

    while (lookahead(CLASS)) {
      ClassDeclaration();
    }

    match(EOF);
  }

  private void MainClass() throws ParseException {
    match(CLASS);
    match(ID);
    match(LBRACE);
    match(PUBLIC);
    match(STATIC);
    match(VOID);
    match(MAIN);
    match(LPARENS);
    match(STRING);
    match(LBRACKET);
    match(RBRACKET);
    match(ID);
    match(RPARENS);
    match(LBRACE);
    PrintStatement();
    match(RBRACE);
    match(RBRACE);
  }

  private void ClassDeclaration() throws ParseException {
    match(CLASS);
    match(ID);
    ClassDeclarationTail();
  }
  
  private void ClassDeclarationTail() throws ParseException {
    if (lookahead(EXTENDS)) {
      match();
      match(ID);
    }
    
    match(LBRACE);
    
    while (!lookahead(PUBLIC)) {
      VarDeclaration();
    }
    
    while (lookahead(PUBLIC)) {
      MethodDeclaration();
    }
    
    match(RBRACE);
  }

  private void VarDeclaration() throws ParseException {
    Type();
    match(ID);
    match(SEMI);
  }

  private void MethodDeclaration() throws ParseException {
    match(PUBLIC);
    Type();
    match(ID);
    match(LPARENS);

    if (!lookahead(RPARENS)) {
        FormalParameterList();
    }

    match(RPARENS);
    match(LBRACE);

    if (!lookahead(RETURN)) {
      VarDeclarationOrStatement();
    }

    match(RETURN);
    Expression();
    match(SEMI);
    match(RBRACE);
  }

  private void FormalParameterList() throws ParseException {
    FormalParameter();

    while (lookahead(COMMA)) {
      match();
      FormalParameter();
    }
  }

  private void FormalParameter() throws ParseException {
    Type();
    match(ID);
  }

  private void Type() throws ParseException {
    if (lookahead(INT)) {
      ArrayOrIntegerType();
    }
    else if (lookahead(BOOLEAN)) {
      BooleanType();
    }
    else {
      match(ID);
    }
  }

  private void ArrayOrIntegerType() throws ParseException {
    match(INT);

    if (lookahead(LBRACKET)) {
      match();
      match(RBRACKET);
    }
  }

  private void BooleanType() throws ParseException {
    match(BOOLEAN);
  }

  private void VarDeclarationOrStatement() throws ParseException {
    if (lookahead(INT)) {
      ArrayOrIntegerType();
      VarDeclarationTail();
    }
    else if (lookahead(BOOLEAN)) {
      BooleanType();
      VarDeclarationTail();
    }
    else if (lookahead(LBRACE)) {
      Block();
      Statements();
    }
    else if (lookahead(IF)) {
      IfStatement();
      Statements();
    }
    else if (lookahead(WHILE)) {
      WhileStatement();
      Statements();
    }
    else if (lookahead(PRINT)) {
      PrintStatement();
      Statements();
    }
    else {
      match(ID);
      IdentifierDeclarationOrStatement();
    }
  }

  private void VarDeclarationTail() throws ParseException {
    match(ID);
    match(SEMI);
    VarDeclarationOrStatement();
  }

  private void IdentifierDeclarationOrStatement() throws ParseException {
    if (lookahead(ID)) {
      match();
      match(SEMI);
      VarDeclarationOrStatement();
    }
    else {
      IdentifierStatementTail();
      Statements();
    }
  }

  private void Statements() throws ParseException {
    while (lookahead(LBRACE)
        || lookahead(IF)
        || lookahead(WHILE)
        || lookahead(PRINT)
        || lookahead(ID)) {
      Statement();
    }
  }

  private void Statement() throws ParseException {
    if (lookahead(LBRACE)) {
      Block();
    }
    else if (lookahead(IF)) {
      IfStatement();
    }
    else if (lookahead(WHILE)) {
      WhileStatement();
    }
    else if (lookahead(PRINT)) {
      PrintStatement();
    }
    else {
      match(ID);
      IdentifierStatementTail();
    }
  }

  private void Block() throws ParseException {
    match(LBRACE);
    Statements();
    match(RBRACE);
  }

  private void IfStatement() throws ParseException {
    match(IF);
    match(LPARENS);
    Expression();
    match(RPARENS);
    Statement();

    if (lookahead(ELSE)) {
      match();
      Statement();
    }
  }

  private void WhileStatement() throws ParseException {
    match(WHILE);
    match(LPARENS);
    Expression();
    match(RPARENS);
    Statement();
  }

  private void PrintStatement() throws ParseException {
    match(PRINT);
    match(LPARENS);
    Expression();
    match(RPARENS);
    match(SEMI);
  }

  private void IdentifierStatementTail() throws ParseException {
    if (lookahead(LBRACKET)) {
      match();
      Expression();
      match(RBRACKET);
    }

    match(ASSIGN);
    Expression();
    match(SEMI);
  }

  private void Expression() throws ParseException {
    AndExpression();
  }

  private void AndExpression() throws ParseException {
    LessThanExpression();

    while (lookahead(AND)) {
      match();
      LessThanExpression();
    }
  }

  private void LessThanExpression() throws ParseException {
    AdditiveExpression();

    while (lookahead(LESS)) {
      match();
      AdditiveExpression();
    }
  }

  private void AdditiveExpression() throws ParseException {
    TimesExpression();

    while (lookahead(ADD) || lookahead(SUB)) {
      match();
      TimesExpression();
    }
  }

  private void TimesExpression() throws ParseException {
    PrefixExpression();

    while (lookahead(MULT)) {
      match();
      PrefixExpression();
    }
  }

  private void PrefixExpression() throws ParseException {
    while (lookahead(NOT)) {
      match();
    }

    PostFixExpression();
  }

  private void PostFixExpression() throws ParseException {
    PrimaryExpression();

    while (true) {
      if (lookahead(LBRACKET)) {
        ArrayLookup();
      }
      else if (lookahead(DOT)) {
        ArrayLengthOrMethodCall();
      }
      else {
        break;
      }
    }
  }

  private void ArrayLookup() throws ParseException {
    match(LBRACKET);
    Expression();
    match(RBRACKET);
  }

  private void ArrayLengthOrMethodCall() throws ParseException {
    match(DOT);
    ArrayLengthOrMethodCallTail();
  }

  private void ArrayLengthOrMethodCallTail() throws ParseException {
    if (lookahead(LENGTH)) {
      match();
    }
    else {
      match(ID);
      match(LPARENS);

      if (!lookahead(RPARENS)) {
        ExpressionList();
      }

      match(RPARENS);
    }
  }

  private void PrimaryExpression() throws ParseException {
    if (lookahead(NUM)
     || lookahead(TRUE)
     || lookahead(FALSE)
     || lookahead(ID)
     || lookahead(THIS)) {
      match();
    }
    else if (lookahead(LPARENS)) {
      match();
      Expression();
      match(RPARENS);
    }
    else {
      match(NEW);
      ArrayOrObjectAllocation();
    }
  }

  private void ArrayOrObjectAllocation() throws ParseException {
    if (lookahead(INT)) {
      match();
      match(LBRACKET);
      Expression();
      match(RBRACKET);
    }
    else {
      match(ID);
      match(LPARENS);
      match(RPARENS);
    }
  }

  private void ExpressionList() throws ParseException {
    Expression();

    while (lookahead(COMMA)) {
      match();
      Expression();
    }
  }

  private Token token;
}
