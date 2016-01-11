import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.*;
import java.nio.*;
import java.io.*;
import java.util.*;

import javax.swing.JFileChooser;

public class BurrowsWheeler {
    
    /**
     * This inner class represents the input block sequence/file being
     * transformed for compression. All <code>Rotatable</code> objects
     * in a BWTransform are presumably constructed from the same char array,
     * but with different indexes. Thus they all share the same array, but
     * each <code>Rotatable</code> has a different index that will be
     * used in the compareTo method and will be used in client code
     * as part of the tranform process. The index used in construction
     * represents the first character of a <code>Rotatable</code>. For example, 
     * index of zero uses the first index/char of the array and an index
     * of three represents three shifts/rotates so that the char at index 3
     * is the first character in terms of printing or comparisons.
     * @author ola
     *
     */
    public class Rotatable implements Comparable<Rotatable>{
        
        private char[] myArray;
        private int    myIndex;
        private String myString;
        
        /**
         * Construct from an array of chars and an index, the index
         * used to represent the true 'start' of the rotated-array of chars.
         * @param list
         * @param index
         */
        public Rotatable(char[] list, int index){
            myArray = list;
            myIndex = index;
        }
        
        /**
         * Return this object's index/first char (zero represents true first).
         * @return index used at construction, first char of rotated/shifted sequence
         */
        public int getIndex(){
            return myIndex;
        }
        
        /**
         * Return the last char of the array, where last is relative to the index 
         * representing the first character. For an index of zero, last should
         * have index array.length - 1. 
         * @return last char of this object
         */
        public char lastChar(){
            return myArray[(myIndex + myArray.length - 1) % myArray.length];
        }
        
        /**
         * Compare two Rotatable objects and return negative value if this
         * one is less than parameter, positive if this is larger, and zero
         * if they represent the same sequence. It's possible that all
         * characters of the (rotated) char-sequence are compared, each 
         * Rotatable starts the comparison at its appropriate index.
         * @return int representing value of comparison
         */
        public int compareTo(Rotatable b) {
            int aindex = myIndex;
            int bindex = b.myIndex;
            int length = myArray.length;
            /**
             * TODO: compare a.myArray values with b.myArray values
             * using the start indexes of each as appropriate and returning
             * negative, 0, positive depending on whether this sequence
             * is less than, equal to, or greater than sequence b, respectively.
             */
            
            for (int i=0; i<length; i++){
            	if (myArray[(aindex+i)%length]!=b.myArray[(bindex+i)%length])
            		return myArray[(aindex+i)%length]-b.myArray[(bindex+i)%length];
            }           
            
            return 0;  // initially everything is equal
        }
        
        /**
         * Convert to string once and store for all subsequent retrievals; the
         * string represents a rotated view, so the first character of the
         * returned string has the index used at construction time.
         * @return a String form of this object, string conversion done once
         */
        public String toString(){
            if (myString == null){
                StringBuilder sb= new StringBuilder();
                sb.append(myIndex + "\t: ");
                int index = myIndex;
                for(int k=0; k < myArray.length; k++){
                    sb.append((char) myArray[index]);
                    index++;
                    if (index >= myArray.length) index = 0;
                }
                myString = sb.toString();
            }
            return myString;
        }
        
    }
   
    
    public int BLOCK_SIZE = 1 << 15;   // number of chars in a block
    private int[] myIndexes;           // used in move-to-front
    private int myFirst;               // store value between method calls
    
    /**
     * Construct a Burrows-Wheeler transformer that can read/transform, 
     * untransform, and both move-to-front and undo move-to-front.
     */
    public BurrowsWheeler(){
        myIndexes = new int[IHuffConstants.ALPH_SIZE];
        myFirst = -1;
        resetIndexes();
    }
    
    /**
     * Set myIndexes appropriately so that move-to-front and unmove-to-front work,
     * i.e., so that myIndexes[k] == k for all valid values of k.
     */
    private void resetIndexes(){
        for(int k=0; k < myIndexes.length; k++){
            myIndexes[k] = k;
        }
    }
    
    /**
     * Read the BitInputStream until it's exhausted or until BLOCK_SIZE
     * chars have been read --- read BITS_PER_WORD bits, e.g., 8 in most
     * cases, for each char read. Returned this list of read characters
     * after it has been transformed by the Burrows-Wheeler transform. Before
     * the method returns set state appriopriately so that method <code>getFirst</code>
     * returns the index in the BW-transformed sorted list of the first, original
     * sequence read
     * @param bis is open for reading
     * @return the transformed array of chars read (last chars of sorted rows)
     * @throws IOException if reading doesn't succeed
     */
    
