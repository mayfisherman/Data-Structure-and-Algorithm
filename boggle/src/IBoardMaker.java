
/**
 * Interface for creating a Boggle Board. Facilitates
 * alternate methods for board creation, e.g., randomly
 * or a fixed board for debugging.
 * <P>
 * @author Owen Astrachan
 *
 */
public interface IBoardMaker {
    /**
     * Return square board of specified size (4 or 5).
     * @param rows is dimension of board
     * @return squre BoggleBoard of specified size
     */
    
    public BoggleBoard makeBoard(int rows);
}
