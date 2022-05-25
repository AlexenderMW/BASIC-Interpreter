package ICSI311;

import ICSI311.Token.TokenType;
//node containing math operation and pointer to the left and right nodes
public class MathOpNode extends Node
{	
	//variables
	private TokenType mathOp;
	private Node left;
	private Node right;
	//constructor
	MathOpNode(Token.TokenType mathOp, Node left, Node right)
	{
		this.mathOp = mathOp;
		this.left = left;
		this.right = right;
	}
	public Node getLeft()
	{
		return this.left;
	}
	public Node getRight()
	{
		return right;
	}
	public String getOp()
	{
		return this.mathOp.name();
	}
	@Override
	public String toString()
	{
		return "MathOp(" + this.mathOp.name() + " LEFT->" + left.toString() + " RIGHT->" + right.toString() + ")";
	}

}
