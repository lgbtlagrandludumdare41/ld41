#include "minesweeper.h"
#include <vector>
#include <iostream>
#include <cstring>

#define SPACE_EMPTY '_'
#define SPACE_FLAG 'F'
#define SPACE_UNKNOWN '#'
#define SPACE_BOMB 'B'
#define SPACE_INVALID '*'
#define SPACE_QUESTION '?'

Minesweeper::Minesweeper()
{
	Minesweeper(DEFAULT_WIDTH_HEIGHT, DEFAULT_WIDTH_HEIGHT);
}

Minesweeper::Minesweeper(int width, int height)
{
	_fullBoard = new char*[width];
	_userBoard = new char*[width];

	for (int i = 0; i < width; i++)
	{
		_fullBoard[i] = new char[height];
		_userBoard[i] = new char[height];
	}

	_width = width;
	_height = height;

	for (int row = 0; row < width; row++)
	{
		for (int col = 0; col < height; col++)
		{
			_fullBoard[row][col] = SPACE_EMPTY;
			_userBoard[row][col] = SPACE_UNKNOWN;
		}
	}
}

Minesweeper::~Minesweeper()
{
	for (int i = 0; i < _width; i++)
	{
		delete[] _fullBoard[i];
		delete[] _userBoard[i];
	}

	delete[] _fullBoard;
	delete[] _userBoard;
}

std::string Minesweeper::processCommand(std::string command, std::string values)
{
	if (command.compare("d") == 0 || command.compare("D") == 0)
	{
		return getUserGrid() + "\n" + getFullGrid();
	}
	else
	{
		int next = values.find_first_of(",", 0);
		int x = std::stoi(values.substr(0, next));
		int y = std::stoi(values.substr(next + 1, command.length()));

		std::string result;
		std::cout << "Command is " << command << ".\n";
		if (command.compare("C") == 0 || command.compare("c") == 0)
		{
			result = peek(x, y);
		}
		else if (command.compare("F") == 0 || command.compare("f") == 0)
		{
			_userBoard[x][y] = SPACE_FLAG;
			result = peek(x, y);
		}
		else if (command.compare("Q") == 0 || command.compare("q") == 0)
		{
			_userBoard[x][y] = SPACE_QUESTION;
			result = peek(x, y);
		}
		else
		{
			result = "Invalid";
		}

		return result;
	}
}

std::string Minesweeper::getUserGrid()
{
	std::string grid;

	for (int i = 0; i < _width; i++)
	{
		for (int j = 0; j < _height; j++)
		{
			grid += _userBoard[i][j];
		}
	}

	return grid;
}

std::string Minesweeper::getFullGrid()
{
	std::string grid;

	for (int i = 0; i < _width; i++)
	{
		for (int j = 0; j < _height; j++)
		{
			grid += _fullBoard[i][j];
		}
	}

	return grid;
}

std::string Minesweeper::peek(int x, int y)
{
	return "_FB#*?123";
}
