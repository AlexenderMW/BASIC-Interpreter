package ICSI311;

import java.util.ArrayList;
//class for DatNode contains a string or variable name and list of Nodes that should be a Variables
public class InputNode extends StatementNode{
	private ArrayList<Node> toInput;
	private String inputMessage;
	public InputNode(String inputMessage, ArrayList<Node> toInput)
	{
		this.inputMessage = inputMessage;
		this.toInput = toInput;
	}
	public ArrayList<Node> getList()
	{
		return this.toInput;
	}
	public String getInput()
	{
		return this.inputMessage;
	}
	@Override
	public String toString()
	{
		return "INPUT: " + this.inputMessage + "(variables:" + this.toInput.toString() + ")";
	}
}
