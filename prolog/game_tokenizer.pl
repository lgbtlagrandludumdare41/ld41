:- module(game_tokenizer, [
              tokenize/2
          ]).

:- use_module(library(dcg/basics)).

:- use_module(library(tokenize), [tokenize/3 as toke]).
:- use_module(library(apply)).

tokenize(In, Out) :-
    string(In),
    string_codes(In, IC),
    t(IC, Out).
tokenize(In, Out) :-
    atom(In),
    atom_codes(In, IC),
    t(IC, Out).
tokenize(In, Out) :-
    is_list(In),
    t(In, Out).

t(In, Out) :-
    toke(In, Tokes, [spaces(false),
                     cntrl(false),
                     punct(false),
                     to(atoms),
                     pack(false)]),
    maplist(deword, Tokes, OutW),
    include(is_word, OutW, Out).

is_word(W) :-
    number(W).
is_word(W) :-
    W \= 'CRAP'.

deword(word(N), NI) :-
    atom_codes(N, C),
    catch(
        number_codes(NI, C),
        _,
        fail),
    !.
deword(word(W), Ret) :-
    memberchk(W-Ret, [
                 flag-f,
		 f-f,
		 mark-f,
		 ? - q,
		 q-q,
		 question-q,
		 remove-x,
		 x-x,
		 r-x,
		 clear-x,
		 n-n,
		 north-n,
		 s-s,
		 south-s,
		 e-e,
		 east-e,
		 w-w,
		 west-w,
		 ne-ne,
		 northeast-ne,
		 se-se,
		 southeast-se,
		 nw-nw,
		 northwest-nw,
		 sw-sw,
		 southwest-sw
                ]),
    !.
deword(_, 'CRAP').
