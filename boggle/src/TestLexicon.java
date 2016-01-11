
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class TestLexicon {

    private ArrayList<String> myWords;
    private ArrayList<String> myPrefixes;
    private ArrayList<String> myNonWords;
    
    private ILexicon myLexicon;
    
    public ILexicon makeLexicon(){
        return new CompressedTrieLexicon();
    }
    
    @Before
    public void setUp(){
        String[] words = { "apple", "berry", "cherry", "date",
                           "fig", "melon", "orange", "pineapple",
                           "blueberry", "cherry-pie", "blueberry-pie",
                           "apple-pie", "pineapple-upside-down-cake",
                           "watermelon"};
        String[] prefixes = { "pine", "blue", "water", "melo", "fi", "cherr"};
        String[] nonWords = { "aardvark", "figgy", "melon-ball", "dater", "xylophone",
                              "oranges", "goofy", "mickey"};
        
        myWords = new ArrayList<String>(Arrays.asList(words));
        myPrefixes = new ArrayList<String>(Arrays.asList(prefixes));
        myNonWords = new ArrayList<String>(Arrays.asList(nonWords));
        myLexicon = makeLexicon();
        myLexicon.load(myWords);
    }
    
    @Test
    public void wordTest(){
        assertEquals("size of lexicon failed",myWords.size(), myLexicon.size());
        for(String s : myWords){
            LexStatus stat = myLexicon.wordStatus(s);
            assertEquals("fail for word: "+s,LexStatus.WORD,stat);
        }
    }
    
    @Test
    public void prefixTest(){
        for(String s : myPrefixes){
            LexStatus stat = myLexicon.wordStatus(s);
            assertEquals("fail for prefix: "+s,LexStatus.PREFIX,stat);
        }
    }
    
    @Test
    public void nonWordTest(){
        for(String s : myNonWords){
            LexStatus stat = myLexicon.wordStatus(s);
            assertEquals("fail for non word: "+s,LexStatus.NOT_WORD,stat);
        }
    }
}
