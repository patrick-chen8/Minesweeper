# Minesweeper

### Introduction
This project was assigned to me during my CS1302 class at UGA. The goal of this project was to familiarize students with the problem solving process necessary to create solutions.
The instructions provided for the class were extremely detailed, but they still allowed for creative thinking. The goal of the project was to create a non-GUI based minesweeper game.

### Technology Used
- Java 11.0.12
- MobaXterm (Unix emulator)

### Solution/Thought Process

##### Creating the minefield
This was one of the simplest taks. I was able to create a string representation of the minefield via a for loop that would print each line. However, this was just visual, and the actual
data was stored in a 2D string array called status.

#### User commands
The user commands were taken in via a Scanner object and compared with the possible commands: reveal, mark, guess, help, and quit. If the user command matched the syntax of a command,
then the user's desired operation was performed.

#### Minefield data
The location of the mines were stored in a 2D boolean array, location. Whenever a user used the reveal command, the specified location would be checked against the location array. 
If the location array had a value of true at the specified location, then that would mean the user had revealed a mine, resulting in a loss. This was the simplest case. In Minesweeper, 
whenever a user used the reveal command but did not hit a mine, the game would return the number of adjacent mines. This is also true for my version. In order to accomplish this, 
a helper method called getNumAdjMines that would return the number of adjacent mines to a location was created. If a user revealed a location that was not a mine, getNumAdjMines 
was called, and the status array was updated to match the number of adjacent mines. When the next round was printed, the number of adjacent mines would be in the specified location.
Another command that required updating the status was the mark command. Whenever a user wanted to mark a location, the status array was updated so that an F would appear when it 
would be printed in the next round.

#### Loss conditions
Upon a user revealing a square with a mine, the printloss method was run, which would print a GAMEOVER screen and exit the game, returning the user back to the command line. 
In order for this method to function correctly, an if statement that covered the case of revealing a mine was used. The game over screen was provided by the instructors.

#### Win conditions
Upon a user marking the location(s) of all of the mine(s) and revealing all other squares, a printWin method was run, which would print a game won screen and exit the game. 
In order for this method to function correctly, a isWon boolean method that tested for these win conditions was run every time the user inputted a command. 
If the isWon method ever returned true, then the printWin method would run. This game won screen was also provided by the instructors.



