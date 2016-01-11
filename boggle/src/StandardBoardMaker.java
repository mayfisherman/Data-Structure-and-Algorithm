import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Supply a random BoggleBoard that's either 4x4 or 5x5 using standard cubes
 * from Boggle and Big Boggle, respectively. Uses the BoggleFactory's random
 * number generator.
 * <P>
 * 
 * @author Owen Astrachan
 * 
 */
public class StandardBoardMaker implements IBoardMaker {

    private ArrayList<Cube> myDice16;
    private ArrayList<Cube> myDice25;

    public StandardBoardMaker() {
        myDice16 = new ArrayList<Cube>();
        myDice25 = new ArrayList<Cube>();
        initDice();
    }

    /**
     * Initialize Dice list with official boggle cubes for 4x4 game and 5x5 game
     * (Big Boggle)
     * 
     */

    private static String[] bigStrings = { "aafirs", "adennn", "aaafrs",
            "aeegmu", "aaeeee", "aeeeem", "afirsy", "aegmnn", "bjkqxz",
            "ceipst", "ceiilt", "ccnstw", "ceilpt", "ddlonr", "dhlnor",
            "dhhlor", "dhhnot", "ensssu", "emottt", "eiiitt", "fiprsy",
            "gorrvw", "hiprry", "nootuw", "ooottu" };

    private static String[] dieStrings = { "aaeegn", "abbjoo", "achops",
            "affkps", "aoottw", "cimotu", "deilrx", "delrvy", "distty",
            "eeghnw", "eeinsu", "ehrtvw", "eiosst", "elrtty", "himnqu",
            "hlnnrz" };

    private void initDice() {
        for (String s : dieStrings) {
            myDice16.add(new Cube(s));
        }
        for (String s : bigStrings) {
            myDice25.add(new Cube(s));
        }
    }

    /**
     * Returns random boggle board of specified number of rows, boards are
     * square.
     * 
     * @param rows
     *            is number of rows (and columns) in returned board.
     */
    public BoggleBoard makeBoard(int rows) {
        return new BoggleBoard(getRandomBoard(rows * rows));
    }

    /**
     * Return an array of Strings showing the sequence of faces on a randomly
     * generated board.
     */

    private String[] getRandomBoard(int totalSquares) {

        ArrayList<Cube> dice = myDice16;
        if (totalSquares > 16) {
            dice = myDice25;
        }
        String[] letterList = new String[totalSquares];
        Random rand = BoggleBoardFactory.getRandom();
        Collections.shuffle(dice, rand);

        for (int i = 0; i < totalSquares; i++) {
            Cube d = dice.get(i % dice.size());
            letterList[i] = d.getRandomFace();
        }
        return letterList;
    }

}
