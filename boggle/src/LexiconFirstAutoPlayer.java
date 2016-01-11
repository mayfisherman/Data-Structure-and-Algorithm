import java.util.*;


public class LexiconFirstAutoPlayer extends AbstractAutoPlayer {

    private IWordOnBoardFinder myFinder;
    
    public LexiconFirstAutoPlayer(){
        myFinder = new GoodWordOnBoardFinder();
    }
    
    @Override
    public void findAllValidWords(BoggleBoard board, ILexicon lex, int minLength) {
        
        clear();
        for(String word : lex) {
            if (word.length() < minLength) continue;
            List<BoardCell> list = myFinder.cellsForWord(board,word);
            if (list.size() > 0) {
                // found word
                add(word);
            }
        }    
    }

}
