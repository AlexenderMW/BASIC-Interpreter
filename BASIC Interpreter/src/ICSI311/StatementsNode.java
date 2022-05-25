package ICSI311;

import java.util.ArrayList;
//class for statemesnt node contains a list of StatementNode's
public class StatementsNode extends StatementNode{

	private ArrayList<StatementNode> statementsList;
	public StatementsNode(ArrayList<StatementNode> statements)
	{
		this.statementsList = statements;
	}
	public ArrayList<StatementNode> get()
	{
		return this.statementsList;
	}
	@Override
	public String toString()
	{
		return statementsList.toString();
	}
}
