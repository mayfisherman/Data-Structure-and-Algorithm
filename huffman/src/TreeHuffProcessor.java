import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class TreeHuffProcessor extends SimpleHuffProcessor {

	public int preprocessCompress(InputStream in) throws IOException {

		myLetterCount = countLetters(in);
		printCounts(myLetterCount);

		myRoot = HuffTree(myLetterCount);
		myViewer.update("\n\nHUFF TREE LOOKS LIKE: \n\n"
				+ treeToString(myRoot, ""));

		myMapping = new TreeMap<Integer, String>();
		myCompBits = BITS_PER_INT; // the number of bits for magic number
														  
		createMapping(myRoot, myMapping, "");
		myViewer.update("\n\nTHE CODE TABLE LOOKS LIKE: \n\n");
		printMapping(myMapping);

		// add extra bits to fulfill the nearest multiples of 8.
		if (myCompBits % 8 != 0)
			myCompBits = (myCompBits / 8 + 1) * 8;

		mySavedBits = (myRoot.myWeight - 1) * BITS_PER_WORD - myCompBits;
		myViewer.update("\n\noriginal file size in bits: "
				+ (myRoot.myWeight - 1) * BITS_PER_WORD);
		return mySavedBits;
	}

	protected void createMapping(TreeNode root, Map<Integer, String> map,
			String path) {
		if (root == null)
			return;
		if ((root.myLeft == null) && (root.myRight == null)) {
			map.put(root.myValue, path);
			myCompBits += BITS_PER_WORD + 2 + root.myWeight * path.length();
			return;
		}
		myCompBits++;
		createMapping(root.myLeft, map, path + "0");
		createMapping(root.myRight, map, path + "1");
	}

	public int compress(InputStream in, OutputStream out, boolean force)
			throws IOException {
		if ((mySavedBits <= 0) && (force == false)) {
			myViewer.showError("compression uses " + (-mySavedBits)
					+ " more bits\nuse force compression to compress");
			return 0;
		} else {
			BitOutputStream bitsOut = new BitOutputStream(out);
			myCompBits = 0;
			// write store tree magic number
			bitsOut.writeBits(BITS_PER_INT, STORE_TREE);
			myCompBits += BITS_PER_INT;

			// write header extra credit, Standard Tree preorder traversal
			writeTree(bitsOut, myRoot);

			// write compressed file
			BitInputStream bitsin = new BitInputStream(in);
			int curWord = bitsin.readBits(BITS_PER_WORD);
			while (curWord != -1) {
				writeCompFile(curWord, bitsOut);
				curWord = bitsin.readBits(BITS_PER_WORD);
			}

			// Write pseudo EOF
			writeCompFile(PSEUDO_EOF, bitsOut);
			bitsOut.close();
			if (myCompBits % 8 != 0)
				myCompBits = (myCompBits / 8 + 1) * 8;
			return myCompBits;
		}
	}

	private void writeTree(BitOutputStream out, TreeNode current) {
		if (current == null)
			return;
		if ((current.myLeft == null) && (current.myRight == null)) {
			out.writeBits(1, 1);
			myCompBits += 1;
			out.writeBits(BITS_PER_WORD + 1, current.myValue);
			myCompBits += BITS_PER_WORD + 1;
			return;
		}
		out.writeBits(1, 0);
		myCompBits++;
		writeTree(out, current.myLeft);
		writeTree(out, current.myRight);
	}

	public int uncompress(InputStream in, OutputStream out) throws IOException {

		BitInputStream bitsIn = new BitInputStream(in);
		int magic = bitsIn.readBits(BITS_PER_INT);
		if (magic != STORE_TREE) {
			throw new IOException("magic number not right");
		}

		myRoot = readTree(bitsIn);
		 myViewer.update("\n\nHUFF TREE LOOKS LIKE: \n\n"
		 + treeToString(myRoot, ""));

		myMapping = new TreeMap<Integer, String>();
		recreateMapping(myRoot, myMapping, "");
		 myViewer.update("\n\nTHE CODE TABLE LOOKS LIKE: \n\n");
		 printMapping(myMapping);

		// Step 5: Uncompress that file
		BitOutputStream bitsOut = new BitOutputStream(out);
		TreeNode tnode = myRoot;
		myUncompBits = 0;
		while (tnode != null) {
			int bits = bitsIn.readBits(1);
			if (bits == -1) {
				throw new IOException("error reading bits, no PSEUDO-EOF");
			}

			if ((bits & 1) == 0)
				tnode = tnode.myLeft;
			else
				tnode = tnode.myRight;

			if ((tnode.myLeft == null) && (tnode.myRight == null)) {
				if (tnode.myValue == PSEUDO_EOF)
					break;
				// out of while-loop
				else {
					bitsOut.write(tnode.myValue);
					myUncompBits += BITS_PER_WORD;
					tnode = myRoot;
					// start back at top
				}
			}
		}
		myViewer.update("\n\n uncompressed file size " + myUncompBits);
		return myUncompBits;
	}

	private TreeNode readTree(BitInputStream in) throws IOException {
		int readValue = in.readBits(1);
		if ((readValue & 1) == 1) {
			return new TreeNode(in.readBits(BITS_PER_WORD + 1), 0, null, null);
		}
		TreeNode left = readTree(in);
		TreeNode right = readTree(in);
		return new TreeNode(-1, 0, left, right);
	}

}
