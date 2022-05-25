package ICSI311;

import ICSI311.Token.TokenType;
//class for variable node contains an identifier token name
public class VariableNode extends Node{

	//variables
	private String variableName;
	//constructor
	VariableNode(String variableName)
	{
		this.variableName = variableName;
	}
	public String get()
	{
		return this.variableName;
	}
	@Override
	public String toString()
	{
		return "(Variable:" + this.variableName + ")";
	}

}
