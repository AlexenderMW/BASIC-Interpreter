package ICSI311;
//NextNode class contains nothing but modifies toString
public class NextNode extends StatementNode{
	private VariableNode variable;
	private StatementNode forStatement;
	public NextNode(VariableNode variable)
	{
		this.variable = variable;
	}
	public String getValue()
	{
		return this.variable.toString();
	}
	public void setFor(StatementNode forStatement)
	{
		this.forStatement = forStatement;
	}
	public StatementNode getFor()
	{
		return this.forStatement;
	}
	@Override
	public String toString()
	{
		return "NEXT: " + this.variable.toString();
	}
}