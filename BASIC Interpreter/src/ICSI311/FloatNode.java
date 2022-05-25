package ICSI311;
//node containing a float number
public class FloatNode extends Node
{
	public FloatNode(String value)
	{
		this.value = Float.parseFloat(value);
	}
	private float value;
	public float getValue() {return this.value;}
	@Override
	public String toString()
	{
		return "float" + String.valueOf(this.value);
	}
}
