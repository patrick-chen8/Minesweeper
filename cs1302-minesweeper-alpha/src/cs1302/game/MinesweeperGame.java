package cs1302.game;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * The class for {@code MinesweeperGame}, a game that wants its players
 * to determine the location of all the mines.
 */
public class MinesweeperGame {

    private final Scanner stdIn; // declare instance constant
    private File seedFile; // the seed file read from the command line
    private int rows; // the amount of rows from the seed file
    private int cols; // the amouunt of columns in the seed file
    private int numberOfMines; // the number of mines from the seed file
    private boolean [][] location; // an array that stores the location of the mines as true
    private String [][] status; // an array that is updated with the current status of each location
    private int rounds; // a counter that keeps track of the number of rounds
    private int emptySquares; // stores the number of empty squares
    private double score; // calculates the score for the player after they win the game
    private String command; // stores the command that was entered by the player
    private String [][] noFog; // an array that ensures that nofog only lasts for one round.


    /**
     * A constructor that makes a {@code MinesweeperGame} object.
     * @param stdIn a Scanner object that will read in all of the user's inputs
     * @param seedPath a String that reads in a path file for the location of the seed
     */
    public MinesweeperGame(Scanner stdIn, String seedPath) {
        this.stdIn = stdIn;
        seedFile = new File(seedPath);
    }

    /**
     * A method that prints the welcome screen to the Minesweeper Game.
     * It also stores the rows, columns, and the number of mines from the seed file.
     */
    public void printWelcome() {
        try {
            File file = new File("./resources/welcome.txt");
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                System.out.println(input.nextLine());
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println("Seed File Not Found Error: The file was not found.");
            System.exit(2);
        }
        this.readIn();
    } // printWelcome

