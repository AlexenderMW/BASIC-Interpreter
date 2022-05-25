package ICSI311;

import java.util.ArrayList;
//class for Read node contains a list of variables 
public class ReadNode extends StatementNode{
	private ArrayList<Node> toRead;
	public ReadNode(ArrayList<Node> toRead)
	{
		this.toRead = toRead;
	}
	public ArrayList<Node> get()
	{
		return this.toRead;
	}
	@Override
	public String toString()
	{
		return "READ: " + toRead.toString();
	}
}
