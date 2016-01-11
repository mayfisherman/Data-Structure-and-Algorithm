/*
 * Created on Mar 16, 2005
 *
 */

/**
 * A Boggle player must support the methods
 * of this interface. Each player has an associated
 * view. Players add words, e.g., human players 
 * might enter a string of text and computer players
 * might find words on a board passed to a computer player method.
 * (See <code>IAutoPlayer</code>).
 * <P>
 * @author Owen Astrachan
 *
 */

public interface IPlayer extends Iterable<String>{
    
    /**
     * Specify a view for this player, the view
     * is potentially used to show errors, highlight
     * cubes for a word, and so on.
     * @param view
     */
    public void setView(IPlayerView view);
    
    /**
     * Get the score for this player, see <code>BoggleScore</code>
     * for how scores are calculated.
     * @return current score for this player
     */
    public int getScore();
    
    /**
     * Add a word for the player. The player may choose
     * to ignore duplicate words, skip words not in
     * the lexicon, or to check these.
     * @param word
     * @return TODO
     * @return true if the word was added, false otherwise
     */
    public boolean add(String word);
    
    /**
     * Return the name of a player.
     * @return this player's name
     */
    public String getName();
    
    /**
     * Clear state of a player, e.g., as in a new game.
     */
    public void clear();
    
    /**
     * Return number of unique words added by this player.
     * @return number of words
     */
    public int wordCount();
    
}
