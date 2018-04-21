/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ld41;

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
    
    private int _width;
    private int _height;
    private char[][] _fullBoard;
    private char[][] _userBoard;
	
	public Minesweeper()
    {
		this(DEFAULT_WIDTH_HEIGHT, DEFAULT_WIDTH_HEIGHT);
    }
    
    public Minesweeper(int width, int height)
    {
		_width = width;
		_height = height;
		
		_fullBoard = new char[_width][_height];
		_userBoard = new char[_width][_height];
		
		for (int i = 0; i < _width; i++)
		{
			for (int j = 0; j < _height; j++)
			{
				_fullBoard[i][j] = SPACE_EMPTY;
				_userBoard[i][j] = SPACE_UNKNOWN;
			}
		}
    }

    public String processCommand(String command, String values)
	{
		if (command.toLowerCase().equals("d"))
		{
			return getUserGrid() + "\n" + getFullGrid();
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
			if (x > 0 && x < _width && y > 0 && y < _height)
			{
				if (command.toLowerCase().equals("c"))
				{
					result = peek(x, y);
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

	public String getUserGrid()
	{
		String grid = "";
		
		for (int i = 0; i < _width; i++)
		{
			for (int j = 0; j < _height; j++)
			{
				grid += _userBoard[i][j];
			}
		}

		return grid;
	}
	
	public String getFullGrid()
	{
		String grid = "";

		for (int i = 0; i < _width; i++)
		{
			for (int j = 0; j < _height; j++)
			{
				grid += _fullBoard[i][j];
			}
		}

		return grid;
	}
	
	public String peek(int x, int y)
	{
		// Just return test data for now
		return "_FB#?123";
	}
}
