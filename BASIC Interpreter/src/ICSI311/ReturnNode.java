package ICSI311;
//ReturnNode class contains nothing but modifies toString
public class ReturnNode extends StatementNode{
	public ReturnNode()
	{
	}
	public String getValue()
	{
		return "RETURN";
	}
	@Override
	public String toString()
	{
		return "RETURN";
	}
}
