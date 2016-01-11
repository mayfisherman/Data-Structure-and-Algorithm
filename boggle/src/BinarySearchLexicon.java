import java.util.*;

public class BinarySearchLexicon implements ILexicon {

	private ArrayList<String> myWords;

	public BinarySearchLexicon() {
		myWords = new ArrayList<String>();
	}

	public void load(Scanner s) {
		myWords.clear();
		while (s.hasNext()) {
			myWords.add(s.next().toLowerCase());
		}
		Collections.sort(myWords);
	}

	public void load(ArrayList<String> list) {
		myWords.clear();
		myWords.addAll(list);
		Collections.sort(myWords);
	}

	public LexStatus wordStatus(StringBuilder s) {
		return wordStatus(s.toString());
	}

	public LexStatus wordStatus(String s) {

		// You need to make this code use Binary Search
		int searchResult = Collections.binarySearch(myWords, s);
		if (searchResult >= 0)
			return LexStatus.WORD;
		int Index = -searchResult - 1;
		if ((Index < myWords.size()) && (myWords.get(Index).startsWith(s)))
			return LexStatus.PREFIX;
		return LexStatus.NOT_WORD;
	}

	public Iterator<String> iterator() {
		return myWords.iterator();
	}

	public int size() {
		return myWords.size();
	}

}
