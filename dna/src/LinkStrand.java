import java.util.*;

public class LinkStrand implements IDnaStrand {

	public LinkStrand() {
		this("");
	}

	public LinkStrand(String s) {
		myFirst = myLast = new DnaNode(s, null);
		mySize = s.length();
	}

	private static class DnaNode {
		public String info;
		public DnaNode next;

		public DnaNode(String s, DnaNode node) {
			info = s;
			next = node;
		}
	}

	private DnaNode myFirst, myLast; // first and last nodes of list
	private long mySize;
	private int myAppends = 0;
	private int myCutNo = 0;
	

	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		if (myFirst.next != null)
			throw new RuntimeException("link strand has more than one node");
		int pos = 0;
		int start = 0;
		String search = myFirst.info;
		boolean first = true;
		LinkStrand ret = null;
		while ((pos = search.indexOf(enzyme, pos)) >= 0) {
			if (first) {
				ret = new LinkStrand(search.substring(start, pos));
				first = false;
			} else {
				ret.append(search.substring(start, pos));

			}
			start = pos + enzyme.length();
			ret.append(splicee);
			// add splicee to the nodes of the returned LinkStrand
			ret.myCutNo++;
			// keep track of the number of cut sites
			pos++;
		}
		// dont' forget there may be stuff to add here, check and add
		if (start < search.length()) {
			if (ret == null) {
				ret = new LinkStrand("");
			} else {
				ret.append(search.substring(start));
			}
		}
		return ret;

	}

	public long size() {
		return mySize;
	}

	public void initializeFrom(String source) {
		myLast = myFirst = new DnaNode(source, null);
		mySize = source.length();
	}

	public String strandInfo() {
		return this.getClass().getName();
	}

	public String toString() {
		
		if (myFirst == null)
			return null;
		
		StringBuilder wholeDNA = new StringBuilder();
		DnaNode current = myFirst;
		while (current != null) {
			wholeDNA.append(current.info);
			current = current.next;
		}
		return wholeDNA.toString();

	}

	public IDnaStrand append(IDnaStrand dna) {
		if (dna instanceof LinkStrand) {
			LinkStrand ls = (LinkStrand) dna;
			myLast.next = ls.myFirst;
			myLast = myLast.next;
			mySize += ls.mySize;
			myAppends++;
			return this;
		} else {
			return append(dna.toString());
		}
	}

	public IDnaStrand append(String dna) {
		myLast.next = new DnaNode(dna, null);
		myLast = myLast.next;
		mySize += dna.length();
		myAppends++;
		return this;
	}

	public IDnaStrand reverse() {
		
		if (myFirst == null)
			return null;

		LinkStrand ls = new LinkStrand();
		ls.myFirst = null;  //do not need the initial node with an empty string
		HashMap<String, String> reverseMap = new HashMap<String, String>();
		DnaNode current = myFirst;

		while (current != null) {

			StringBuilder copy = new StringBuilder(current.info);
			String DnaCopy = copy.toString();
			DnaNode temp;
			
			//if the current string is a duplicate, use the map
			//to retrieve the reverse string
			if (reverseMap.containsKey(DnaCopy))
				temp = new DnaNode(reverseMap.get(DnaCopy), ls.myFirst);
			else {
				String reverseDna = copy.reverse().toString();
				reverseMap.put(DnaCopy, reverseDna);
				temp = new DnaNode(reverseDna, ls.myFirst);
			}
			ls.myFirst = temp;
			
			//when it is the last node, assign its pointer to myLast
			if (ls.myFirst.next == null)
				ls.myLast = temp;
			current = current.next;

		}

		ls.mySize = mySize;
		return ls;

	}


	public String getStats() {
		return String.format("# append calls = %d, # cut points = %d", myAppends,myCutNo);
	} // # nucleotides in DNA

}
