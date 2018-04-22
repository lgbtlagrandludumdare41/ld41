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

#### TODOS
 * Prolog: Fix the hardcoded board size
 * Prolog: Make sure everything the Java program splits out is handled correctly
 * Prolog: If the user tries to perform an action on a spot off the board, display an appropriate message in response to the Java program returning 'invalid.' Some ideas:
   * YOU CAN'T DO THAT -- THERE'S A LARGE TREE IN THAT DIRECTION.
   * YOU SEE A SWIRLING MASS OF MUD IN THAT DIRECTION. IT'S PROBABLY A BAD IDEA TO GO THAT WAY.
   * YOU START TO STEP IN THAT DIRECTION WHEN A TENTACLE SUDDENLY RISES FROM THE MUD. YOU LEAP BACK IN SURPRISE.


	
	





