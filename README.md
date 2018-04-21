# ld41
Text based Minesweeper 

## Ludum Dare entry

This is our entry for Ludum Dare 41,  a silly text based minesweeper game.

## Code Organization

Code is organized by language - Java code in java,
SWI-Prolog code in prolog

## Architecture

Prolog runs a server on port 9870.

When you http request / there, you get a page.

Javascript on the page will create a pengine, and send a 'make_game' request.

This forks a process, and remembers info about the process in the pengine's module.

## Communication

### prolog -> java

 Prolog sends Java the data as a command character, a space, a signed int, a comma, a signed int, and a
 newline, or as a special command followed by a newline.
 
 The numbers are zero based indices into the grid, X,Y
 they are not guaranteed to be on the board.
 
 The command character is one of:
 
   * C - click here
   * D - return the debug message
   * F - plant a flag here
   * ? - plant a question mark here
   * X - remove flags or question marks from here
   * X - remove flags or question marks from here
   
   The special commands are
   
   * exit - cause the Java process to die
   
Java then responds with a string followed by a newline.


#### C, F, X, and ?

For these, the response is 

    click([[X, X, X], [X, X, X], [X, X, X]]).\n
    
(note the period at the end)
where X is one of (case sensitive)

   * x    empty
   * f    flag
   * #    unknown
   * b    bomb
   * *    invalid
   * ?    question_mark

or is 

    lose.\n
    
or is 

    win.\n
    
or is

    invalid.\n
    
Note the periods and lowercase.

#### D

For D, the response is

   debug(\`##########\`, \`xxxxxxxxxxxxxxxxx\`).\n
   
note that those are back ticks.


	
	





