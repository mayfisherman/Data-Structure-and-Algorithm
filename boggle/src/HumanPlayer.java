
/**
 * Boggle player that interacts with a human player.
 * Duplicate words generate an error, words not
 * in lexicon generate an error. The errors are shown via
 * an associated view.
 * @author Owen Astrachan
 *
 */

import java.util.List;

public class HumanPlayer extends AbstractPlayer {

    private String myName;
    
    public HumanPlayer(String name){
        myName = name;
    }
    
    /** 
     * Add word if the word is in the lexicon and isn't
     * a duplicate. Update score appropriately via 
     * call to AbstractPlayer (super). Show word found
     * on the associated View of this player.
     */
    public boolean add(String word) {
        if (word.length() == 0){
            myView.showError("","empty word, ignored");
            return false;
        }
        else if (myWords.contains(word)){
            myView.showError(word,"already guessed");
            return false;
        }
        else {
            super.add(word);
            return true;
        }
    }

    /**
     * Returns name of this player.
     * @return name of this human player
     */
    public String getName() {
        return myName;
    }
}
