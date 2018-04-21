#ifndef MINESWEEPER_H

#include <string>

#define DEFAULT_WIDTH_HEIGHT	10

class Minesweeper
{
private:
	char **_fullBoard;
	char **_userBoard;
	int _width;
	int _height;

	std::string getUserGrid();
	std::string getFullGrid();
	std::string peek(int x, int y);

public:
	Minesweeper();
	Minesweeper(int width, int height);
	~Minesweeper();

	std::string processCommand(std::string command, std::string values);
};

#endif
