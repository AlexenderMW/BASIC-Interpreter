package ICSI311;

import java.util.ArrayList;
//Print node class contains a list of expressions and string nodes to print
public class PrintNode extends StatementNode{
	private ArrayList<Node> toPrint;
	public PrintNode(ArrayList<Node> toPrint)
	{
		this.toPrint = toPrint;
	}
	public ArrayList<Node> get()
	{
		return this.toPrint;
	}
	@Override
	public String toString()
	{
		return "PRINT: " + toPrint.toString();
	}
}
