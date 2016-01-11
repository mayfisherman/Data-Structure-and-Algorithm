import java.util.*;

/**
 * This is a skeleton program, you'll
 * need to modify, either adding code in main
 * or in functions called from main
 * @author Yuanjie Jin
 *
 */

public class HangmanStats {
	public static void main(String[] args) {
		HangmanFileLoader loader = new HangmanFileLoader();
		loader.readFile("lowerwords.txt");
		HashSet<String> set = new HashSet<String>();
	    int[] round = {1000,5000,10000,20000,50000,100000,500000};
	    for(int i = 4;i<21;i++){
	        for (int j = 0;j < round.length; j++){
	    			for(int k = 0; k < round[j]; k += 1) {
				    set.add (loader.getRandomWord(i));
			    	      	}
			System.out.printf("number of %d letter words = %d\n", i,set.size());
			set.clear();
		}
	    }
	 }
}
