import java.util.*;

/**
 * Simple Lexicon implementation simply storing
 * words in a set. This makes finding WORD status
 * fast, but PREFIX requires iterating over
 * all the strings stored.
 * <P>
 * @author Owen Astrachan
 *
 */
public class SimpleLexicon implements ILexicon {

    private TreeSet<String> myWords;
    private String myUpperBound;
    
    public SimpleLexicon(){
       myWords = new TreeSet<String>();
    }
    public void load(Scanner s) {
        myWords.clear();
        while (s.hasNext()){
            myWords.add(s.next().toLowerCase());
        }
        myUpperBound = myWords.last()+"\0";
    }
    
    public void load(ArrayList<String> list){
        myWords.clear();
        myWords.addAll(list);
        myUpperBound = myWords.last()+"\0";
    }

    public LexStatus wordStatus(String s) {
       if (myWords.contains(s)){
            return LexStatus.WORD;
        }
        if (s.compareTo(myUpperBound) > 0){
            return LexStatus.NOT_WORD;
        }
        String wordAfter = myWords.higher(s);
        if(wordAfter != null && wordAfter.startsWith(s)) return LexStatus.PREFIX;
        return LexStatus.NOT_WORD;
    }

    public Iterator<String> iterator() {
        return myWords.iterator();
    }

    public int size() {
        return myWords.size();
    }
    public LexStatus wordStatus(StringBuilder s) {
        return wordStatus(s.toString());
    }
}
