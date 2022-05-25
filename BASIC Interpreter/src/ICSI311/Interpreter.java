package ICSI311;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Interpreter {

	//Variable Storage
	private HashMap<String, Integer> IntegerMap = new HashMap<>(); //contains variables that are integers
	private HashMap<String, Float> FloatMap = new HashMap<>();//contains variables that are floats
	private HashMap<String, String> StringMap = new HashMap<>();//contains variables that are strings
	private HashMap<String, Node> LabelMap = new HashMap<>();//contains LabeledStatements and their title
	private StatementsNode list;//used to input a full ASTtree
	private ArrayList<Node> dataList = new ArrayList<>();//contains data from DATA nodes
	private Stack<VariableNode> functionStack = new Stack<VariableNode>();
	//constructor
	public Interpreter(StatementsNode list)
	{
		this.list = list;
	}
	//evaluate each statement
	public void interpret(StatementNode statement) throws Exception
	{
		Stack<Node> nodeStack = new Stack<Node>();
		do
		{
			//evaluate ReadNode
			if(statement instanceof ReadNode)
			{
				ReadNode readNode = (ReadNode) statement;
				//iterating through and checking to make sure DATA matches each variable type
				for(int i = 0; i < readNode.get().size(); ++i)
				{
					VariableNode variable = (VariableNode) readNode.get().get(i);
					//string type
					if(variable.get().endsWith("$") && dataList.get(i) instanceof StringNode)
					{
						StringNode value = (StringNode) dataList.get(i);
						StringMap.put(variable.get(), value.getValue());
					}
					//float type
					else if(variable.get().endsWith("%") && dataList.get(i) instanceof FloatNode)
					{
						FloatNode value = (FloatNode) dataList.get(i);
						FloatMap.put(variable.get(), value.getValue());
					}
					//int type
					else if(dataList.get(i) instanceof IntegerNode)
					{
						IntegerNode value = (IntegerNode) dataList.get(i);
						IntegerMap.put(variable.get(), value.getValue());
					}
					else
						throw new Exception("cannot read in different types");
				}
				statement = statement.getStatement();
			}
			//evaluate assignmentNode
			else if(statement instanceof AssignmentNode)
			{
				//checking to make sure variable is placed into correct hashmap
				if(((AssignmentNode)statement).getVariable().get().endsWith("$"))
				{
					if(((AssignmentNode)statement).getValue() instanceof FunctionNode)
					{
						functionStack.push(((AssignmentNode)statement).getVariable());
						evaluateFunction(((FunctionNode)((AssignmentNode)statement).getValue()));
					}
					else
					{
						StringMap.put(((AssignmentNode)statement).getVariable().get(), 
							((StringNode) ((AssignmentNode)statement).getValue()).getValue());
					}
				}
				if(((AssignmentNode)statement).getVariable().get().endsWith("%"))
				{
					if(((AssignmentNode)statement).getValue() instanceof FunctionNode)
					{
						functionStack.push(((AssignmentNode)statement).getVariable());
						evaluateFunction(((FunctionNode)((AssignmentNode)statement).getValue()));
					}
					else
						FloatMap.put(((AssignmentNode)statement).getVariable().get(), evaluateFloatMathOp(((AssignmentNode)statement).getValue()));
				}
				else
				{
					if(((AssignmentNode)statement).getValue() instanceof FunctionNode)
					{
						functionStack.push(((AssignmentNode)statement).getVariable());
						evaluateFunction(((FunctionNode)((AssignmentNode)statement).getValue()));
					}
					else
						IntegerMap.put(((AssignmentNode)statement).getVariable().get(), evaluateIntMathOp(((AssignmentNode)statement).getValue()));
				}
				statement = statement.getStatement();
			}
			//interpting a INPUT statement
			else if(statement instanceof InputNode)
			{
				//casting to InputNode
				InputNode inputNode = (InputNode) statement;
				Scanner scanner = new Scanner(System.in);
				System.out.println(inputNode.getInput());
				//checking to ensure variable to store data in is place in proper hashmap should probably make this a function
				for(int i = 0; i < inputNode.getList().size(); ++i)
				{
					VariableNode variable = (VariableNode) inputNode.getList().get(i);
					if(variable.get().endsWith("$"))
					{
						String string = scanner.nextLine();
						StringMap.put(variable.get(), string);
					}
					else if(variable.get().endsWith("%"))
					{
						String string = scanner.nextLine();
						FloatMap.put(variable.get(), Float.parseFloat(string));
					}
					else
					{
						String string = scanner.nextLine();
						IntegerMap.put(variable.get(), Integer.parseInt(string));
					}
				}
				statement = statement.getStatement();
			}
			//interpreting a PRINT statemnt
			else if(statement instanceof PrintNode)
			{
				System.out.println("Printing...");
				//casting to PrintNode
				PrintNode printNode = (PrintNode) statement;
				for(int i = 0; i < printNode.get().size(); ++i)
				{
					//if node to be printed is a variable check to see if it can be found in variable storage
					if(printNode.get().get(i) instanceof VariableNode)
					{
						if(StringMap.containsKey(((VariableNode)printNode.get().get(i)).get()))
							System.out.println(StringMap.get(((VariableNode)printNode.get().get(i)).get()));
						if(IntegerMap.containsKey(((VariableNode)printNode.get().get(i)).get()))
							System.out.println(IntegerMap.get(((VariableNode)printNode.get().get(i)).get()));	
						if(FloatMap.containsKey(((VariableNode)printNode.get().get(i)).get()))
							System.out.println(FloatMap.get(((VariableNode)printNode.get().get(i)).get()));
	
					}	
					else
						System.out.println(printNode.get().get(i));
				}
				statement = statement.getStatement();
			}
			//interprets if statement
			else if(statement instanceof IfNode)
			{
				//if boolean is true jump to label found after THEN
				if(evaluateBoolean(((IfNode)statement).getLeftExpression(), ((IfNode)statement).getOperator(), ((IfNode)statement).getRightExpression()))
					statement = (StatementNode)LabelMap.get(((IfNode)statement).getLabel());
			}
			//evaluates GosubNode
			else if(statement instanceof GosubNode)
			{
				//pushing next statement onto stack to reference later
				nodeStack.push(statement.getStatement());
				//going to label
				statement = (StatementNode)LabelMap.get(((GosubNode)statement).getValue());
			}
			//evaluates ReturnNode
			else if(statement instanceof ReturnNode)
			{
				//going to statement right after Gosub
				if(nodeStack.isEmpty())
					statement = statement.getStatement();
				else
					statement = (StatementNode)nodeStack.pop();
			}
			//evaluate For
			else if(statement instanceof ForNode)
			{
				//checking if variable is already present, if not sets to initialization value, if so increments by STEP value
				if(IntegerMap.containsKey(((ForNode)statement).getVariable()))
					IntegerMap.put(((ForNode)statement).getVariable(), ((ForNode)statement).getInitializer() + ((ForNode)statement).getIncrement());
				else 
					IntegerMap.put(((ForNode)statement).getVariable(), ((ForNode)statement).getInitializer());
				//checking if value at TO has been reached, if so sets statement to statement after Next
				if(((ForNode)statement).getComparison() >= IntegerMap.get(((ForNode)statement).getVariable()))
					statement = ((ForNode)statement).getNext();
				else 
					statement = statement.getStatement();
			}
			//evaluates next Node if found sets statement to FOR node
			else if(statement instanceof NextNode)
			{
				statement = ((NextNode)statement).getFor();
			}
		}
		while(statement != null);
	}
	//evaluates Functions(statement instanceof FunctionNode)
	private void evaluateFunction(FunctionNode function) throws Exception
	{
		if(function.getName().equals("RANDOM"))
		{
			Random rand = new Random();
			IntegerMap.put(functionStack.pop().get(), rand.nextInt(1000000));
		}
		if(function.getName().equals("LEFT$"))
		{
			if(!(function.get().get(0) instanceof StringNode))
				throw new Exception("Invalid input for LEFT$");
			if(!(function.get().get(1) instanceof IntegerNode))
				throw new Exception("Invalid input for LEFT$");
			//printing lefmost N characters
			StringMap.put(functionStack.pop().get(), (((StringNode)function.get().get(0)).getValue().substring(0 ,
							((IntegerNode)function.get().get(1)).getValue())));
		}
		if(function.getName().equals("RIGHT$"))
		{
			if(!(function.get().get(0) instanceof StringNode))
				throw new Exception("Invalid input for RIGHT$");
			if(!(function.get().get(1) instanceof IntegerNode))
				throw new Exception("Invalid input for RIGHT$");
			//printing rightmost N characters
			StringMap.put(functionStack.pop().get(), (((StringNode)function.get().get(0)).getValue().substring(
					((StringNode)function.get().get(0)).getValue().length() -  
					((IntegerNode)function.get().get(1)).getValue(), 
					((IntegerNode)function.get().get(1)).getValue())));
		}
		if(function.getName().equals("MID$"))
		{
			if(!(function.get().get(0) instanceof StringNode))
				throw new Exception("Invalid input for MID$");
			if(!(function.get().get(1) instanceof IntegerNode))
				throw new Exception("Invalid input for MID$");
			if(!(function.get().get(2) instanceof IntegerNode))
				throw new Exception("Invalid input for MID$");
			//printing String from first int to second int
			StringMap.put(functionStack.pop().get(), (((StringNode)function.get().get(0)).getValue().substring(  
					((IntegerNode)function.get().get(1)).getValue(), 
					((IntegerNode)function.get().get(2)).getValue())));
		}
		if(function.getName().equals("NUM$"))
		{
			//outputting the number as a string
			if(function.get().get(0) instanceof IntegerNode)
				StringMap.put(functionStack.pop().get(), String.valueOf(((IntegerNode)function.get().get(0)).getValue()));
			else if(function.get().get(0) instanceof FloatNode)
				StringMap.put(functionStack.pop().get(), String.valueOf(((FloatNode)function.get().get(0)).getValue()));
			else
				throw new Exception("Invalid input for NUM$");
		}
		//outputs string as an int
		if(function.getName().equals("VAL"))
		{
			if(!(function.get().get(0) instanceof StringNode))
				throw new Exception("Invalid input for VAL");
			IntegerMap.put(functionStack.pop().get(), Integer.parseInt(((StringNode)function.get().get(0)).getValue()));
		}
		//outputs string as a float
		if(function.getName().equals("VAL%"))
		{
			if(!(function.get().get(0) instanceof StringNode))
				throw new Exception("Invalid input for VAL%");
			FloatMap.put(functionStack.pop().get(), Float.parseFloat(((StringNode)function.get().get(0)).getValue()));
		}
	}
	//evaluates a Boolean 
	private boolean evaluateBoolean(Node leftValue, BooleanOperationNode operation, Node rightValue) throws Exception
	{
		if(operation.getValue().equals("LESSTHAN"))
			return evaluateIntMathOp(leftValue) < evaluateIntMathOp(rightValue);
		else if(operation.getValue().equals("MORETHAN"))
			return evaluateIntMathOp(leftValue) > evaluateIntMathOp(rightValue);
		else if(operation.getValue().equals("EQUALS"))
			return evaluateIntMathOp(leftValue) == evaluateIntMathOp(rightValue);
		else if(operation.getValue().equals("LESSOREQUALS"))
			return evaluateIntMathOp(leftValue) <= evaluateIntMathOp(rightValue);
		else if(operation.getValue().equals("MOREOREQUALS"))
			return evaluateIntMathOp(leftValue) >= evaluateIntMathOp(rightValue);
		else
			throw new Exception("error when evaluating boolean");
	}
	//evaluates an integer
	private int evaluateIntMathOp(Node operation) throws Exception
	{
		if(operation instanceof IntegerNode)
			return ((IntegerNode) operation).getValue();
		if(operation instanceof VariableNode)
			return IntegerMap.get(((VariableNode) operation).get());
		//if the node is a mathOp then recurses left and right of the expression and returns result
		if(operation instanceof MathOpNode)
		{
			if(((MathOpNode)operation).getOp() == "PLUS")
				return evaluateIntMathOp(((MathOpNode)operation).getLeft()) 
						+ evaluateIntMathOp(((MathOpNode)operation).getRight());
			if(((MathOpNode)operation).getOp() == "MINUS")
				return evaluateIntMathOp(((MathOpNode)operation).getLeft()) 
						- evaluateIntMathOp(((MathOpNode)operation).getRight());
			if(((MathOpNode)operation).getOp() == "TIMES")
				return evaluateIntMathOp(((MathOpNode)operation).getLeft()) 
						* evaluateIntMathOp(((MathOpNode)operation).getRight());
			if(((MathOpNode)operation).getOp() == "DIVIDES")
				return evaluateIntMathOp(((MathOpNode)operation).getLeft()) 
						/ evaluateIntMathOp(((MathOpNode)operation).getRight());
		}
		else
		{
			throw new Exception("Error with evaluation");
		}
		return 0;
	}
	//evaluates a float number
	private float evaluateFloatMathOp(Node operation) throws Exception
	{
		if(operation instanceof FloatNode)
			return ((FloatNode) operation).getValue();
		if(operation instanceof VariableNode)
			return FloatMap.get(((VariableNode) operation).get());
		//if MathOp recurse to left and right of expression returns result
		if(operation instanceof MathOpNode)
		{
			if(((MathOpNode)operation).getOp() == "PLUS")
				return evaluateFloatMathOp(((MathOpNode)operation).getLeft()) 
						+ evaluateFloatMathOp(((MathOpNode)operation).getRight());
			if(((MathOpNode)operation).getOp() == "MINUS")
				return evaluateFloatMathOp(((MathOpNode)operation).getLeft()) 
						- evaluateFloatMathOp(((MathOpNode)operation).getRight());
			if(((MathOpNode)operation).getOp() == "TIMES")
				return evaluateFloatMathOp(((MathOpNode)operation).getLeft()) 
						* evaluateFloatMathOp(((MathOpNode)operation).getRight());
			if(((MathOpNode)operation).getOp() == "DIVIDES")
				return evaluateFloatMathOp(((MathOpNode)operation).getLeft()) 
						/ evaluateFloatMathOp(((MathOpNode)operation).getRight());
		}
		else
		{
			throw new Exception("Error with evaluation");
		}
		//required as a number must be returned, but an error should be thrown before then
		return 0;
	}
	public void initialize()
	{
		walkLabledStatements();
		walkDataStatements();
		walkForNextStatements();
		walkStatements();
		try {
			interpret(this.list.get().get(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void walkStatements()
	{
		//setting the next statement for each statement
		for(int i = 0; i < list.get().size(); i++)
		{
			if((i + 1) == list.get().size())
				list.get().get(i).setStatement(null);
			else
				list.get().get(i).setStatement(list.get().get(i + 1));
		}
	}
	private void walkLabledStatements()
	{
		//iterating through tree labeled statements, saving in hashmap and replacing with child node
		for(int i = 0; i < list.get().size(); i++)
		{
			if(list.get().get(i) instanceof LabeledStatementNode)
			{
				LabeledStatementNode statement = (LabeledStatementNode) list.get().get(i);
				LabelMap.put(statement.getLabel(), statement.getStatement());
				list.get().set(i, statement.getStatement());
			}
		}
	}
	private void walkDataStatements()
	{
		//iterating through tree to find DataNode, saving data when found and deleting node
		for(int i = 0; i < list.get().size(); i++)
		{
			if(list.get().get(i) instanceof DataNode)
			{
				//saving data and removing the node
				DataNode statement = (DataNode) list.get().get(i);
				for(int k = 0; k < statement.get().size(); ++k)
				{
					dataList.add(statement.get().get(k));
				}
				list.get().remove(i);
				--i;
			}
		}
	}
	//member function to walk through FOR statements and find matching NEXT
	private void walkForNextStatements()
	{
		//Stack structures to in case of FOR within FOR loops
		Stack<Integer> indexStack = new Stack<Integer>();
		//iterating through
		for(int i = 0; i < list.get().size(); i++)
		{
			//if node is a FOR or NEXT proceed
			if(list.get().get(i) instanceof ForNode || list.get().get(i) instanceof NextNode)
			{
				//if the stack containing FOR statements is not empty proceed
				if(!indexStack.isEmpty())
				{
					//if a NEXT node is found, FOR statement is popped and the next statements are assigned
					if(list.get().get(i) instanceof NextNode)
					{
						//checking to make sure if there is a statement after NEXT
						if((i + 1) < list.get().size())
						{
							//setting next node for NEXT
							((NextNode)list.get().get(i)).setFor(list.get().get(indexStack.peek()));
							//setting next node for FOR
							((ForNode)list.get().get(indexStack.peek())).setNext(list.get().get(i + 1));
							//popping indexStack
							indexStack.pop();
						}
						else
						{
							//setting next node for NEXT
							((NextNode)list.get().get(i)).setFor(list.get().get(indexStack.peek()));
							//setting next node for FOR
							((ForNode)list.get().get(indexStack.peek())).setNext(null);
							//popping indexStack
							indexStack.pop();
						}
					}
					//if statement is FOR again
					else
					{
						indexStack.push(i);
					}
				}
				//if indexStack is empty
				else
				{
					indexStack.push(i);
				}
			}
		}
	}
	//used for debugging
	@Override
	public String toString()
	{
		String interpreter = "LOOKING AT DATA STORED\n---------------------------------\n";
		for(int i = 0; i < list.get().size(); ++i)
		{
			if(list.get().get(i).getStatement() != null)
			{
				interpreter += list.get().get(i).toString() + " points to the next statement->" 
							+ list.get().get(i).getStatement().toString() + "\n";
			}	
			else
			{
				interpreter += list.get().get(i).toString() + " points to the next statement->" 
						+ "null" + "\n";
			}
		}
		interpreter += "data currently stored: " + dataList.toString() + "\n";
		interpreter += "integers currently stored: " + IntegerMap.toString() + "\n";
		interpreter += "floats currently stored: " + FloatMap.toString() + "\n";
		interpreter += "strings currently stored: " + StringMap.toString() + "\n";
		interpreter += "labels currently stored: " + LabelMap.toString() + "\n";
		return interpreter;
	}
}
