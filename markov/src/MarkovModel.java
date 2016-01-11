import java.util.*;
import java.io.*;

/**
 * The model for the Markov text generation assignment. See methods
 * for details. This model uses a brute-force algorithm for generating
 * text, e.g., the entire training text is rescanned each time a new
 * character is generated.
 * @author ola
 *
 */
public class MarkovModel extends AbstractModel {

    
    private String myString;
    private Random myRandom;
    public static final int DEFAULT_COUNT = 100; // default # random letters generated

    public MarkovModel() {
       
        myRandom = new Random(1234);
    }

    /**
     * Create a new training text for this model based on the information read
     * from the scanner.
     * @param s is the source of information
     */
    public void initialize(Scanner s) {
        double start = System.currentTimeMillis();
        int count = readChars(s);
        double end = System.currentTimeMillis();
        double time = (end - start) / 1000.0;
        super.messageViews("#read: " + count + " chars in: " + time + " secs");
    }

    protected int readChars(Scanner s) {
        myString = s.useDelimiter("\\Z").next();
        s.close();      
        return myString.length();
    }

    /**
     * Generate N letters using an order-K markov process where
     * the parameter is a String containing K and N separated by
     * whitespace with K first. If N is missing it defaults to some
     * value.
     */
    public void process(Object o) {
        String temp = (String) o;
        String[] nums = temp.split("\\s+");
        int k = Integer.parseInt(nums[0]);
        int numLetters = DEFAULT_COUNT;
        if (nums.length > 1) {
            numLetters = Integer.parseInt(nums[1]);
        }
        brute(k, numLetters);
    }


    public void brute(int k, int numLetters) {

        int start = myRandom.nextInt(myString.length() - k + 1);
        String str = myString.substring(start, start + k);
        String wrapAroundString = myString + myString.substring(0,k);   
        StringBuilder build = new StringBuilder();
        ArrayList<Character> list = new ArrayList<Character>();
        double stime = System.currentTimeMillis();
        for (int i = 0; i < numLetters; i++) {
            list.clear();
            int pos = 0;
            while ((pos = wrapAroundString.indexOf(str, pos)) != -1 && pos < myString.length()) {
                char ch = wrapAroundString.charAt(pos + k);
                list.add(ch);
                pos++;
            }
            int pick = myRandom.nextInt(list.size());
            char ch = list.get(pick);
            build.append(ch);
            str = str.substring(1) + ch;
        }
        double etime = System.currentTimeMillis();
        double time = (etime - stime) / 1000.0;
        this.messageViews("time to generate: " + time);
        this.notifyViews(build.toString());
    }
}
