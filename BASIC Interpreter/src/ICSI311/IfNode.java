package ICSI311;

//class for IfNode, contains a leftExpression, boolean operator, and right expression
public class IfNode extends StatementNode{
	private Node leftExpression;
	private Node rightExpression;
	private BooleanOperationNode operator;
	private String label;
	public IfNode(Node leftExpression, BooleanOperationNode operator, Node rightExpression, String label)
	{
		this.leftExpression = leftExpression;
		this.operator = operator;
		this.rightExpression = rightExpression;
		this.label = label + ":";
	}
	public Node getLeftExpression()
	{
		return this.leftExpression;
	}
	public BooleanOperationNode getOperator()
	{
		return this.operator;
	}
	public Node getRightExpression()
	{
		return this.rightExpression;
	}
	public String getLabel()
	{
		return this.label;
	}
	@Override
	public String toString()
	{
		return "IF( " + this.leftExpression.toString() 
		+ " " + this.operator.toString() + " " + this.rightExpression.toString() + ")";
	}
}
