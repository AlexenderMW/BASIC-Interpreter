package ICSI311;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ICSI311.Token;
import ICSI311.Token.TokenType;

public class Lexer {
	//Constructor
	public Lexer()
	{
		this.WordMap = new HashMap<>();
		this.WordMap.put("PRINT", Token.TokenType.PRINT);
		this.WordMap.put("READ", Token.TokenType.READ);
		this.WordMap.put("DATA", Token.TokenType.DATA);
		this.WordMap.put("INPUT", Token.TokenType.INPUT);
		this.WordMap.put("FOR", Token.TokenType.FOR);
		this.WordMap.put("NEXT", Token.TokenType.NEXT);
		this.WordMap.put("RETURN", Token.TokenType.RETURN);
		this.WordMap.put("STEP", Token.TokenType.STEP);
		this.WordMap.put("GOSUB", Token.TokenType.GOSUB);
		this.WordMap.put("TO", Token.TokenType.TO);
		this.WordMap.put("IF", Token.TokenType.IF);
		this.WordMap.put("RANDOM", Token.TokenType.RANDOM);
		this.WordMap.put("LEFT$", Token.TokenType.LEFT$);
		this.WordMap.put("RIGHT$", Token.TokenType.RIGHT$);
		this.WordMap.put("MID$", Token.TokenType.MID$);
		this.WordMap.put("NUM$", Token.TokenType.NUM$);
		this.WordMap.put("VAL", Token.TokenType.VAL);
		this.WordMap.put("VAL%", Token.TokenType.VAL$);
		this.WordMap.put("THEN", Token.TokenType.THEN);
	}
	//lex method to create tokens out of lines received
	public ArrayList<Token> lex(String line) throws Exception
	{
		ArrayList<Token> tokenList = new ArrayList<Token>();
		int state = 1;
		String temp = "";
		//iterating through line
		for(int i = 0; i < line.length(); i++)
		{	
			if(line.charAt(i) != ' ')
			{
				//start state of state machine
				if(state == 1)
				{
					if(Character.isDigit(line.charAt(i))) state = 2;
					else if(line.charAt(i) == '.') state = 3;
					else if(line.charAt(i) == '*') state = 4;
					else if(line.charAt(i) == '/') state = 5;
					else if(line.charAt(i) == '+') state = 6;
					else if(line.charAt(i) == '-') state = 7;
					else if(line.charAt(i) == '<') state = 8;
					else if(line.charAt(i) == '>') state = 9;
					else if(line.charAt(i) == '=') state = 10;
					else if(line.charAt(i) == '"') state = 11;
					else if(line.charAt(i) == ')') state = 12;
					else if(line.charAt(i) == '(') state = 13;
					else if(line.charAt(i) == ',') state = 14;
					else if(Character.isLetter(line.charAt(i))) state = 15;
					else
						throw new Exception("There are characters that are not recognized");
				}
				switch(state)
				{
				//number state
				case 2:
					while((i + 1 < line.length()) && (Character.isDigit(line.charAt(i + 1))))
					{
						temp += line.charAt(i);
						++i;
					}
					temp += line.charAt(i);
					if(i + 1 < line.length())
					{
						//checking if next char is a '.' if so stop this state and move on to state 3
						if(line.charAt(i + 1) == '.')
						{
							state = 3;
							break;
						}
						else
							state = 1;
					}
					tokenList.add(new Token(TokenType.NUMBER, temp));
					temp = "";
					break;
					//decimal state
				case 3:
					while((i + 1 < line.length()) && (Character.isDigit(line.charAt(i + 1))))
					{
						temp += line.charAt(i);
						++i;
					}
					temp += line.charAt(i);
					if(i + 1 < line.length())
					{
						//checking if next char is a '.' if so this is an error
						if(line.charAt(i + 1) == '.')
						{
							state = 1;
							throw new Exception("cannot have a decimal after another decimal or in the same number");
						}
						else
							state = 1;
					}
					tokenList.add(new Token(TokenType.FLOAT, temp));
					temp = "";
					break;
				case 4:
					tokenList.add(new Token(TokenType.TIMES));
					state = 1;
					break;
				case 5:
					tokenList.add(new Token(TokenType.DIVIDE));
					state = 1;
					break;
				case 6:
					tokenList.add(new Token(TokenType.PLUS));
					state = 1;
					break;
				case 7:
					//checking if - indicates a negative number
					if((i == 0) && (i + 1 < line.length()) && (Character.isDigit(line.charAt(i + 1))))
					{
						temp += line.charAt(i);
						state = 2;
						break;
					}
					else if(!(Character.isDigit(line.charAt(i - 2))) && (i + 1 < line.length()) && (Character.isDigit(line.charAt(i + 1))))
					{
						temp += line.charAt(i);
						state = 2;
						break;
					}
					tokenList.add(new Token(TokenType.MINUS));
					state = 1;
					break;
					//case <
				case 8:
					if((i + 1 < line.length()))
					{
						if(line.charAt(i + 1) == '=')
						{
							temp += line.charAt(i);
							state = 10;
							break;
						}
						else if(line.charAt(i + 1) == '>')
						{
							temp += line.charAt(i);
							state = 9;
							break;
						}
					}
					tokenList.add(new Token(TokenType.LESSTHAN));
					state = 1;
					break;
					//case >
				case 9:
					if(temp.equals("<"))
					{
						tokenList.add(new Token(TokenType.NOTEQUALS));
						temp = "";
						state = 1;
						break;
					}
					else if((i + 1 < line.length()))
					{
						if(line.charAt(i + 1) == '=')
						{
							temp += line.charAt(i);
							state = 10;
							break;
						}
					}
					tokenList.add(new Token(TokenType.MORETHAN));
					state = 1;
					break;
					//case =
				case 10:
					if(temp.equals("<"))
					{
						tokenList.add(new Token(TokenType.LESSOREQUALS));
						temp = "";
						state = 1;
						break;
					}
					else if(temp.equals(">"))
					{
						tokenList.add(new Token(TokenType.MOREOREQUALS));
						temp = "";
						state = 1;
						break;
					}
					tokenList.add(new Token(TokenType.EQUALS));
					state = 1;
					break;
					//String case
				case 11:
					++i;
					Boolean syntaxCheck = false;
					while(i + 1 < line.length() && !syntaxCheck)
					{
						if(line.charAt(i + 1) == '"')
							syntaxCheck = true;
						temp += line.charAt(i);
						++i;
					}
					if(!syntaxCheck)
					{ 
						state = 1;
						throw new Exception("Syntax error, no second parenthesis enclosing string");
					}
					tokenList.add(new Token(TokenType.STRING, temp));
					temp = "";
					state = 1;
					break;
					//RPAREN
				case 12:
					tokenList.add(new Token(TokenType.RPAREN));
					state = 1;
					break;
					//LPAREN
				case 13:
					tokenList.add(new Token(TokenType.LPAREN));
					state = 1;
					break;
				//comma case
				case 14:
					tokenList.add(new Token(TokenType.COMMA));
					state = 1;
					break;
					//word case
				case 15:
					String tempChar = "";
					Boolean syntaxFlag = false;
					//reading in string, ending if %, $, or : is found
					while((i  < line.length()) && !(syntaxFlag))
					{
						if((i + 1) < line.length())
						{
							if((line.charAt(i + 1) == '$') || (line.charAt(i + 1) == '%') || (line.charAt(i + 1) == ':'))
							{
								syntaxFlag = true;
								tempChar += line.charAt(i + 1);
							}
						}
						if(!Character.isLetter(line.charAt(i)))
							break;
						temp += line.charAt(i);
						++i;
					}					
					if(syntaxFlag)
						temp += tempChar;
					//checking if the word is known
					if(this.WordMap.containsKey(temp))
					{
						tokenList.add(new Token(WordMap.get(temp), temp));
						temp = "";
						state = 1;
						break;
					}
					//checking if word is a label
					if(tempChar.equals(":"))
					{
						tokenList.add(new Token(TokenType.LABEL, temp));
						temp = "";
						state = 1;
						break;
					}
					//word is an identifier
					else
					{
						tokenList.add(new Token(TokenType.IDENTIFIER, temp));
						temp = "";
						state = 1;
						//in the case that a comma comes directly after an identifier this ensures that it is not glossed over
						if(i < line.length())
							if(line.charAt(i) == ',')
								--i;
						break;
					}
				}
			}
		}
		tokenList.add(new Token(TokenType.EndOfLine));
		return tokenList;
	}
	//keeps a list of known words
	private HashMap<String, Token.TokenType> WordMap;
	}
