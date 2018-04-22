:- module(command, [
              request_command/2
          ]).

:- use_module(game_interact, [player_will_move_to/3,
                             try_move_player_to/2]).

% succeed if we can find a valid command in this
request_command(Tokens, Cmd) :-
    phrase(statement(Cmd), Tokens).


statement(Cmd) -->
    move_direction(Dir),
    {  player_will_move_to(Dir, X, Y),  % might be invalid coords
       format(atom(Cmd), 'c ~w,~w', [Y, X]),
       try_move_player_to(X, Y)
    }.

statement(Cmd) -->
    command(Cmd).

cmd_direction(Dir) -->
    move_direction(Dir).
cmd_direction(Dir) -->
    diag_direction(Dir).

command(Cmd) -->
    command_name(Name),
    cmd_direction(Dir),
    {  player_will_move_to(Dir, X, Y),  % might be invalid coords
       format(atom(Cmd), '~w ~w,~w', [Name, Y, X])   % Aaron wants Row,Col zero based
    }.

command_name(f) --> [f].
command_name(x) --> [x].
command_name(q) --> [q].

move_direction(D) -->
    [D],
    {  memberchk(D, [n,s,e,w]) }.

diag_direction(D) -->
    [D],
    {  memberchk(D, [ne,nw,se,sw]) }.


