import java.util.*;

public class HangmanMyStats {

	public static void main(String[] args) {
		HangmanFileLoader loader = new HangmanFileLoader();
		loader.readFile("lowerwords.txt");
		ArrayList<String> wordlist = new ArrayList<String>();
 		
	    for(int i = 4;i < 21;i++){
	    	double total = 0.0;
	      //for (int j = 0;j < round.length;j++){
	    	for (int j = 0;j < 1000; j++){
	          
	      	  int duplicateCall;
	      	  boolean duplicate = false;
	   	      for(int m = 0; m < 10000; m += 1) {
	   	    	  wordlist.add(loader.getRandomWord(i));
				  for(int n = 0; n < m; n += 1) {
					  if(wordlist.get(n).equals(wordlist.get(m))){
						  duplicateCall=m-n;
						  //System.out.println(duplicateCall);
						  total += duplicateCall;
						  //set.clear();
						  duplicate = true;
						  break;
					}
				}
				if (duplicate)
					break;  
			    }
	    	wordlist.clear();
	    	}
	        double average = total/1000;
		    System.out.printf("On average %f calls are needed before returning the same %d letter words again\n",average,i);
		    //set.clear();
		
	    }

	}

}
