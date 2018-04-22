:- module(game_interact,
          [create_game/1,
          game_turn/2,
          player_will_move_to/3]).

:- use_module(library(pengines)).
:- use_module(describe).
:- use_module(command).
:- use_module(game_tokenizer).

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

game_turn(RawRequest, Response) :-
    debug(ld(turn), 'turn ~w', [RawRequest]),
    tokenize(RawRequest, Request),
    debug(ld(tokens), '~q', [Request]),
    game_turn_(Request, Response).

game_turn_(Request, Response) :-
    request_command(Request, Cmd),
    !,
    do_cmd(Cmd, Response).
game_turn_(Request, Response) :-
    format(string(Response), 'I DONT KNOW HOW TO DO THAT', [Request]).

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
    term_description(Term, ResponseStr),
    !,
    split_string(ResponseStr, "\n", "", SubStrs),
    member(Response, SubStrs).

sandbox:safe_primitive(game_interact:game_turn(_, _)).

% may return invalid coords
player_will_move_to(Dir, X, Y) :-
    pengine_self(ID),
    current_location(ID, OldX, OldY),
    memberchk(Dir-DX-DY, [
                         n-0- -1,
                         ne-1- -1,
                         e-1-0,
                         se-1-1,
                         s-0-1,
                         sw- -1-1,
                         w- -1-0,
                         nw- -1- -1
                     ]),
    X is OldX + DX,
    Y is OldY + DY.
