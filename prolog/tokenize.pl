:- module(tokenize, [
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
    include(is_word, Tokes, OutW),
    maplist(deword, OutW, Out).

is_word(word(_)).

deword(word(N), NI) :-
    atom_codes(N, C),
    number_codes(NI, C),
    !.
deword(word(W), W).
