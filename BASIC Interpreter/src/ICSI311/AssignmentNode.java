package ICSI311;

import ICSI311.Token.TokenType;
//Class for Assignment node contains a variable node and the value its assigned
public class AssignmentNode extends StatementNode{
	//variables
	private Node value;
	private VariableNode variable;
	//constructor
	AssignmentNode(VariableNode variable, Node value)
	{
		this.variable = variable;
		this.value = value;
	}
	public VariableNode getVariable()
	{
		return this.variable;
	}
	public Node getValue()
	{
		return this.value;
	}
	@Override
	public String toString()
	{
		return "ASSIGNMENT(" + this.variable.toString() + " = " + this.value.toString() + ")";
	}

}
