import java.util.SortedSet;

/**
 * Augments <code>AutoPlayer</code> by adding
 * abstract method for autoplayer.
 * <P>
 * @author Owen Astrachan
 *
 */
public abstract class AbstractAutoPlayer extends AbstractPlayer implements IAutoPlayer{
    
    public abstract void findAllValidWords(BoggleBoard board, ILexicon lex,int minLength);

    /**
     * Return string "AutoPlayer".
     * @return this player's name
     */
    public String getName() {
       return "AutoPlayer";
    }
}
