/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ld41;
import java.util.Random;

/**
 *
 * @author aaron
 */
public class Minesweeper 
{
    public static final int DEFAULT_WIDTH_HEIGHT = 10;
	public static final char SPACE_EMPTY = '_';
	public static final char SPACE_FLAG = 'F';
	public static final char SPACE_UNKNOWN = '#';
	public static final char SPACE_BOMB = 'B';
	public static final char SPACE_INVALID = '*';
	public static final char SPACE_QUESTION = '?';
    
    private char[][] _fullBoard;
    private char[][] _userBoard;
	
	public Minesweeper()
    {
		this(DEFAULT_WIDTH_HEIGHT, DEFAULT_WIDTH_HEIGHT);
    }
    
    public Minesweeper(int width, int height)
    {	
		_fullBoard = new char[width][height];
		_userBoard = new char[width][height];
		
		// Initialize the boards. The user can't see anything and the real board is empty
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				_fullBoard[i][j] = SPACE_EMPTY;
				_userBoard[i][j] = SPACE_UNKNOWN;
			}
		}
		
		// Add some bombs! 10% of the squares will contain a bomb
		int numBombs = 0;
		int maxBombs = (int) Math.round(width * height * 0.1);
		Random r = new Random(System.currentTimeMillis());
		while (numBombs < maxBombs)
		{
			int row = r.nextInt(width);
			int col = r.nextInt(height);
			
			// If there isn't already a bomb here, put one
			if (_fullBoard[row][col] == SPACE_EMPTY)
			{
				_fullBoard[row][col] = SPACE_BOMB;
				numBombs++;
			}
			// Otherwise, don't do anything - just roll another (x, y) next time
		}
		
		prettyPrintFullGrid();
    }

    public String processCommand(String command, String values)
	{
		if (command.toLowerCase().equals("d"))
		{
			return getUserGrid() + "\n" + getFullGrid();
		}
		else if (command.toLowerCase().equals("u"))
		{
			prettyPrintUserGrid();
			return "";
		}
		else if (command.toLowerCase().equals("a"))
		{
			prettyPrintFullGrid();
			return "";
		}
		else if (!values.contains(",") || values.length() < 3)
		{
			return "Invalid";
		}
		else
		{
			String[] coordinates = values.split(",");
			// Convert into 0-indexed
			int x = Integer.parseInt(coordinates[0]) - 1;
			int y = Integer.parseInt(coordinates[1]) - 1;

			String result;
			if (x >= 0 && x < _fullBoard.length && y >= 0 && y < _fullBoard[0].length)
			{
				if (command.toLowerCase().equals("c"))
				{
					result = clickBoard(x, y);
				}
				else if (command.toLowerCase().equals("f"))
				{
					_userBoard[x][y] = SPACE_FLAG;
					result = peek(x, y);
				}
				else if (command.equals("?"))
				{
					_userBoard[x][y] = SPACE_QUESTION;
					result = peek(x, y);
				}
				else
				{
					result = "Invalid";
				}
			}
			else
			{
				result = "Invalid";
			}

			return result;
		}
	}
	
	public String clickBoard(int x, int y)
	{
		String s = "";
		
		return s;
	}

	public String getUserGrid()
	{
		String s = "";
		
		for (int i = 0; i < _userBoard.length; i++)
		{
			for (int j = 0; j < _userBoard[0].length; j++)
			{
				s += _userBoard[i][j];
			}
		}

		return s;
	}
	
	public String getFullGrid()
	{
		String s = "";

		for (int i = 0; i < _fullBoard.length; i++)
		{
			for (int j = 0; j < _fullBoard[0].length; j++)
			{
				s += _fullBoard[i][j];
			}
		}

		return s;
	}
	
	public String peek(int x, int y)
	{
		String s = "";
		
		for (int i = (x - 1); i < (x + 2); i++)
		{
			for (int j = (y - 1); j < (y + 2); j++)
			{
				if (i < 0 || j < 0 || i >= _userBoard.length || j >= _userBoard[0].length)
				{
					s += SPACE_INVALID;
				}
				else
				{
					s += _userBoard[i][j];
				}
			}
		}
		
		return s;
	}
	
	private void prettyPrintUserGrid()
	{
		String s = "";
		for (int i = 0; i < _userBoard.length; i++)
		{
			for (int j = 0; j < _userBoard[0].length; j++)
			{
				s += _userBoard[i][j] + " ";
			}
			s += "\n";
		}
		
		System.out.print(s);
	}
	
	private void prettyPrintFullGrid()
	{
		String s = "";
		for (char[] _fullBoardRow : _fullBoard)
		{
			for (char _square : _fullBoardRow)
			//for (int j = 0; j < _fullBoard[0].length; j++)
			{
				s += _square + " ";
			}
			s += "\n";
		}
		
		System.out.print(s);
	}
}
