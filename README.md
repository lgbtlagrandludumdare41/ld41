# ld41
Text based Minesweeper 

## Ludum Dare entry

This is our entry for Ludum Dare 41,  a silly text based minesweeper game.

## Code Organization

Code is organized by language - C++ code in cpp,
SWI-Prolog code in prolog

## Architecture

Prolog runs a server on port 9870.

When you http request / there, you get a page.

Javascript on the page will create a pengine, and send a 'make_game' request.

This forks a process, and remembers info about the process in the pengine's module.






