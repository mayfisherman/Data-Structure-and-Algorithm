import java.util.Random;
import java.io.InputStream;


/**
 * Play a game of Boggle. To play code must instantiate the
 * BoggleGui gui object with a working IWordOnBoardFinder and
 * a working ILexicon, as well as a working IAutoPlayer.
 *
 */

public class BoggleMain {

    public static void main(String[] args) {
        
        ILexicon lexicon = new SimpleLexicon();
        IWordOnBoardFinder finder = new GoodWordOnBoardFinder();
        BoggleBoardFactory.setRandom(new Random(12345));
        InputStream is = lexicon.getClass().getResourceAsStream("/ospd3.txt");      
        IAutoPlayer compPlayer = new LexiconFirstAutoPlayer();
        BoggleGUI bgui = new BoggleGUI(lexicon,finder,is, compPlayer);
    }

}
