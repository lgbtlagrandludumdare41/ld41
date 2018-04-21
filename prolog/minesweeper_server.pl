:- module(minesweeper_server, [
              go/0
          ]).


:- use_module(library(http/thread_httpd)).
:- use_module(library(http/http_dispatch)).
:- use_module(library(http/http_session)).
:- use_module(library(http/html_write)).
:- use_module(library(http/http_parameters)).
:- use_module(library(http/html_head)).
:- use_module(library(http/http_files)).
:- use_module(library(pengines)).
:- use_module(game_interact).

:- use_module(library(sandbox)).
:- use_module(pengine_sandbox:game_interact).

:- multifile http:location/3.
:- dynamic   http:location/3.

http:location(js, '/js', []).
http:location(css, '/css', []).
http:location(img, '/img', []).
user:file_search_path(css, './css').
user:file_search_path(js, './js').
user:file_search_path(icons, './icons').

:- html_resource(style, [virtual(true), requires([css('style.css')]), mime_type(text/css)]).
:- html_resource(script, [virtual(true), requires([js('interact.js')]), mime_type(text/javascript)]).
:- html_resource(jquery, [virtual(true), requires([js('jquery.js')]), mime_type(text/javascript)]).
:- html_resource(pengines_script, [virtual(true), requires([root('pengine/pengines.js')]), mime_type(text/javascript)]).

go :-
    http_set_session_options([timeout(60)]),
    http_server(http_dispatch, [port(9870), timeout(180)]).

:- http_handler(/, game_handler, []).

:- http_handler(js(.), http_reply_from_files('js/', []),
           [priority(1000), prefix]).
:- http_handler(css(.), http_reply_from_files('css/', []),
                [priority(1000), prefix]).
:- http_handler(img(.), http_reply_from_files('icons/', []),
                [priority(1000), prefix]).

game_handler(_Request) :-
    reply_html_page(
        code('MNSWPR'),
        \minesweeper_page).

minesweeper_page -->
    html([div(id(codeliketext),
          [\html_requires(style),
           \html_requires(jquery),
           \html_requires(pengines_script),
           \html_requires(script),
           code(b('***************  MINE SWEEPER  ******************')),
          code('CAPTAIN, WE\'VE SAILED INTO A MINEFIELD'),
          code('LOREM IPSUM DOLOR SIT AMET, CONSECTETUR ADIPISCING ELIT. NULLAM NISI EX, CONSECTETUR AC ALIQUAM EGET, PRAESENT VESTIBULUM VELIT ID VARIUS POSUERE. NULLAM SED COMMODO TORTOR. QUISQUE IMPERDIET ERAT NON DOLOR FACILISIS TINCIDUNT EGET SIT AMET LOREM. NAM PULVINAR IACULIS ERAT SED LUCTUS.'),
          code('NULLA LOBORTIS NIBH MATTIS VELIT MATTIS, NEC CONDIMENTUM RISUS ELEIFEND.ETIAM MATTIS, QUAM ET SAGITTIS MOLLIS, MASSA URNA FAUCIBUS EX, EGET TEMPUS ODIO ELIT AT LACUS. DUIS BIBENDUM CURSUS ENIM VITAE SEMPER. CURABITUR LAOREET EST EU LUCTUS ALIQUAM.')
          ]),
          div(id(inputarea),
             [
                  code('command(C,F, or ?) followed by a pair of numbers'),
                  label(for(user), blink('\u25b6')),
                  input([type(text), name(user), id(inputbox)], [])
              ])
          ]).

