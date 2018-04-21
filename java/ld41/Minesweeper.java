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
	
	/** Default constructor. Just calls the other one with the default width/height
	 * 
	 */
	public Minesweeper()
    {
		this(DEFAULT_WIDTH_HEIGHT, DEFAULT_WIDTH_HEIGHT);
    }
    
	/** Constructor. Sets up both the user and full boards and places the bombs.
	 * 
	 * @param width
	 * @param height 
	 */
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
			// We also don't allow a bomb on the 'center' space, since that's where the user starts
			if (_fullBoard[row][col] == SPACE_EMPTY && row != Math.round(width / 2) && col != Math.round(height / 2))
			{
				_fullBoard[row][col] = SPACE_BOMB;
				numBombs++;
			}
			// Otherwise, don't do anything - just roll another (x, y) next time
		}
		
		prettyPrintFullGrid(); // For testing only
    }

	/**
	 * 
	 * @param command
	 * @param values
	 * @return 
	 */
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
			int x = Integer.parseInt(coordinates[0]);
			int y = Integer.parseInt(coordinates[1]);

			String result;
			if (x >= 0 && x < _fullBoard.length && y >= 0 && y < _fullBoard[0].length)
			{
				if (command.toLowerCase().equals("c"))
				{
					result = clickSquare(x, y);
				}
				else if (command.toLowerCase().equals("f"))
				{
					result = flagSquare(x, y);
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
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return 
	 */
	private String clickSquare(int x, int y)
	{	
		_userBoard[x][y] = _fullBoard[x][y];
		
		if (_userBoard[x][y] == SPACE_BOMB)
		{
			return "Bomb";
		}
		if (allBombsFlagged())
		{
			return "Win";
		}
		
		// Update the user board to uncover any blank spaces
		uncoverBlanks();
		return peek(x, y);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return 
	 */
	private String flagSquare(int x, int y)
	{
		_userBoard[x][y] = SPACE_FLAG;
		
		if (allBombsFlagged())
		{
			return "Win";
		}
		
		return peek(x, y);
	}
	
	/** Determines if all bombs in the full board have been flagged by the user
	 * 
	 * @return true if all bombs are flagged, and false otherwise
	 */
	private boolean allBombsFlagged()
	{
		for (int i = 0; i < _fullBoard.length; i++)
		{
			for (int j = 0; j < _fullBoard[0].length; j++)
			{
				if (_fullBoard[i][j] == SPACE_BOMB && _userBoard[i][j] != SPACE_FLAG)
				{
					System.out.println("Unflagged bomb at " + i + ", " + j);
					return false;
				}
			}
		}
		
		// We didn't find an unflagged bomb
		return true;
	}
	
	/**
	 * 
	 */
	private void uncoverBlanks()
	{
		
	}

	/**
	 * 
	 * @return 
	 */
	private String getUserGrid()
	{
		String s = "";
		
		for (char[] row : _userBoard)
		{
			for (char square : row)
			{
				s += square;
			}
		}
		return s;
	}
	
	/**
	 * 
	 * @return 
	 */
	private String getFullGrid()
	{
		String s = "";

		for (char[] row : _fullBoard)
		{
			for (char square : row)
			{
				s += square;
			}
		}

		return s;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return 
	 */
	private String peek(int x, int y)
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
	
	/** Debugging function that neatly prints the user board
	 * 
	 */
	private void prettyPrintUserGrid()
	{
		String s = "";
		for (char[] row : _userBoard)
		{
			for (char square : row)
			{
				s += square + " ";
			}
			s += "\n";
		}
		
		System.out.print(s);
	}
	
	/** Debugging function that neatly prints the full board
	 * 
	 */
	private void prettyPrintFullGrid()
	{
		String s = "";
		for (char[] row : _fullBoard)
		{
			for (char square : row)
			{
				s += square + " ";
			}
			s += "\n";
		}
		
		System.out.print(s);
	}
}
