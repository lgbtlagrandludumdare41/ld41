#include <iostream>
#include "minesweeper.h"

using namespace std;

int main(int argc, char **argv)
{
    int width = DEFAULT_WIDTH_HEIGHT;
    int height = DEFAULT_WIDTH_HEIGHT;
    if (argc >= 2)
    {
		width = stoi(argv[1]);
    }
    if (argc >= 3)
    {
		height = stoi(argv[2]);
    }

    Minesweeper game(width, height);

    string command, values;
    do
    {
		cin >> command >> values;
		cout << game.processCommand(command, values) << endl;
	} while (command != "exit" && command != "quit"); // Just for testing purposes

    return 0;
}
