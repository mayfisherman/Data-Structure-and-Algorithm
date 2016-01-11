
/**
 * The view for a player, allows a player to show
 * an error, or to highlight cubes, e.g., as on a board
 * for a word found by a player.
 * @author Owen Astrachan
 *
 */
import java.util.List;
public interface IPlayerView {
    /**
     * A view should support some way to visualize a player's word
     * found on a board at some set of board locations.
     * @param word is word visualized
     * @param list of BoardCells the word is found on (on some board)
     * @param player is the player finding a word
     */
    public void showWord(String word, List<BoardCell> list, IPlayer player);
    
    /**
     * Show an error during the game.
     * @param word is cause of error, e.g., duplicate, not in lexicon
     * @param message is displayed with the word
     */
    public void showError(String word, String message);
}
