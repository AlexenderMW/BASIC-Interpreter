package ICSI311;

//GosubNode contains a label that will be used to jump to later
public class GosubNode extends StatementNode{
	private String identifier;
	public GosubNode(String identifier)
	{
		this.identifier = identifier + ":";
	}
	public String getValue()
	{
		return this.identifier;
	}
	@Override
	public String toString()
	{
		return "GOSUB: " + identifier.toString();
	}
}
