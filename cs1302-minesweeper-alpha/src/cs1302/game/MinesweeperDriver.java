package cs1302.game;

import java.util.Scanner;

/**
 * A class for the {@code MinesweeperDriver} which contains the code
 * to start the minesweeper game.
 */
public class MinesweeperDriver {

    public static void main(String [] args) {

        if (args.length != 1) {
            System.err.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        } else {
            Scanner stdIn = new Scanner(System.in);
            String seedPath = args [0];
            MinesweeperGame game = new MinesweeperGame(stdIn, seedPath);
            game.play();
        }


    }


} // Driver class
