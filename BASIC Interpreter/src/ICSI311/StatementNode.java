package ICSI311;
//class for other class to extend
public class StatementNode extends Node{

	private StatementNode next;
	public StatementNode() {}
	public void setStatement(StatementNode statement)
	{
		this.next = statement;
	}
	public StatementNode getStatement()
	{
		return this.next;
	}
	@Override 
	public String toString()
	{
		return "";
	}
}
