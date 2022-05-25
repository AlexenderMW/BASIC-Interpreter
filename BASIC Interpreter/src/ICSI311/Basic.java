package ICSI311;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
public class Basic {
//ArrayList<Type> str = new ArrayList<Type>();
	public static void main(String args[]) throws Exception
	{  
		//check if only 1 command line arg, else nuke it from orbit
		if(args.length > 1)
		{
			System.out.println("Error to many command line arguments... exiting");
			System.exit(1);
		}
		//sytem out for testing purposes 
		System.out.println("Your first argument is: "+args[0]);
		Path path = Paths.get(args[0]);
		System.out.println(path.toString());
		List<String> list = Files.readAllLines(path);

		ArrayList<Token> tokenList = new ArrayList<Token>();
		System.out.println("Lexing...");
		Lexer lexer = new Lexer();	
		//using lex on each line and placing into ArrayList of Tokens
		for(int i = 0; i < list.size(); ++i)
		{
			System.out.println(list.get(i));
			try
			{
			tokenList.addAll(lexer.lex(list.get(i)));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		System.out.println(tokenList);
		System.out.println("Parsing...");
		Parser parser = new Parser(tokenList);
		Node ASTTree = parser.parse();
		
		System.out.println(ASTTree.toString());
		System.out.println("Interpreting...");
		Interpreter interpreter = new Interpreter((StatementsNode)ASTTree);
		interpreter.initialize();
		System.out.println(interpreter.toString());
	}  

}