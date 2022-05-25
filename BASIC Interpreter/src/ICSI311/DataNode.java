package ICSI311;

import java.util.ArrayList;
//class for DatNode contains a list of Nodes that should be a StringNode, IntegerNode, of FloatNode
public class DataNode extends StatementNode{
	private ArrayList<Node> toData;
	public DataNode(ArrayList<Node> toData)
	{
		this.toData = toData;
	}
	public ArrayList<Node> get()
	{
		return this.toData;
	}
	@Override
	public String toString()
	{
		return "DATA: " + toData.toString();
	}
}