    /**
     * A helper method that reads in all of information contained in the seed file.
     * It also catches many errors that can occur with the seed file.
     */
    private void readIn() {
        try {
            Scanner seed = new Scanner(seedFile);
            if (seed.hasNext() == false) {
                System.err.println();
                System.err.println("Seed File Malformed Error: The seed file is empty.");
                System.exit(3);
            }
            for (int i = 0; i < 3; i++) {
                try {
                    switch (i) {
                    case 0:
                        rows = seed.nextInt();
                        break;
                    case 1:
                        cols = seed.nextInt();
                        break;
                    case 2:
                        numberOfMines = seed.nextInt();
                        if (numberOfMines < 1 || numberOfMines > (rows * cols) - 1) {
                            System.err.println();
                            System.err.println("Seed File Malformed Error: " +
                                "The number of mines is not allowed.");
                            System.exit(3);
                        } else {
                            break;
                        }
                    }
                } catch (InputMismatchException ime) {
                    System.err.println();
                    System.err.println("Seed File Malformed Error: " +
                        "The content is not of the expected type.");
                    System.exit(3);
                }
            }
            this.initializeArrays();
            while (seed.hasNextInt()) {
                try {
                    int iLocation = seed.nextInt();
                    int jLocation = seed.nextInt();
                    location [iLocation][jLocation] = true;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    System.err.println();
                    System.err.println("Seed File Malformed Error: The mine is out of bounds.");
                    System.exit(3);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println("Seed File Not Found Error: The file was not found.");
            System.exit(2);
        }
        rounds = 0;
        emptySquares = rows * cols - numberOfMines;
        command = "";
    } // readIn

    /**
     * A helper method that initializes all the arrays that will be used throughout.
     * It also ensures that the seed file creates an appropriately sized grid.
     */
    private void initializeArrays() {
        if (rows < 5 || rows > 10 || cols < 5 || cols > 10) {
            System.err.println();
            System.err.println("Seed File Malformed Error: The grid is not of proper size.");
            System.exit(3);
        } else {
            location = new boolean [rows][cols];
            status = new String [rows][cols];
            noFog = new String [rows][cols];
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                location [i][j] = false;
                status [i][j] =  "   ";
                noFog [i][j] = "   ";
            }
        }
    } // initializeArrays

    /**
     * This will print the current status of the minefield.
     * It also handles the nofog command with a separate array.
     */
    public void printMineField() {
        System.out.println("Rounds Completed: " + rounds + "\n");
        if (command.equals("nofog")) {
            for (int i = 0; i < rows + 1; i++) {
                if (i != rows) {
                    System.out.print(" " + i + " ");
                }
                for (int j = 0; j < cols + 1; j++) {
                    if (i == rows && j == 0) {
                        System.out.print("     " + j + " ");
                    } else if (i == rows && j != cols) {
                        System.out.print("  " + j + " ");
                    } else if (i != rows) {
                        if (j < cols) {
                            System.out.print("|" + noFog[i][j]);
                        } else {
                            System.out.print("|");
                        }
                    }
                }
                System.out.println();
            }
        } else {
            for (int i = 0; i < rows + 1; i++) {
                if (i != rows) {
                    System.out.print(" " + i + " ");
                }
                for (int j = 0; j < cols + 1; j++) {
                    if (i == rows && j == 0) {
                        System.out.print("     " + j + " ");
                    } else if (i == rows && j != cols) {
                        System.out.print("  " + j + " ");
                    } else if (i != rows) {
                        if (j < cols) {
                            System.out.print("|" + status[i][j]);
                        } else {
                            System.out.print("|");
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    /**
     * Reads in the user input and interprets the commands properly.
     * Utilizes helper methods for a few of the commands.
     */
    public void promptUser() {
        System.out.print("minesweeper-alpha: ");
        String fullCommand = stdIn.nextLine();
        System.out.println();
        Scanner commandScan = new Scanner(fullCommand);
        command = commandScan.next();
        if (command.equals("r") || command.equals("reveal")) {
            this.reveal(commandScan);
        } else if (command.equals("m") || command.equals("mark")) {
            this.mark(commandScan);
        } else if (command.equals("g") || command.equals("guess")) {
            this.guess(commandScan);
        } else if (command.equals("h") || command.equals("help")) {
            System.out.println("Commands Available...");
            System.out.println("Reveal: r/reveal row col");
            System.out.println("Mark: m/mark row col");
            System.out.println("Guess: g/guess row col");
            System.out.println("Help: h/help");
            System.out.println("Quit: q/Quit");
            rounds++;
        } else if (command.equals("q") || command.equals("quit")) {
            System.out.println("Quitting the game..\nBye!.");
            System.exit(0);
        } else if (command.equals("nofog")) {
            rounds++;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (location [i][j] == true) {
                        if (noFog [i][j].isBlank() == true) {
                            noFog [i][j] = "< >";
                        } else {
                            String currentStatus =  noFog [i][j].trim();
                            noFog [i][j] = "<" + currentStatus + ">";
                        }
                    }
                }
            }
        } else {
            System.err.println();
            System.err.println("Invalid Command: " +
                "Please enter a valid command (type h/help for a list of commands)");
        }
    } // promnptUser


    /**
     * A helper method that handles the reveal command from the user.
     * @param commandScan a scanner object that reads in the commands from the user
     */
    private void reveal(Scanner commandScan) {
        try {
            int rowReveal = commandScan.nextInt();
            int colReveal = commandScan.nextInt();
            if (commandScan.hasNext() == true) {
                 System.err.println();
                 System.err.println("Invalid Command Error: " +
                     "Your command does not have the correct arguments");
            } else {
                if (location [rowReveal][colReveal] == true) {
                    this.printLoss();
                } else {
                    status [rowReveal][colReveal] = " " +
                        Integer.toString(this.getNumAdjMines(rowReveal, colReveal)) + " ";
                    noFog [rowReveal][colReveal] = " " +
                        Integer.toString(this.getNumAdjMines(rowReveal, colReveal)) + " ";
                    rounds++;
                }
            }
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Invalid Command Error: " +
                "Your command does not have the correct arguments");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println();
            System.err.println("Invalid Command Error: " +
                "Your command is out of bounds");
        }
    }


    /**
     * A helper method that handles the mark command.
     * @param commandScan a scanner object that reads in the command from the user
     */
    private void mark(Scanner commandScan) {
        try {
            int rowMark = commandScan.nextInt();
            int colMark = commandScan.nextInt();
            if (commandScan.hasNext() == true) {
                System.err.println();
                System.err.println("Invalid Command Error: " +
                    "Your command does not have the correct arguments");
            } else {
                status [rowMark][colMark] = " F ";
                noFog [rowMark][colMark] = " F ";
                rounds++;
            }
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Invalid Command Error: " +
                "Your command does not have the correct arguments");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println();
            System.err.println("Invalid Command Error: " +
                "Your command is out of bounds");
        }
    }



    /**
     * A helper method that handles the guess command.
     * @param commandScan a scanner object that reads in the command from the user
     */
    private void guess(Scanner commandScan) {
        try {
            int rowGuess = commandScan.nextInt();
            int colGuess = commandScan.nextInt();
            if (commandScan.hasNext() == true) {
                System.err.println();
                System.err.println("Invalid Command Error: " +
                    "Your command does not have the correct arguments");
            } else {
                status [rowGuess][colGuess] = " ? ";
                noFog [rowGuess][colGuess] = " ? ";
                rounds++;
            }
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Invalid Command Error: " +
                "Your command does not have the correct arguments");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println();
            System.err.println("Invalid Command Error: " +
                "Your command is out of bounds");
        }
    }

    /**
     * A method that determines if the games has been won yet.
     * @return true if all the conditions to win a game are met, otherwise false
     */
    public boolean isWon() {
        int counterForMines = 0;
        int emptySquaresCounter = 0;
        boolean won = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                try {
                    if (location [i][j] == true && status [i][j].trim().equals("F")) {
                        counterForMines++;
                    } else if (location [i][j] == false &&
                        Integer.parseInt(status [i][j].trim()) >= 0) {
                        emptySquaresCounter++;
                    }
                } catch (NumberFormatException nfe) {
                    continue;
                }
            }
        }
        if (counterForMines == numberOfMines && emptySquaresCounter == emptySquares) {
            won = true;
            return won;
        } else {
            return won;
        }
    }

