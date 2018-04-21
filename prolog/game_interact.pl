:- module(game_interact,
          [create_game/0,
          game_turn/2]).

:- use_module(library(pengines)).

:- dynamic current_process/4.

create_game :-
    pengine_self(PengineID),
    current_process(PengineID, _, _, _),
    !,
    debug(ld(redundant), 'game already created', []).
create_game :-
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
    debug(ld(create), 'game created ~w, ~w', [PengineID, PID]).

:- multifile sandbox:safe_primitive/1.

sandbox:safe_primitive(game_interact:create_game).

kill_all_processes(PengineID) :-
    setof(PID, current_process(PengineID, PID, _, _), PIDS),
    maplist(process_kill, PIDS).


kill_game(PengineID) :-
    current_process(PengineID, PID, _, _),
    process_kill(PID).


game_turn(Request, Response) :-
    debug(ld(turn), 'turn ~w', [Request]),
    pengine_self(PengineID),
    current_process(PengineID, _PID, STDIN, STDOUT),
    format(STDIN, 'C 4,4\n', []),
    flush_output(STDIN),
    sleep(1),
%    read_line_to_codes(STDOUT, X),
%    debug(ld(turn), 'rawout ~s', [X]),
    read_term(STDOUT, Term, []),
    debug(ld(turn), 'resp ~q', [Term]),
    Response = 'AHEAD OF YOU IS A PARTICULARLY MUDDY PATCH'.

sandbox:safe_primitive(game_interact:game_turn(_, _)).
