package ICSI311;

import java.util.ArrayList;

//class for FunctionNode contains a list of Nodes that should be a StringNode, IntegerNode, of FloatNode 
//along with the function name
public class FunctionNode extends StatementNode{
	private ArrayList<Node> parameterList;
	private String functionName;
	public FunctionNode(String functionName, ArrayList<Node> parameterList)
	{
		this.functionName = functionName;
		this.parameterList = parameterList;
	}
	public ArrayList<Node> get()
	{
		return this.parameterList;
	}
	public String getName()
	{
		return this.functionName;
	}
	@Override
	public String toString()
	{
		return this.functionName + ": " + parameterList.toString();
	}
}
