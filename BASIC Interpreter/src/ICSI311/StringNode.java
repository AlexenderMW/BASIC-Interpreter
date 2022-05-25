package ICSI311;
//class for String node contains a constant string
public class StringNode extends Node
{
	public StringNode(String value)
	{
		this.value = value;
	}
	private String value;
	public String getValue() {return this.value;}
	@Override
	public String toString()
	{
		return "''" + value + "''";
	}
}
