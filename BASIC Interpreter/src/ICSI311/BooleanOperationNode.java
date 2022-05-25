package ICSI311;
//Boolean operation node takes in a tokenType that represents the Boolean operator
public class BooleanOperationNode extends Node
{
	public BooleanOperationNode(Token.TokenType operator)
	{
		this.operator = operator;
	}
	private Token.TokenType operator;
	public String getValue() {return this.operator.name();}
	@Override
	public String toString()
	{
		return this.operator.name();
	}
}
