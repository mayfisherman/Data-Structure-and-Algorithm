
/**
 * Specification for a Lexicon as used in the Boggle
 * assignment. Lexicons can be loaded from a scanner or an 
 * ArrayList, searched for a string to see if the string is a WORD, the
 * PREFIX of a word, or is NOT_WORD (see LexStatus).
 * <P>
 * Words can be queried by using either a StringBuilder or a String.
 * Implementations may convert one to another so that there's no
 * guarantee that the <code>wordStatus</code> method doesn't create
 * a new object in performing a lexicon query.
 * <P>
 * 
 * @author Owen Astrachan
 *
 */

import java.util.*;
public interface ILexicon extends Iterable<String>{

    /**
     * Clear a lexicon and store all values read
     * from the scanner for subsequent querying.
     * @param s is the source of words for the lexicon
     */
    public void load(Scanner s);
    
    /**
     * Clear a lexicon and store all values read
     * from the list for subsequent querying.
     * @param list is the source of words for the lexicon
     */
    public void load(ArrayList<String> list);
    
    /**
     * Returns value specifying whether is is in the
     * lexicon: WORD, is the prefix of a word in
     * the lexicon: PREFIX, or is not a prefix and
     * not a word: NOT_WORD. See LexStatus
     * @param s represents the word/sequence  queried
     * @return status of s as to how it appears in lexicon
     */
    public LexStatus wordStatus(StringBuilder s);

    /**
     * Similar to <code>wordStatus</code> that takes a
     * StringBuilder parameter, but works with a String.
     * @param s is the string queried
     * @return status of s as to how it appears in lexicon
     */
    public LexStatus wordStatus(String s);
    
    /**
     * Return an iterator over all words stored
     * in the lexicon.
     * @return an iterator over the words in the lexicon
     */
    public Iterator<String> iterator();
    
    /**
     * Returns number of words stored in the lexicon
     * (as from last call to load, for example).
     * @return number of words in the lexicon
     */
    public int size();
}
