/*
 * This class encapsulates N words/strings so that the
 * group of N words can be treated as a key in a map or an
 * element in a set, or an item to be searched for in an array.
 * <P>
 * @author Yuanjie Jin
 */

public class WordNgram implements Comparable<WordNgram>{
    
    private String[] myWords;
    private int myHashCode;
    
    /*
     * Store the n words that begin at index start of array list as
     * the N words of this N-gram.
     * @param list contains at least n words beginning at index start
     * @start is the first of the N worsd to be stored in this N-gram
     * @n is the number of words to be stored (the n in this N-gram)
     */
    public WordNgram(String[] list, int start, int n) {
        myWords = new String[n];
        System.arraycopy(list, start, myWords, 0, n);

        int sum = 0;
    	
    	for (int k = 0; k < myWords.length; k++){
    		sum = sum * (k + 50) + myWords[k].hashCode();
    	}
    	myHashCode = sum;
    }
    
    /**
     * Return value that meets criteria of compareTo conventions.
     * @param wg is the WordNgram to which this is compared
     * @return appropriate value less than zero, zero, or greater than zero
     */
    public int compareTo(WordNgram wg) {
    	
    	for (int i = 0; i < myWords.length; i++){
    		
    		if(!myWords[i].equals(wg.myWords[i])) {
    			return myWords[i].compareTo(wg.myWords[i]);
    			
    		}
                
    	}
    	
    	return 0;
        
    }
    
    /**
     * Return true if this N-gram is the same as the parameter: all words the same.
     * @param o is the WordNgram to which this one is compared
     * @return true if o is equal to this N-gram
     */
    public boolean equals(Object o){
    	
        WordNgram wg = (WordNgram) o;

        for (int j = 0; j < myWords.length; j++){
        	if(!myWords[j].equals(wg.myWords[j])) {
        		return false;
        	}
    			
        }
      
        return true;
    }
    
    /**
     * Returns a good value for this N-gram to be used in hashing.
     * @return value constructed from all N words in this N-gram
     */
    public int hashCode(){
        
    	return myHashCode;
//    	int sum = 0;
//    	
//    	for (int k = 0; k < myWords.length; k++){
//    		sum = sum * (k + 50) + myWords[k].hashCode();
//    	}
//    	
//        return sum;
    }
    
    //a helper method that extracts a word from WordNgram and put a space after it
    public String wordAt(int k){
    	return myWords[k]+" ";
    }
}
