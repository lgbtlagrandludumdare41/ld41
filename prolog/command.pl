:- module(command, [
              request_command/2
          ]).


% succeed if we can find a valid command in this
request_command(Request, Cmd) :-
    phrase(cmd(Cmd), Codes).

cmd('C 5,5') -->
    "FIVE".

