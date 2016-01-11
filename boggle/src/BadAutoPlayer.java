import java.util.SortedSet;
import java.util.TreeSet;

/**
 * AutoPlayer that can't find any words. Useful
 * for trying out a game before a good implementation
 * is written.
 * @author Owen Astrachan
 *
 */
public class BadAutoPlayer extends AbstractAutoPlayer {

    public BadAutoPlayer(){
        
    }
    
    public void findAllValidWords(BoggleBoard board,
            ILexicon lex,int minLength) {
        // do nothing!!!
    }

}
