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
	public static final char SPACE_EMPTY = 'x';
	public static final char SPACE_FLAG = 'f';
	public static final char SPACE_UNKNOWN = '#';
	public static final char SPACE_BOMB = 'b';
	public static final char SPACE_INVALID = '*';
	public static final char SPACE_QUESTION = '?';
	
	public static final String CMD_CLICK = "c";
	public static final String CMD_DEBUG = "d";
	public static final String CMD_FLAG = "f";
	public static final String CMD_QUESTION = "?";
	public static final String CMD_REMOVE = "x";
	
	public static final String RESULT_INVALID = "invalid.";
	public static final String RESULT_WIN = "win.";
	public static final String RESULT_LOSE = "bomb.";
    
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
			// Otherwise, don't do anything - just try again next iteration
		}
		
		// TODO: Update numbers 
		int adjacentBombs;
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				adjacentBombs = 0;
				for (int i = (x - 1); i < (x + 2); i++)
				{
					for (int j = (y - 1); j < (y + 2); j++)
					{
						if (i >= 0 && i < _fullBoard.length &&
							j >= 0 && j < _fullBoard[0].length)
						{
							if (i != x || j != y)
							{
								if (_fullBoard[i][j] == SPACE_BOMB)
								{
									adjacentBombs++;
								}
							}
						}
					}
				}
				if (_fullBoard[x][y] != SPACE_BOMB && adjacentBombs > 0)
				{
					_fullBoard[x][y] = Integer.toString(adjacentBombs).charAt(0);
				}
			}
		}
		
//		prettyPrintFullGrid(); // For testing only
    }

	/**
	 * 
	 * @param command
	 * @param values
	 * @return 
	 */
    public String processCommand(String command, String values)
	{
		if (command.toLowerCase().equals(CMD_DEBUG))
		{
			return "debug(\'" + getUserGrid() + "\', \'" + getFullGrid() + "\').\n";
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
//			System.err.println("values invalid");
			return RESULT_INVALID;
		}
		else
		{
			String[] coordinates = values.split(",");
			int x = Integer.parseInt(coordinates[0]);
			int y = Integer.parseInt(coordinates[1]);

			String result;
			if (x >= 0 && x < _fullBoard.length && y >= 0 && y < _fullBoard[0].length)
			{
//				System.err.println("Command: " + command + ".");
				if (command.toLowerCase().equals(CMD_CLICK))
				{
					result = clickSquare(x, y);
				}
				else if (command.toLowerCase().equals(CMD_FLAG))
				{
					result = flagSquare(x, y);
				}
				else if (command.equals(CMD_QUESTION))
				{
					_userBoard[x][y] = SPACE_QUESTION;
					result = peek(x, y);
				}
				else if (command.toLowerCase().equals(CMD_REMOVE))
				{
					if (_userBoard[x][y] == SPACE_FLAG || _userBoard[x][y] == SPACE_QUESTION)
					{
						_userBoard[x][y] = SPACE_UNKNOWN;
					}
					result = peek(x, y);
				}
				else
				{
//					System.err.println("invalid command");
					result = RESULT_INVALID;
				}
			}
			else
			{
//				System.err.println("Invalid coordinates");
				result = RESULT_INVALID;
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
			return RESULT_LOSE;
		}
		if (allBombsFlagged())
		{
			return RESULT_WIN;
		}
		
		// Update the user board to uncover any blank spaces
		if (_userBoard[x][y] == SPACE_EMPTY)
		{
			uncoverBlanks(x, y);
		}
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
			return RESULT_WIN;
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
				// If a bomb is not flagged or a flag is NOT on a bomb, then just bail out now
				if ((_fullBoard[i][j] == SPACE_BOMB && _userBoard[i][j] != SPACE_FLAG) ||
					(_userBoard[i][j] == SPACE_FLAG && _fullBoard[i][j] != SPACE_BOMB))
				{
//					System.err.println("Unflagged bomb at " + i + ", " + j);
					return false;
				}
			}
		}
		
		// We didn't find an unflagged bomb
		return true;
	}
	
	/** Called after the user clicked a square to uncover any connected blanks
	 * 
	 */
	private void uncoverBlanks(int x, int y)
	{
//		System.err.println("Checking around " + x + " " + y);
		// Count bombs adjacent to this square
		// If count is 0, uncover all adjacent squares
		// and make a recursive call to this function for each that's not already uncovered
		boolean adjacentBombs = false;
		int[][] locationsToCheck = new int[8][2];
		int next = 0;
		
		for (int i = (x - 1); i < (x + 2); i++)
		{
			for (int j = (y - 1); j < (y + 2); j++)
			{
				if (i >= 0 && i < _userBoard.length &&
					j >= 0 && j < _userBoard[0].length)
				{
//					System.err.println(i + " " + j + " " + _fullBoard[i][j] + " " + _userBoard[i][j]);
					if (i != x || j != y)
					{
						if (_fullBoard[i][j] == SPACE_BOMB)
						{
							adjacentBombs = true;
							break;
						}
						else
						{
							locationsToCheck[next][0] = i;
							locationsToCheck[next][1] = j;
//							System.err.println("Check " + i + " " + j);
							next++;
						}
					}
				}
			}
		}
		
		if (!adjacentBombs)
		{
			boolean[] processLocation = new boolean[8];
			for (int i = 0; i < next; i++)
			{
				int xCoord = locationsToCheck[i][0];
				int yCoord = locationsToCheck[i][1];
				
				processLocation[i] = false;
				if (_userBoard[xCoord][yCoord] == SPACE_UNKNOWN)
				{
//					System.err.println("Uncover " + xCoord + " " + yCoord);
					_userBoard[xCoord][yCoord] = _fullBoard[xCoord][yCoord];
					processLocation[i] = true;
				}
			}
			
			for (int k = 0; k < next; k++)
			{
//				System.err.print("Does " + locationsToCheck[k][0] + " " + locationsToCheck[k][1] + " need more checking?");
				if (processLocation[k])
				{
//					System.err.println("Yes");
					int xCoord = locationsToCheck[k][0];
					int yCoord = locationsToCheck[k][1];

					uncoverBlanks(xCoord, yCoord);
				}
//				else
//				{
//					System.err.println("No");
//				}
			}
		}
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
		String s = "click([";
		
		for (int i = (x - 1); i < (x + 2); i++)
		{
			s += "[";
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
				
				if (j != y + 1)
				{
					s += ", ";
				}
			}
			s += "]";
			if (i != x + 1)
			{
				s += ", ";
			}
		}
		
		s += "]).";
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
