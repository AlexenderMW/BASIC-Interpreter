package ICSI311;

public class Token {
	//possible tokentypes
	public enum TokenType
	{
		MINUS, PLUS, TIMES, DIVIDE, NUMBER, FLOAT, EndOfLine, LESSTHAN, MORETHAN, LESSOREQUALS, MOREOREQUALS, 
		EQUALS, NOTEQUALS, STRING, LPAREN, RPAREN, IDENTIFIER, LABEL, PRINT, COMMA, DATA, READ, INPUT, FOR, NEXT,
		RETURN, STEP, GOSUB, TO, IF, THEN, RANDOM, LEFT$, RIGHT$, MID$, NUM$, VAL$, VAL
	}
	public Token(TokenType t, String s)
	{
		this.type = t;
		this.value = s;
	}
	public Token(TokenType t)
	{
		this.type = t;
	}
	private TokenType type;
	private String value;
	public TokenType getTokenType()
	{
		return this.type;
	}
	public String getValue() 
	{
		return this.value;
	}
	@Override
	public String toString()
	{
		if(value == null)
			return type.name() + " ";
		else
			return type.name() + " (" + value + ") ";
	}
}
