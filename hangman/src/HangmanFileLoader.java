import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * FileLoader for hangman game
 * @author Michael Hewner
 * @author Owen Astrachan
 *
 */

public class HangmanFileLoader {
	
	Map<Integer,ArrayList<String>> myWordMap;
	Random myRandom;
	
	public HangmanFileLoader() {
		myWordMap = new HashMap<Integer,ArrayList<String>>();
		myRandom = new Random();
	}
	
	/**
	 * Must call this method with a filename before calling other methods
	 * to retrieve random words
	 * @param filename name of file to be read
	 * @return true if and only if file was read successfully
	 */
	public boolean readFile(String filename) {
		try {
			FileReader dataFile = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(dataFile);
			String currentLine = bufferedReader.readLine();

			while(currentLine != null) {
				String trimmedWord = currentLine.trim();
				Integer length = new Integer(trimmedWord.length());
				if(!myWordMap.containsKey(length)) {
					myWordMap.put(length, new ArrayList<String>());
				}
				myWordMap.get(length).add(trimmedWord);
				currentLine = bufferedReader.readLine();
			}
			bufferedReader.close();
		} 
		catch (IOException e) {
			System.err.println("A error occured reading file: " + e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Return a randomly chosen word of a specified length, or null
	 * if there are no words of that length.
	 * @param wordLength is length of returned word
	 * @return randomly chosen word (or null if no words)
	 */
	public String getRandomWord(int wordLength) {
		ArrayList<String> wordList = myWordMap.get(new Integer(wordLength));
		if(wordList == null || wordList.size() == 0) return null; //there are no words of this length
		int selection = myRandom.nextInt(wordList.size());
		return wordList.get(selection);
	}
}