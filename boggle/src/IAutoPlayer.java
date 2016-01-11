import java.util.SortedSet;


/**
 * Add method for auto/computer player to the
 * standard player interface. An autoplayer
 * will find words on a board using a lexicon
 * via the single method exported by this interface.
 * <P>
 * @author Owen Astrachan
 *
 */
public interface IAutoPlayer extends IPlayer{
   
    /**
     * Find all the valid words  automatically, found words should
     * be added via the <code>IPlayer</code> interface's <code>add</code>
     * method.
     * @param board is the BoggleBoard on which wrods are found
     * @param lex is the lexicon in which words are searched/validated
     * @param minLength of words found by an autoplayer
     */
    
    public void findAllValidWords(BoggleBoard board, ILexicon lex, int minLength);
}
