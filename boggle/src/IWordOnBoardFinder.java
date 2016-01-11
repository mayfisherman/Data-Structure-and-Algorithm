import java.util.List;

public interface IWordOnBoardFinder
{

    /**
     * Return a list of cells on the given board such that
     * the i-th element of the list correponds to the
     * i-th character of the string as found on the board.
     * Returns an empty list if the word cannot be found on the board.
     * @param board is searched for the given word
     * @param word is being searched for on the board
     * @return list of cells on the supplied board that
     * correspond to the word, an empty list should be returned if the word cannot be
     * found on the board
     */
  
    public List<BoardCell> cellsForWord(BoggleBoard board, String word);
    
}