    /**
     * This will print the win screen and the score obtained from the game.
     */
    public void printWin() {
        try {
            score = 100.0 * (rows * cols) / rounds;
            score = Math.round(score * 100.0) / 100.0;
            File file = new File("./resources/gamewon.txt");
            Scanner input = new Scanner(file);
            System.out.println();
            while (input.hasNextLine()) {
                System.out.println(input.nextLine());
            }
            System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + score);
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        }
        System.exit(0);
    } // printWin

    /**
     * This prints the loss screen if the user reveals a mine or quits the game.
     */
    public void printLoss() {
        try {
            File file = new File("./resources/gameover.txt");
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                System.out.println(input.nextLine());
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        }
        System.exit(0);
    }

    /**
     * This uses all other methods and fills the location and status arrays with the
     * locations of the mines.
     */
    public void play() {
        this.printWelcome();

        while (this.isWon() == false) {
            this.printMineField();
            this.promptUser();
        }
//        this.printMineField();
        this.printWin();
    } // play

    /**
     * Returns the number of mines adjacent to the specified
     * square in the grid.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     * @return the number of adjacent mines
     */
    private int getNumAdjMines(int row, int col) {
        int counter = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                try {
                    if (location [i][j] == true) {
                        counter ++;
                    }
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    continue;
                }
            }
        }
        if (location [row][col] == true) {
            counter --;
        }
        return counter;
    } // getNumAdjMines

} // Game class
