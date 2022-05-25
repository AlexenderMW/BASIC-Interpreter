package ICSI311;
import java.util.ArrayList;
public class Parser {
	//constructor
	public Parser(ArrayList<Token> list)
	{
		this.lexeme = list;
	}
	private ArrayList<Token> lexeme;
	//start the parse
	public Node parse() throws Exception
	{
		Node target = parseStatements();
		if(target == null)
			return null;
		else
			return target;
	}
	//parse Statements grammer STATEMENT list
	private Node parseStatements() throws Exception
	{
		ArrayList<StatementNode> statementList = new ArrayList<>();
		StatementNode statement;
		do
		{
			statement = parseStatement();
			if(statement != null)
				statementList.add(statement);
		}while(statement != null);
		if(!statementList.isEmpty())
			return new StatementsNode(statementList);
		else
			return null;
	}
	//parse STATEMENT grammer PRINT || ASSIGNMENT || READ || INPUT ||DATA
	private StatementNode parseStatement() throws Exception
	{
		if(lexeme.isEmpty())
			return null;
		LabeledStatementNode labeledStatement = parseLabeledStatement();
		if(labeledStatement != null)
			return labeledStatement;
		PrintNode print = parsePrint();
		if(print != null)
			return print;
		ReadNode read = parseRead();
		if(read != null)
			return read;
		DataNode data = parseData();
		if(data != null)
			return data;
		InputNode input = parseInput();
		if(input != null)
			return input;
		AssignmentNode assignment = parseAssignment();
		if(assignment != null)
			return assignment;
		ForNode forNode = parseFor();
		if(forNode != null)
			return forNode;
		NextNode next = parseNext();
		if(next != null)
			return next;
		GosubNode gosub = parseGosub();
		if(gosub != null)
			return gosub;	
		ReturnNode returnStatement = parseReturn();
		if(returnStatement != null)
			return returnStatement;
		IfNode ifNode = parseIf();
		if(ifNode != null)
			return ifNode;
		else
			return null;
	}
	//parse ASSIGNMENT grammer VARIABLE = EXPRESSION
	private AssignmentNode parseAssignment() throws Exception
	{
		VariableNode variable = parseVariable();
		if(variable == null)
			return null;
		if(matchAndRemove(Token.TokenType.EQUALS) == null) throw new Exception("varriable not initialized");
		Node expression = parseExpression();
		System.out.println(expression.toString());
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after statement has finished");
		if(expression == null) 
			throw new Exception("no value given after assignment");
		return new AssignmentNode(variable, expression);
	}
	//parse Expression grammar TERM + TERM || TERM - TERM
	private Node parseExpression()
	{
		try {			

			//parse left hand side
			Node leftTerm = parseTerm();
			if(leftTerm != null)
			{
				//try to create node recurse to right hand side to term
				if(matchAndRemove(Token.TokenType.PLUS) != null)
					return new MathOpNode(Token.TokenType.PLUS, leftTerm, parseTerm());
				else if(matchAndRemove(Token.TokenType.MINUS) != null) 
					return new MathOpNode(Token.TokenType.MINUS, leftTerm, parseTerm());
				else
					return leftTerm;
			}		
			Node function = parseFunction();
			if(function != null)
				return function;
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//parse Term grammar FACTOR * FACTOR || FACTOR / FACTOR
	private Node parseTerm()throws Exception
	{
		//parse left hand side
		Node leftFactor = parseFactor();
		if(leftFactor == null)return null;
		//try to create node recurse right hand side to factor
		if(matchAndRemove(Token.TokenType.TIMES) != null)
		{
			return new MathOpNode(Token.TokenType.TIMES, leftFactor, parseFactor());
		}
		else if(matchAndRemove(Token.TokenType.DIVIDE) != null) 
			return new MathOpNode(Token.TokenType.DIVIDE, leftFactor, parseFactor());
		//if no right hand return left
		else
			return leftFactor;
	}
	//parse Factor grammar integer|| float || lparen EXPRESSION rparen || VARIABLE
	private Node parseFactor() throws Exception
	{
		IntegerNode Inode = parseInteger();
		if(Inode != null) return Inode;
		FloatNode Fnode = parseFloat();
		if(Fnode != null) return Fnode;
		//recurse to expresion if "(" is found
		if(matchAndRemove(Token.TokenType.LPAREN) != null) 
		{
			Node expression = parseExpression();
			if(expression == null)throw new Exception("expression was null");
			if(matchAndRemove(Token.TokenType.RPAREN) != null)
				return expression;
			throw new Exception("no RPAREN found");
		}
		VariableNode variable = parseVariable();
		if(variable != null) return variable;
		return null;
	}
	//parse integer check to see if token is integer, return node if true
	private IntegerNode parseInteger()
	{
		String value = this.lexeme.get(0).getValue();
		if(matchAndRemove(Token.TokenType.NUMBER) == null) return null;
		else
			return new IntegerNode(value);
	}
	//parse float check to see if token is float, return node if true
	private FloatNode parseFloat()
	{
		String value = this.lexeme.get(0).getValue();
		if(matchAndRemove(Token.TokenType.FLOAT) == null) return null;
		else
			return new FloatNode(value);
	}
	private BooleanOperationNode parseBooleanOperation()
	{
		if(matchAndRemove(Token.TokenType.LESSTHAN) != null)
			return new BooleanOperationNode(Token.TokenType.LESSTHAN);
		if(matchAndRemove(Token.TokenType.MORETHAN) != null)
			return new BooleanOperationNode(Token.TokenType.MORETHAN);
		if(matchAndRemove(Token.TokenType.EQUALS) != null)
			return new BooleanOperationNode(Token.TokenType.EQUALS);
		if(matchAndRemove(Token.TokenType.LESSOREQUALS) != null)
			return new BooleanOperationNode(Token.TokenType.LESSOREQUALS);
		if(matchAndRemove(Token.TokenType.MOREOREQUALS) != null)
			return new BooleanOperationNode(Token.TokenType.MOREOREQUALS);
		if(matchAndRemove(Token.TokenType.NOTEQUALS) != null)
			return new BooleanOperationNode(Token.TokenType.NOTEQUALS);
		else 
			return null;
	}
	private VariableNode parseVariable()
	{
		String name = this.lexeme.get(0).getValue();
		if(matchAndRemove(Token.TokenType.IDENTIFIER) == null) return null;
		return new VariableNode(name);
	}	
	//parseString method to add a StringNode
	private StringNode parseString()
	{
		String value = this.lexeme.get(0).getValue();
		if(matchAndRemove(Token.TokenType.STRING) == null) return null;
		return new StringNode(value);
	}
	//parses a labeledStatmentNode
	private LabeledStatementNode parseLabeledStatement() throws Exception
	{
		String label = this.lexeme.get(0).getValue();
		if(matchAndRemove(Token.TokenType.LABEL) == null) return null;
		StatementNode statement = parseStatement();
		if(statement != null)
			return new LabeledStatementNode(label, statement);
		else
			throw new Exception("no statment found after label");
	}
	//aka PrintStatements method from Expand the parser
	private PrintNode parsePrint() throws Exception
	{
		if(matchAndRemove(Token.TokenType.PRINT) == null) return null;
		ArrayList<Node> printList = parseList();
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after statement has finished");
		if(!printList.isEmpty())
			return  new PrintNode(printList);
		return null;
	}
	//parseData method to parse a list from DATA and return a DataNode
	private DataNode parseData() throws Exception
	{
		if(matchAndRemove(Token.TokenType.DATA) == null) return null;
		ArrayList<Node> dataList = parseList();
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after statement has finished");
		if(!dataList.isEmpty())
			return new DataNode(dataList);	
		return null;
	}
	//parseInput method to parse a String/Variable and a list from INPUT, returns a InputNode
	private InputNode parseInput() throws Exception
	{
		if(matchAndRemove(Token.TokenType.INPUT) == null) return null;
		//Creating list and also checking to see if token before the list is a constant String or a variable
		Node temp = parseString();
		if(temp == null)
		{
			temp = parseVariable();
			if(temp == null) 
				throw new Exception("INPUT does not have a string or variable before the list");
		}
		//now parsing through the rest of the list
		ArrayList<Node> inputList = parseList();
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after statement has finished");
		if(!inputList.isEmpty())
		{
			return new InputNode(temp.toString(), inputList);
		}
		return null;
	}
	//parse a READ statement
	private ReadNode parseRead() throws Exception
	{
		if(matchAndRemove(Token.TokenType.READ) == null) return null;
		ArrayList<Node> readList = parseList();
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after statement has finished");
		if(!readList.isEmpty())
			return  new ReadNode(readList);	
		return null;
	}
	//parse a GOSUB statement GOSUB must be followed by an IDENTIFIER token
	private GosubNode parseGosub() throws Exception
	{
		if(matchAndRemove(Token.TokenType.GOSUB) == null)
			return null;
		String identifier = this.lexeme.get(0).getValue();
		if(matchAndRemove(Token.TokenType.IDENTIFIER) == null) 
			throw new Exception("no identifier found after GOSUB");
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after statement has finished");
		return new GosubNode(identifier);
	}
	//parse a Return statement RETURN must be followed by an EndOfLine token
	private ReturnNode parseReturn() throws Exception
	{
		if(matchAndRemove(Token.TokenType.RETURN) == null) 
			return null;
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after RETURN");
		else
			return new ReturnNode();
	}
	//parse a FOR statement must be in the format FOR VARIABLE = INTEGER TO INTEGER || FOR VARIABLE = INTEGER TO INTEGER STEP INTEGER
	private ForNode parseFor() throws Exception
	{
		if(matchAndRemove(Token.TokenType.FOR) == null) 
			return null;
		VariableNode variable = parseVariable();
		if(variable == null) 
			throw new Exception("variable not found after FOR");
		if(matchAndRemove(Token.TokenType.EQUALS) == null)
			throw new Exception("FOR variabel not initialized");
		IntegerNode initializer = parseInteger();
		if(initializer == null) 
			throw new Exception("FOR variabel not initialized");
		if(matchAndRemove(Token.TokenType.TO) == null)
			throw new Exception("FOR TO comparsison is not included");
		IntegerNode comparison = parseInteger();
		if(comparison == null) 
			throw new Exception("TO comparison value not included");
		if(matchAndRemove(Token.TokenType.STEP) == null)
		{
			if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
				throw new Exception("EndOfLine not found after statement has finished");
			return new ForNode(variable, initializer, comparison, new IntegerNode("1"));
		}
		IntegerNode increment = parseInteger();
		if(increment == null) 
			throw new Exception("no value found after STEP");
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after statement has finished");
		return new ForNode(variable, initializer, comparison, increment);
	}
	//parse a NEXT statement
	private NextNode parseNext() throws Exception
	{
		if(matchAndRemove(Token.TokenType.NEXT) == null) 
			return null;
		VariableNode variable = parseVariable();
		if(variable == null)
			throw new Exception("Need variable after NEXT");
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after statement has finished");
		return new NextNode(variable);
	}
	//IfNode parser grammer LPAREN EXPRESSION OPERATION EXPRESSION RPAREN
	private IfNode parseIf() throws Exception
	{
		if(matchAndRemove(Token.TokenType.IF) == null)
			return null;
		if(matchAndRemove(Token.TokenType.LPAREN) == null)
			throw new Exception("no LPAREN found after IF");
		//parsing through the boolean expression
		Node leftExpression = parseExpression();
		if(leftExpression == null)
			throw new Exception("no left expression found in IF");
		BooleanOperationNode operation = parseBooleanOperation();
		if(operation == null)
			throw new Exception("no operand found in IF");
		Node rightExpression = parseExpression();
		if(rightExpression == null)
			throw new Exception("no right expression found in IF");
		//finishing IfNode parse to see if valid
		if(matchAndRemove(Token.TokenType.RPAREN) == null)
			throw new Exception("no closing RPAREN");
		if(matchAndRemove(Token.TokenType.THEN) == null)
			throw new Exception("no THEN statement");
		String identifier = this.lexeme.get(0).getValue();
		if(matchAndRemove(Token.TokenType.IDENTIFIER) == null)
			throw new Exception("no label found after THEN");
		if(matchAndRemove(Token.TokenType.EndOfLine) == null) 
			throw new Exception("EndOfLine not found after IF statement");
		return new IfNode(leftExpression, operation, rightExpression, identifier);
	}
	private FunctionNode parseFunction() throws Exception
	{
		String functionName = "";
		if(matchAndRemove(Token.TokenType.RANDOM) != null) functionName = "RANDOM";
		if(matchAndRemove(Token.TokenType.LEFT$) != null) functionName = "LEFT$";
		if(matchAndRemove(Token.TokenType.RIGHT$) != null) functionName = "RIGHT$";
		if(matchAndRemove(Token.TokenType.MID$) != null) functionName = "MID$";
		if(matchAndRemove(Token.TokenType.NUM$) != null) functionName = "NUM$";
		if(matchAndRemove(Token.TokenType.VAL) != null) functionName = "VAL";
		if(matchAndRemove(Token.TokenType.VAL$) != null) functionName = "VAL%";
		if(functionName.isEmpty()) return null;
		if(matchAndRemove(Token.TokenType.LPAREN) == null)
			throw new Exception("no LPAREN found after function declaration");
		ArrayList<Node> paramList = parseList();
		if(matchAndRemove(Token.TokenType.RPAREN) == null)
			throw new Exception("no RSPAREN found after function declaration");
		return new FunctionNode(functionName, paramList);
	}
	//aka Printlist method from Expand the parser
	private ArrayList<Node> parseList()
	{
		ArrayList<Node> list = new ArrayList<>();
		do
		{
			Node expression = parseExpression();
			if(expression != null)
				list.add(expression);
			Node string = parseString();
			if(string != null)
				list.add(string);
		}while(matchAndRemove(Token.TokenType.COMMA) != null);
		return list;
	}
	//helper function to check if next token is what is expected, return null when fails
	private Boolean matchAndRemove(Token.TokenType tokenType)
	{
		if(this.lexeme.isEmpty())
			return null;
		if(this.lexeme.get(0).getTokenType() == tokenType)
		{
			//System.out.println( this.lexeme.get(0).getValue() );
			lexeme.remove(0);
			return true;
		}
		else
			return null;
	}
}
