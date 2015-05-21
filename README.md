# Boulder Bearers
A simple board game of own invention

-----
## Rules:

'Boulder Bearers' is a turn based board game.

Each player has 3 types of pieces:
* boulders (oval shaped)
* strongmen (rectangle shaped)
* guardians (diamond shaped)

There are also 3 kinds of fields:
* empty fields (white)
* traps (crossed)
* fields out of the board (black)

A player loses when all his/her boulders are put in traps. The aim of the game, depending on the mode chosen, is to be either the one who survives when all other players have lost or the first one to make another player lose.

Players taking turns one after another have to make a move obeying the following rules:
* A boulder cannot move on its own.
* A guardian can move onto any adjascent empty field.
* A strongman can move vertically or horizontally through any number of empty fields in a straight line.
* A single strongman can change the position of a light piece (i.e. a boulder or a guardian) by CARRYING it (moving onto its present position and placing it on an adjascent empty field) - it's treated as one move.
* If a strongman carries a light piece in the direction it is itself moving, we call it PUSHING.
* If a guardian stands next to a boulder of the same color, the boulder is protected and cannot be MOVED or PUSHED by an opponent's strongman.
* A strongman can PUSH an opponent's boulder into an empty trap. The boulder cannot be MOVED or PUSHED anymore.


## Setup:
After cloning the repository, the application can be simply built and run with sbt. It's enough to type 'sbt run' in the project's directory.
