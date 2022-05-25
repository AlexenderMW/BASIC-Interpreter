package ICSI311;
//ForNode class contains a value to increment
public class ForNode extends StatementNode{
	VariableNode variable;
	IntegerNode increment;
	IntegerNode initializer;
	IntegerNode comparison;	
	StatementNode nextStatement;
	public ForNode(VariableNode variable, IntegerNode initializer, IntegerNode comparison, IntegerNode increment)
	{
		this.variable = variable;
		this.increment = increment;
		this.initializer = initializer;
		this.comparison = comparison;
	}
	public String getVariable()
	{
		return this.variable.toString();
	}
	public int getInitializer()
	{
		return this.initializer.getValue();
	}
	public int getComparison()
	{
		return this.comparison.getValue();
	}
	public int getIncrement()
	{
		return this.increment.getValue();
	}
	public void setNext(StatementNode next)
	{
		this.nextStatement = next;
	}
	public StatementNode getNext()
	{
		return this.nextStatement;
	}
	@Override
	public String toString()
	{
		return "FOR: [" + this.variable.toString() + " = " + this.initializer.toString() + "; " + " TO: " 
			   + this.comparison.toString() + "; " + "STEP: " + increment.toString() + "]";
	}
}
