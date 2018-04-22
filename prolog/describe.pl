:-module(describe, [
             term_description/2
         ]).


:-use_module(library(dcg/basics)).



% convert a term returned by java to a text description
%
term_description(Term, Desc) :-
    phrase(t_d(Term), DescCodes),
    string_codes(Desc, DescCodes),
    !.

t_d(T) --> valid_td(T),!.
t_d(T) -->
    { format(codes(C), '<b>FUNNY RESPONSE</b> ~w', [T]) },
    C.

valid_td(click([[A,B,C],[D,E,F],[G,H,I]])) -->
    cell_desc(`WHERE YOU STAND `, E),
    cell_desc(`TO THE NORTHWEST `, A),
    cell_desc(`TO THE NORTH `, B),
    cell_desc(`TO THE NORTHEAST `, C),
    cell_desc(`TO THE WEST `, D),
    cell_desc(`TO THE EAST `, F),
    cell_desc(`TO THE SOUTHWEST `, G),
    cell_desc(`TO THE SOUTH `, H),
    cell_desc(`TO THE SOUTHEAST `, I).

cell_desc(Dir, x) -->
    Dir,
    {   random_member( Desc,
        [`THERE IS MORE MUDDY GROUND, WITH DISGUSTING POOLS OF MUD`,
         `THERE IS EVEN MORE BLEAK NOTHINGNESS`,
         `SOME SCRUBBY WEEDS MIX WITH STICKY MUD`
        ])
         },
         Desc,
         { random_member( Tracks,
                          [
             `. MUDDY BOOTPRINTS CRISSCROSS THE FILTH.`,
              `. YOUR TRAIL OF SODDEN FOOTSTEPS CROSSES THIS AREA.`,
               `. SLIMY TRACKS SHOW WHERE YOU PASSED THROUGH HERE.`])
         },
         Tracks,
         `\n`,
         !.


cell_desc(Dir, x) -->
    Dir,
    {   random_member( Desc,
        [`THERE IS MORE MUDDY GROUND, WITH DISGUSTING POOLS OF MUD`,
         `THERE IS EVEN MORE BLEAK NOTHINGNESS`,
         `SOME SCRUBBY WEEDS MIX WITH STICKY MUD`
        ])
         },
         Desc,
         { random_member( Tracks,
                          [
             `. MUDDY BOOTPRINTS CRISSCROSS THE FILTH.`,
              `. YOUR TRAIL OF SODDEN FOOTSTEPS CROSSES THIS AREA.`,
               `. SLIMY TRACKS SHOW WHERE YOU PASSED THROUGH HERE.`])
         },
         Tracks,
         ` YOU DONT SENSE ANY MINES NEAR HERE.\n`,
         !.

cell_desc(Dir, f) -->
    Dir,
    {   random_member( Desc,
        [`THERE IS MORE MUDDY GROUND, WITH DISGUSTING POOLS OF MUD.`,
         `THERE IS EVEN MORE BLEAK NOTHINGNESS.`,
         `SOME SCRUBBY WEEDS MIX WITH STICKY MUD.`
        ])
         },
         Desc,
         ` YOU PLANTED A FLAG HERE.\n`,
         !.


cell_desc(Dir, #) -->
    Dir,
    {   random_member( Desc,
        [`THERE IS MORE MUDDY GROUND, WITH DISGUSTING POOLS OF MUD.`,
         `THERE IS EVEN MORE BLEAK NOTHINGNESS.`,
         `SOME SCRUBBY WEEDS MIX WITH STICKY MUD.`
        ])
         },
         Desc,
         `\n`,
         !.


cell_desc(Dir, b) -->
    Dir,
    `<b>THERE IS A LAND MINE!</b>\n`
    .

cell_desc(Dir, *) -->
    Dir,
    `TALL CLIFFS BLOCK YOUR WAY.\n`
    .
cell_desc(Dir, ?) -->
    Dir,
    `YOU REMEMBER THINKING THIS SPOT MIGHT BE DANGEROUS.\n`
    .

cell_desc(Dir, N) -->
    Dir,
    { integer(N) },
    `YOU THINK THERE ARE `,
    integer(N),
    ` MINES NEAR HERE.\n`
    .






