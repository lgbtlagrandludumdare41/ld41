:- module(game_interact,
          [create_game/1,
          game_turn/2]).

:- use_module(library(pengines)).

:- dynamic current_process/4, current_location/3.

create_game('THE RAIN MAKES MUD PUDDLES') :-
    pengine_self(PengineID),
    current_process(PengineID, _, _, _),
    !,
    debug(ld(redundant), 'game already created', []).
create_game(Response) :-
    pengine_self(PengineID),
    thread_at_exit(kill_game(PengineID)),
    process_create(
        '../java/run.sh', [10,10], [
                            stdin(pipe(STDIN)),
                            stdout(pipe(STDOUT)),
                            stderr(pipe(STDOUT)),
                            process(PID),
                            priority(1)
                        ]),
    asserta(current_process(PengineID, PID, STDIN, STDOUT)),
    asserta(current_location(PengineID, 4, 4)),
    sleep(2),
    do_cmd('C 4,4', Response),
    debug(ld(create), 'game created ~w, ~w', [PengineID, PID]).

:- multifile sandbox:safe_primitive/1.

sandbox:safe_primitive(game_interact:create_game(_)).

kill_all_processes(PengineID) :-
    setof(PID, current_process(PengineID, PID, _, _), PIDS),
    maplist(process_kill, PIDS).


kill_game(PengineID) :-
    current_process(PengineID, PID, _, _),
    process_kill(PID).


game_turn(Request, Response) :-
    debug(ld(turn), 'turn ~w', [Request]),
    request_command(Request, Cmd),
    !,
    do_cmd(Cmd, Response).
game_turn(Request, Response) :-
    format(string(Response), 'I DONT KNOW HOW TO DO ~w', [Request]).

do_cmd(Cmd, Response) :-
    pengine_self(PengineID),
    current_process(PengineID, _PID, STDIN, STDOUT),
    format(STDIN, '~w\n', [Cmd]),
    flush_output(STDIN),
    sleep(1),
%    read_line_to_codes(STDOUT, X),
%    debug(ld(turn), 'rawout ~s', [X]),
    read_term(STDOUT, Term, []),
    debug(ld(turn), 'resp ~q', [Term]),
    term_string(Term, Response).

sandbox:safe_primitive(game_interact:game_turn(_, _)).

% succeed if we can find a valid command in this
request_command(Request, Cmd) :-
    text_to_string(Request, Str),
    string_codes(Str, Codes),
    phrase(cmd(Cmd), Codes).

cmd('C 5,5') -->
    "FIVE".
