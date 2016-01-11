import java.util.Iterator;
import java.util.TreeSet;

/**
 * Supplies default implementation and state for a
 * player. Allows a player to a view, a scorekeeper, a lexicon
 * and to store words found in a game. This implementation
 * uses a set to store strings/words and updates score
 * for new words added to the player. Words are not
 * checked for being in a lexicon or on a board in the
 * <code>add</code> method.
 * <P>
 * @author Owen Astrachan
 *
 */
public abstract class AbstractPlayer implements IPlayer {
    
    protected IPlayerView myView;
    protected TreeSet<String> myWords;
    protected BoggleScore myScoreKeeper;
    
    protected int myScore;
    
    /**
     * Make this player have an associated lexicon
     * and initialize other state appropriately.
     * @param lex is the lexicon for this player
     */
    public AbstractPlayer() {
        myWords = new TreeSet<String>();
        myScoreKeeper = new BoggleScore();
        myScore = 0;
    }
    
    /**
     * Set a veiw for this player, if not called
     * the player will have a null view.
     * @param view is this player's view
     */
    public void setView(IPlayerView view){
        myView = view;
    }

    /**
      * Return the player's score (as calculated via updates to the
      * <code>add</code> method).
      * @return this player's score
      */
    public int getScore() {
       return myScore;
    }

    /**
     * Add a word to this player's list of words, adjust
     * score if word is new to this player.
     * Duplicate words are not added, board and lexicon
     * are <em>not checked</em>.
     * @param word is the word added to this player
     */
    public boolean add(String word) {
        
        if (! myWords.contains(word)){
            myScore += myScoreKeeper.getScore(word,4);
            myWords.add(word);
            return true;
        }
        return false;
    }
    
    public Iterator<String> iterator(){
        return myWords.iterator();
    }

    public abstract String getName();
    
    public void clear(){
        myWords.clear();
        myScore = 0;
    }
    
    public int wordCount(){
        return myWords.size();
    }
}
