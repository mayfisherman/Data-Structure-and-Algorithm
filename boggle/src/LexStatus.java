/**
 * Enum values for querying a lexicon
 * and determining if a string is a word,
 * is a prefix of a word, or is not a word (and
 * not a prefix of a word).
 * <P>
 * If PREFIX is returned, then the string is not
 * a word, but is the prefix of at least one word
 * in the lexicon.
 * <P>
 * If WORD is returned, then the string is a word
 * in the dictionary (it could be a prefix too,
 * e.g., MOM is a word, but is a prefix of MOMENT).
 * <P>
 * If NOT_WORD is returned, the string is not
 * a word, and it's not the prefix of any word
 * in the lexicon.
 * <P>
 * @author ola
 *
 */
public enum LexStatus {
    WORD,
    PREFIX,
    NOT_WORD
}
