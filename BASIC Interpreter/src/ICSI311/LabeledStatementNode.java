package ICSI311;

import java.util.ArrayList;

//LabeledStatementNode class, contains a label and a statemtnNode
public class LabeledStatementNode extends StatementNode{
	private String label;
	private StatementNode statement;
	public LabeledStatementNode(String label, StatementNode statement)
	{
		this.label = label;
		this.statement = statement;
	}
	public String getLabel()
	{
		return this.label;
	}
	public StatementNode getStatement()
	{
		return this.statement;
	}
	@Override
	public String toString()
	{
		return "LABELSTATEMENT: " + label.toString() + " " + statement.toString();
	}
}
