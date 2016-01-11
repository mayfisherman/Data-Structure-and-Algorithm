import java.util.*;


public class MapMarkovModel extends AbstractModel {
	
	private String myString;
    private Random myRandom;	
    private Map<String,ArrayList<String>> myMap;
	public static final int DEFAULT_COUNT = 100;
	private String preString = "";
	private int prek = -1;
		
	public MapMarkovModel() {
	       
        myRandom = new Random(1234);
        myMap = new HashMap<String,ArrayList<String>>();
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
        int k = Integer.parseInt(nums[0]);
        int numLetters = DEFAULT_COUNT;
        if (nums.length > 1) {
            numLetters = Integer.parseInt(nums[1]);
        }
        clear();  //clear the output in the views each time you hit GO
        smarter(k, numLetters);
    }
	
	public void smarter(int k, int numLetters) {
		
		String wrapAroundString = myString + myString.substring(0,k);
		double stime = System.currentTimeMillis();
		
		//when k changes or loading another file, reconstruct the map
		if ((k != prek)||(myString != preString)){
			myMap.clear();
			for (int i = 0; i < myString.length(); i++){
				String kgram = wrapAroundString.substring(i, i + k);
				if (!myMap.containsKey(kgram)){
					myMap.put(kgram, new ArrayList<String>());
				}
				String followingkgram = kgram.substring(1) + wrapAroundString.charAt(i + k);
				ArrayList<String> kgramList = myMap.get(kgram);		
				kgramList.add(followingkgram);
			}
			prek = k;
			preString = myString;
		}
							
		produceRandomText(k, numLetters);
		double etime = System.currentTimeMillis();
        double time = (etime - stime) / 1000.0;
        this.messageViews("time to generate: " + time);
	}
	
	//a helper method for generating random texts from the established map
	private void produceRandomText(int order, int numofLets) {
		StringBuilder build = new StringBuilder();
		int start = myRandom.nextInt(myString.length() - order + 1);
		String str = myString.substring(start, start + order);
		for (int j = 0; j < numofLets; j++) {
	         ArrayList<String> seedList = myMap.get(str);
	         int randomkgram = myRandom.nextInt(seedList.size());
	         str = seedList.get(randomkgram);
	         build.append(str.charAt(order-1));
		 }
		this.notifyViews(build.toString());
	}

}
