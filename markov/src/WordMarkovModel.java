import java.util.*;


public class WordMarkovModel extends AbstractModel {
	
	private String myString;
	private String[] myWordArray;
    private Random myRandom;	
    private Map<WordNgram,ArrayList<WordNgram>> myMap;
	public static final int DEFAULT_COUNT = 100;
	private int pren = -1;
	private String preString = "";

	
	public WordMarkovModel() {
	       
        myRandom = new Random(1234);
        myMap = new HashMap<WordNgram,ArrayList<WordNgram>>();
    }
	
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
	
	public void process(Object o) {
        String temp = (String) o;
        String[] nums = temp.split("\\s+");
        int n = Integer.parseInt(nums[0]);
        int numWords = DEFAULT_COUNT;
        if (nums.length > 1) {
            numWords = Integer.parseInt(nums[1]);
        }
        clear();  //clear the output in the views each time you hit GO
        WordMarkov(n, numWords);
    }
	
	public void WordMarkov(int n, int numWords) {
		myWordArray = myString.split("\\s+");
		int j = myWordArray.length;		
		String[] wrapAroundWords = new String[j+n];
		System.arraycopy(myWordArray,0,wrapAroundWords,0,j);
		
		for (int l = 0; l < n; l++){
			wrapAroundWords[j+l]=myWordArray[l]; 
		}
				
		//when n changes or loading another file, reconstruct the map
		if ((n != pren)||(myString != preString)){
			myMap.clear();
			for (int i = 0; i < j; i++){
				WordNgram ngram = new WordNgram(wrapAroundWords,i,n);
				if (!myMap.containsKey(ngram)){
					myMap.put(ngram, new ArrayList<WordNgram>());
				}
				WordNgram followingngram = new WordNgram(wrapAroundWords,i+1,n);
				ArrayList<WordNgram> ngramList = myMap.get(ngram);		
				ngramList.add(followingngram);
			}
			pren = n;
			preString = myString;
		}
					
		//keep track of time and number of keys 
		double stime = System.currentTimeMillis();
		produceRandomWordText(n, numWords);
		double etime = System.currentTimeMillis();
        double time = (etime - stime) / 1000.0;
        int numofkeys = myMap.size();
        this.messageViews("time to generate: " + time);
        System.out.println("mapsize: "+ numofkeys + "; time to generate: " + time);
	}
	
	//a helper method for generating random texts from the established map
	private void produceRandomWordText(int order, int numofWords) {
		String build = "";
		ArrayList<WordNgram> seedList;
		int start = myRandom.nextInt(myWordArray.length - order + 1);
		WordNgram nwords = new WordNgram(myWordArray, start, order);
		for (int t = 0; t < numofWords; t++) {
	         seedList = myMap.get(nwords);
	         int pick = myRandom.nextInt(seedList.size());
	         nwords = seedList.get(pick);
	         build += nwords.wordAt(order-1);
		 }
		
		this.notifyViews(build);
	}

}
