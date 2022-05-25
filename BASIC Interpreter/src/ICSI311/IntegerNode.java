package ICSI311;
//node containing an integer value
public class IntegerNode extends Node{

	public IntegerNode(String value)
	{
		this.value = Integer.parseInt(value);
	}
	private int value;
	public int getValue() {return this.value;}
	@Override
	public String toString()
	{
	 return "int" + String.valueOf(this.value);
	}
}