    public char[] transform(BitInputStream bis) throws IOException{
        
        ArrayList<Character> alist = new ArrayList<Character>();
        while (true){
            int val = bis.readBits(IHuffConstants.BITS_PER_WORD);
            if (val == -1) break;
            alist.add((char) val);
            if (alist.size() == BLOCK_SIZE) break;
        }
        char[] list = new char[alist.size()];
        
        for(int k=0; k < list.length; k++){
            list[k] = alist.get(k);
        }
        
        /**
         * TODO: create and sort array of Rotatable objects, one for each
         * rotated sequence. Create each Rotatable from the array <code>list</code>
         * and an index from 0,1,...list.length-1; so that reach Rotatable 
         * represents a sequence for the BW transform, i.e., in which each char
         * has been rotated once (even though you don't do any rotations, you just
         * create a Rotatable from an index and the same char[] list. After
         * you create each Rotatable in <code>blockList</code> *MAKE SURE YOU
         * SORT THE ARRAY*.
         */
       
        Rotatable[] blockList = new Rotatable[list.length];
        for (int i=0; i<blockList.length; i++)
        	blockList[i] = new Rotatable(list,i);
        Arrays.sort(blockList);
       

        char[] blist = new char[blockList.length];
        for(int k=0; k < blockList.length; k++){
            blist[k] = blockList[k].lastChar();
        }
        /**
         * TODO: find which entry in blockList, the sorted list of Rotatable objects
         * stores the original block. This means you query each Rotatable
         * object to find its original-index: getIndex() 
         * and when you find 0/zero, you set
         * the value of myFirst to the index/location *IN THE SORTED LIST OF ROTATABLES*
         * at which the original-index zero occurs. For example, after
         * the Rotatable objects are sorted the original-index 0/zero sequence of
         * chars might have index 17 in the sorted. list: set myFirst = 17 in that
         * case. Generalize appropriately so myFirst gets the sorted-index at
         * which the original-first zero index occurs.
         */
        
        for (int j=0; j<blockList.length; j++)
        	if (blockList[j].getIndex()==0){
        		myFirst = j;
        		break;
        	}
        
      
       
        // don't forget to set myFirst before returning
        
        
        return blist;
    }
    
    /**
     * Return the index in the rotated/sorted list of strings representing
     * the most recently read block at which the original read string occurs.
     * @return index in sorted list at which original string occurs
     */
    public int getFirst(){
        return myFirst;
    }
 
    /**
     * Return an array of characters representing the move-to-front encoding
     * of the array passed in; all chars in the parameter should represent
     * an unsigned 8- bit value, e.g., between 0 and 255 inclusive.
     * @param list is the array of values to be transformed via move-to-front
     * @return the transformed list of chars
     * @throws RuntimeException if a value in the parameter list is less than
     * zero or greater than 255.
     */
    public char[] mtf(char[] list){
        resetIndexes();
        char[] copy = new char[list.length];
        for (int k=0; k < list.length; k++){
            
            if (list[k] < 0 || list[k] > 255){
                throw new RuntimeException("mtf trouble index "+k+" value "+list[k]);
            }
            
            for(int j=0; j < myIndexes.length; j++){
                if (myIndexes[j] == list[k]) {
                    copy[k] = (char) j;
                    for(int i=j; i >= 1; i--){
                        myIndexes[i] = myIndexes[i-1];
                    }
                    myIndexes[0] = list[k];
                    break;
                }
            }
        }       
        return copy;
    }
    
    /**
     * Returns an un-move-to-front transformed array of char values where the
     * array parameter represents a sequence previously encoded using the
     * method <code>mtf</code> of this class.
     * @param list is the array of previously move-to-front encoded values
     * @return the array of values after decoding by move-to-front
     */
    public char[] unmtf(char[] list){
        resetIndexes();
        char[] copy = new char[list.length];
        for(int k=0; k < list.length; k++){
            int save = myIndexes[list[k]];
            copy[k] = (char) save;           
            for(int j=list[k]; j > 0; j--){
                myIndexes[j] = myIndexes[j-1];
            }
            myIndexes[0] = save;
        }
       
        return copy;
    }
    
    /**
     * Use the reverse/un Burrows-Wheeler transform to decode characters, 
     * returning the decoded characters -- runs in O(n) time for an n-element array.
     * @param list is the last-chars of a BW-transformed sequence
     * @param first is the index in the sorted sequence of chars at which
     * the originally read/encoded sequence occurs.
     * @return a BW decoded array of characters
     */
    public char[] decode(char[] list, int first){
       
        int[] translate = new int[list.length];
        int[] sums = new int[IHuffConstants.ALPH_SIZE + 1];
        for(int k=0; k < list.length; k++){
            sums[list[k]+1]++;
        }
        for(int k=1; k < sums.length; k++){
            sums[k] += sums[k-1];
        }
        for(int k=0; k < list.length; k++){
            int index = sums[list[k]]++;
            translate[index] = k;
        }
        char[] reconstruct = new char[list.length];
        for (int k = 0; k <list.length; k++) {
            first = translate[first];
            reconstruct[k] = list[first];
        }
        return reconstruct;
    }
}
