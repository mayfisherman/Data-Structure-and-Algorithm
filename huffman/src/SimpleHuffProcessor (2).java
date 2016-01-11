import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class SimpleHuffProcessor implements IHuffProcessor {

	private HuffViewer myViewer;
	private int[] letterCount;
	private Map<Integer, String> mapping;
	private TreeNode myRoot;
	private int simpleBits;
	private int savedBits;

	public int preprocessCompress(InputStream in) throws IOException {
		// This is probably where you want to generate your Map of character to
		// codes
		// Step 1: Count number of each character in the file
		letterCount = countLetters(in);
//		for (int i = 0; i < letterCount.length; i++) {
//			if (letterCount[i] > 0)
//				myViewer.update("Letter " + i + "(" + (char) i + "): "
//						+ letterCount[i]);
//		}
		
		// Step 2: Generate the Huffman tree
		myRoot= HuffTree(letterCount);
	//	myViewer.update("\n\nHUFF TREE LOOKS LIKE: \n\n"+ treeToString(myRoot, ""));

		// Step 3: Use the tree to generate the mapping of character to encoding

		// I personally found it easiest to map a character to a string of 0s
		// and 1s
		// Then when I output I just output the 1 or 0 one bit at a time
		mapping = new TreeMap<Integer, String>();
		simpleBits = BITS_PER_INT;
		createMapping(myRoot, mapping, "");

	//	myViewer.update("\n\nTHE CODE TABLE LOOKS LIKE: \n\n");
		for (Integer character : mapping.keySet()) {
			if (character < 256) {
//				myViewer.update("Letter " + character + "("
//						+ (char) character.intValue() + "): "
//						+ mapping.get(character));
				simpleBits += mapping.get(character).length()
						* letterCount[character];
			} else {
//				myViewer.update("Letter " + character + "(Pseudo-EOF): "
//						+ mapping.get(character));
				simpleBits += mapping.get(character).length();
			}
		}
		// +extra bits to fulfill the nearest multiples of 8.
		if (simpleBits % 8 != 0)
			simpleBits = (simpleBits / 8 + 1) * 8;

		// your code is supposed to return the number of bits processed
		// but mine doesn't do that

		savedBits = (myRoot.myWeight - 1) * BITS_PER_WORD - simpleBits;
	//	myViewer.update("\n\noriginal file size in bits: " + (myRoot.myWeight - 1)* BITS_PER_WORD);

		return savedBits;
	}

	// Takes in an input stream
	// Returns an array of size IHuffConstants.ALPH_SIZE
	// each element of the array corresponds to to the number
	// of that letter that occur in the file
	public int[] countLetters(InputStream raw) throws IOException {
		// Here's how you read one character from a file
		int[] result = new int[ALPH_SIZE];
		BitInputStream in = new BitInputStream(raw);
		int currentWord = in.readBits(BITS_PER_WORD);
		while (currentWord != -1) {
			result[currentWord]++;
			currentWord = in.readBits(BITS_PER_WORD);
		}
		return result;
	}
	
	
	
	public TreeNode HuffTree(int[] count){
		PriorityQueue<TreeNode> q = new PriorityQueue<TreeNode>();
		for (int i = 0; i < count.length; i++) {
			if (count[i] > 0) {
				q.add(new TreeNode(i, count[i]));
			}
		}
		q.add(new TreeNode(PSEUDO_EOF, 1));
		while (q.size() > 1) {
			TreeNode a = q.poll();
			TreeNode b = q.poll();
			q.add(new TreeNode(-1, a.myWeight + b.myWeight, a, b));
		}
		return  q.poll();		
	}

	public String treeToString(TreeNode root, String prefix) {
		if (root == null)
			return "";
		prefix += "     ";
		String leftSubtree = treeToString(root.myLeft, prefix);
		String rightSubtree = treeToString(root.myRight, prefix);
		if ((leftSubtree.length() != 0) && (rightSubtree.length() != 0))
			return root.myValue + "\n" + prefix + leftSubtree + "\n" + prefix
					+ rightSubtree;
		if (leftSubtree.length() != 0)
			return root.myValue + "\n" + prefix + leftSubtree;
		if (rightSubtree.length() != 0)
			return root.myValue + "\n" + prefix + rightSubtree;
		return "" + root.myValue;
		// hey, you know what...I bet I could probably modify the
		// algorithm I used to solve DrawTree to print this tree
		// (But any reasonably understandable way to print a tree would work)
	}

	private void createMapping(TreeNode root, Map<Integer, String> map,
			String path) {
		if (root == null)
			return;
		if ((root.myLeft == null) && (root.myRight == null)) {
			map.put(root.myValue, path);
			simpleBits += BITS_PER_WORD + 2;
			return;
		}
		simpleBits++;
		createMapping(root.myLeft, map, path + "0");
		createMapping(root.myRight, map, path + "1");
	}

	public void setViewer(HuffViewer viewer) {
		myViewer = viewer;
	}

	public int compress(InputStream in, OutputStream out, boolean force)
			throws IOException {
		if ((savedBits <= 0) && (force == false)) {
			myViewer.showError("compression uses " + (-savedBits)
					+ " more bits\nuse force compression to compress");
			return 0;
		} else {
			BitOutputStream bitsOut = new BitOutputStream(out);
			simpleBits = 0;
			// write store tree magic number
			bitsOut.writeBits(BITS_PER_INT, STORE_TREE);
			simpleBits += BITS_PER_INT;

			// write header, non-saving-space code using SCF
			 for(int k=0; k < ALPH_SIZE; k++){
			 bitsOut.writeBits(BITS_PER_INT, letterCount[k]);
			 simpleBits+=BITS_PER_INT;
			 }

			// write header extra credit, Standard Tree preorder traversal
			//writeTree(bitsOut, myRoot);

			// write compressed file
			BitInputStream bitsin = new BitInputStream(in);
			int curWord = bitsin.readBits(BITS_PER_WORD);
			while (curWord != -1) {
				String code = mapping.get(curWord);
				for (char c : code.toCharArray()) {
					int bin = Integer.parseInt("" + c);
					bitsOut.writeBits(1, bin);
				}
				simpleBits += code.length();
				curWord = bitsin.readBits(BITS_PER_WORD);
			}

			// Write pseudo EOF
			String bitEOF = mapping.get(PSEUDO_EOF);
			for (char c : bitEOF.toCharArray()) {
				int bin = Integer.parseInt("" + c);
				bitsOut.writeBits(1, bin);
			}
			simpleBits += bitEOF.length();

			// your code is supposed to return the number of bits processed
			// but mine doesn't do that
			bitsOut.close();
			return simpleBits;
		}
	}

//	public void writeTree(BitOutputStream out, TreeNode current) {
//		if (current == null)
//			return;
//		if ((current.myLeft == null) && (current.myRight == null)) {
//			out.writeBits(1, 1);
//			simpleBits += 1;
//			out.writeBits(BITS_PER_WORD + 1, current.myValue);
//			simpleBits += BITS_PER_WORD + 1;
//			return;
//		}
//		out.writeBits(1, 0);
//		simpleBits++;
//		writeTree(out, current.myLeft);
//		writeTree(out, current.myRight);
//	}

	// The first thing you'll want to do is check to see if
	// IHuffConstants.MAGIC_NUMBER
	// is the first number in the file. (hint: IHuffConstants.MAGIC_NUMBER is
	// IHuffConstants.BITS_PER_INT bits long)
	public int uncompress(InputStream in, OutputStream out) throws IOException {
		// return the number of bits you write to the file
		// Step 1: Check for magic number
		BitInputStream bitsIn = new BitInputStream(in);
		int magic = bitsIn.readBits(BITS_PER_INT);
		if (magic != STORE_TREE) {
			throw new IOException("magic number not right");
		}

		// Step 2: Load the counts for each character
		// hint: once you're finished, I would print out those counts so you can
		// verify everything is right
		letterCount = new int[ALPH_SIZE];
		for (int k = 0; k < ALPH_SIZE; k++) {
			int bits = bitsIn.readBits(BITS_PER_INT);
			letterCount[k] = bits;
			if (letterCount[k]>0)
			System.out.println("Letter " + k + "("
					+ (char) k + ")weight: "
					+ letterCount[k]);
		}

		
		// Step 3: Create the tree
		// Couldn't hurt to print out the tree too
		TreeNode readRoot = HuffTree(letterCount);		
	//	myViewer.update("\n\nHUFF TREE LOOKS LIKE: \n\n"+ treeToString(readRoot, ""));

		// Step 4: Use the tree to generate the mapping of bits to characters
		// Yep, print that out too
		mapping = new TreeMap<Integer, String>();
		recreateMapping(readRoot, mapping, "");
	//	myViewer.update("\n\nTHE CODE TABLE LOOKS LIKE: \n\n");

//		for (Integer character : mapping.keySet()) {
//			if (character < 256) {
//				myViewer.update("Letter " + character + "("
//						+ (char) character.intValue() + "): "
//						+ mapping.get(character));
//			} else {
//				myViewer.update("Letter " + character + "(Pseudo-EOF): "
//						+ mapping.get(character));
//			}
//		}
		// Step 5: Uncompress that file
		BitOutputStream bitsOut = new BitOutputStream(out);
		TreeNode tnode = readRoot;
		int totalBits = 0;
		while (tnode != null) {
			int bits = bitsIn.readBits(1);
			if (bits == -1) {
				throw new IOException("error reading bits, no PSEUDO-EOF");
			}
			// use the zero/one value of the bit read
			// to traverse Huffman coding tree
			// if a leaf is reached, decode the character and print UNLESS
			// the character is pseudo-EOF, then decompression done
			if ((tnode.myLeft == null) && (tnode.myRight == null)) {
				if (tnode.myValue == PSEUDO_EOF)
					break;
				// out of while-loop
				else {
					bitsOut.write(tnode.myValue);
					totalBits += BITS_PER_WORD;
					tnode = readRoot;
					// start back at top
				}
			}
			if ((bits & 1) == 0)
				tnode = tnode.myLeft;
			else
				tnode = tnode.myRight;
		}

		// throw new IOException("uncompress not implemented");
//		myViewer.update("\n\n uncompressed file size " + totalBits);
		return totalBits;
	}

//	private TreeNode readTree(BitInputStream in) throws IOException {
//		int readValue = in.readBits(1);
//		if ((readValue & 1) == 1) {
//			return new TreeNode(in.readBits(BITS_PER_WORD + 1), 0, null, null);
//		}
//		TreeNode left = readTree(in);
//		TreeNode right = readTree(in);
//		return new TreeNode(-1, 0, left, right);
//	}

	private void recreateMapping(TreeNode root, Map<Integer, String> map,
			String path) {
		if (root == null)
			return;
		if ((root.myLeft == null) && (root.myRight == null)) {
			map.put(root.myValue, path);
			return;
		}
		createMapping(root.myLeft, map, path + "0");
		createMapping(root.myRight, map, path + "1");
	}

	private void showString(String s) {
		myViewer.update(s);
	}

}
