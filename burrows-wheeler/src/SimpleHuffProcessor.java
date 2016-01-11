import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class SimpleHuffProcessor implements IHuffProcessor {

	protected HuffViewer myViewer;
	protected int[] myLetterCount;
	protected Map<Integer, String> myMapping;
	protected TreeNode myRoot;
	protected int myCompBits, myUncompBits, mySavedBits;

	public int preprocessCompress(InputStream in) throws IOException {

		myLetterCount = countLetters(in);
		// printCounts(myLetterCount);

		myRoot = HuffTree(myLetterCount);
		// myViewer.update("\n\nHUFF TREE LOOKS LIKE: \n\n"
		// + treeToString(myRoot, ""));

		myMapping = new TreeMap<Integer, String>();
		myCompBits = BITS_PER_INT * (ALPH_SIZE + 1); // the number of bits for
														// magic number and
														// character counts
		createMapping(myRoot, myMapping, "");
		// myViewer.update("\n\nTHE CODE TABLE LOOKS LIKE: \n\n");
		// printMapping(myMapping);

		// add extra bits to fulfill the nearest multiples of 8.
		if (myCompBits % 8 != 0)
			myCompBits = (myCompBits / 8 + 1) * 8;

		mySavedBits = (myRoot.myWeight - 1) * BITS_PER_WORD - myCompBits;
		// myViewer.update("\n\noriginal file size in bits: "
		// + (myRoot.myWeight - 1) * BITS_PER_WORD);
		return mySavedBits;
	}
	

	public int[] countLetters(InputStream raw) throws IOException {
		int[] result = new int[ALPH_SIZE];
		BitInputStream in = new BitInputStream(raw);
		int currentWord = in.readBits(BITS_PER_WORD);
		while (currentWord != -1) {
			result[currentWord]++;
			currentWord = in.readBits(BITS_PER_WORD);
		}
		return result;
	}
	

	private int writeHeader(OutputStream out, TreeNode root) {

		BitOutputStream bitsOut = new BitOutputStream(out);
		int bitCount = 0;

		// write regular magic number
		bitsOut.writeBits(BITS_PER_INT, MAGIC_NUMBER);
		bitCount += BITS_PER_INT;

		// write header, non-saving-space code using SCF
		for (int k = 0; k < ALPH_SIZE; k++) {
			bitsOut.writeBits(BITS_PER_INT, myLetterCount[k]);
			bitCount += BITS_PER_INT;
		}

		return bitCount;
	}
	

	protected TreeNode HuffTree(int[] count) {
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
		return q.poll();
	}
	

	protected void createMapping(TreeNode root, Map<Integer, String> map,
			String path) {
		if (root == null)
			return;
		if ((root.myLeft == null) && (root.myRight == null)) {
			map.put(root.myValue, path);
			myCompBits += path.length() * root.myWeight;
			return;
		}
		createMapping(root.myLeft, map, path + "0");
		createMapping(root.myRight, map, path + "1");
	}
	

	public void setViewer(HuffViewer viewer) {
		myViewer = viewer;
	}
	

	private int writeCompressed(BitInputStream bitsin, BitOutputStream bitsOut)
			throws IOException {
		int bitCount = 0;

		// write compressed file
		int curWord = bitsin.readBits(BITS_PER_WORD);
		while (curWord != -1) {
			String code = myMapping.get(curWord);
			for (char c : code.toCharArray()) {
				int bin = Integer.parseInt("" + c);
				bitsOut.writeBits(1, bin);
			}
			bitCount += code.length();
			curWord = bitsin.readBits(BITS_PER_WORD);
		}
		
		// Write pseudo EOF
		String code = myMapping.get(PSEUDO_EOF);
		for (char c : code.toCharArray()) {
			int bin = Integer.parseInt("" + c);
			bitsOut.writeBits(1, bin);
		}
		bitCount += code.length();
		return bitCount;
	}
	

	public int compress(InputStream in, OutputStream out, boolean force)
			throws IOException {
		if ((mySavedBits <= 0) && (force == false)) {
			myViewer.showError("compression uses " + (-mySavedBits)
					+ " more bits\nuse force compression to compress");
			return 0;
		} else {
			BitInputStream bis = new BitInputStream(in);
			BitOutputStream bos = new BitOutputStream(out);
			myCompBits = 0;
			myCompBits += writeHeader(bos, myRoot);
			myCompBits += writeCompressed(bis, bos);
			
			bos.flush();
			if (myCompBits % 8 != 0)
				myCompBits = (myCompBits / 8 + 1) * 8;
			return myCompBits;
		}
	}
	

	private int untransform(BitInputStream bis, OutputStream out)
			throws IOException {
		BurrowsWheeler bw = new BurrowsWheeler();
		int chunkCount = 1;
		while (true) {
			int first = bis.readBits(BITS_PER_INT);
			if (first == -1) {
				myViewer.showError("problem getting first index");
				break;
			}

			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			BitOutputStream temp = new BitOutputStream(byteOut);
			myUncompBits += doOriginalHuffUncompressAfterMagic(bis, temp);
			byte[] array = byteOut.toByteArray();
			char[] chunks = new char[array.length];
			for (int k = 0; k < chunks.length; k++) {
				byte b = array[k];
				char bp = (char) (b & 0xff);
				chunks[k] = bp;
			}

			chunks = bw.unmtf(chunks);
			chunks = bw.decode(chunks, first);
			for (char c : chunks)
				out.write(c);

			int header = bis.readBits(BITS_PER_INT);
			if (header == WHEELER_MAGIC) {
				chunkCount++;
			} else {
				break;
			}
		}
		out.flush();
		return chunkCount;
	}
	

	private int doOriginalHuffUncompressAfterMagic(BitInputStream bitsIn,
			BitOutputStream bitsOut) throws IOException {

		myLetterCount = new int[ALPH_SIZE];
		for (int k = 0; k < ALPH_SIZE; k++) {
			int bits = bitsIn.readBits(BITS_PER_INT);
			myLetterCount[k] = bits;
		}

		myRoot = HuffTree(myLetterCount);
		myMapping = new TreeMap<Integer, String>();
		recreateMapping(myRoot, myMapping, "");
		TreeNode tnode = myRoot;
		int unCompBitCount = 0;
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
					unCompBitCount += BITS_PER_WORD;
					tnode = myRoot;
					// start back at top
				}
			}
		}
		return unCompBitCount;
	}
	
	
	protected void recreateMapping(TreeNode root, Map<Integer, String> map,
			String path) {
		if (root == null)
			return;
		if ((root.myLeft == null) && (root.myRight == null)) {
			map.put(root.myValue, path);
			return;
		}
		recreateMapping(root.myLeft, map, path + "0");
		recreateMapping(root.myRight, map, path + "1");
	}

	
	public int uncompress(InputStream in, OutputStream out) throws IOException {
		BitInputStream bitsIn = new BitInputStream(in);
		BitOutputStream bitsOut = new BitOutputStream(out);
		int myUncompBits = 0;
		int magic = bitsIn.readBits(BITS_PER_INT);
		if (magic == WHEELER_MAGIC) {
			untransform(bitsIn, out);
			return myUncompBits;
		}

		else if (magic == MAGIC_NUMBER) {
			return doOriginalHuffUncompressAfterMagic(bitsIn, bitsOut);
		}

		else
			throw new IOException("magic number not right");		
	}
	

	public int transform(InputStream in, OutputStream out) throws IOException {
		BitInputStream bis = new BitInputStream(in);
		BitOutputStream bos = new BitOutputStream(out);
		myCompBits = 0;
		BurrowsWheeler bw = new BurrowsWheeler();
		while (true) {
			char[] chunk = bw.transform(bis);
			if (chunk.length == 0)
				break;
			chunk = bw.mtf(chunk);
			byte[] array = new byte[chunk.length];
			for (int k = 0; k < array.length; k++) {
				array[k] = (byte) chunk[k];
			}
			ByteArrayInputStream bas = new ByteArrayInputStream(array);
			preprocessCompress(bas);
			int first = bw.getFirst();

			BitInputStream comp = new BitInputStream(new ByteArrayInputStream(
					array)); // construct a BitInputStream from a
								// ByteArrayInputStream
			myCompBits += writeBWTHeader(bos, myRoot, first);
			myCompBits += writeCompressed(comp, bos);
			
			// add extra bits to fulfill the nearest multiples of 8.
			if (myCompBits % 8 != 0)
				myCompBits = (myCompBits / 8 + 1) * 8;
		}
		bos.flush();

		return myCompBits;
	}
	

	private int writeBWTHeader(BitOutputStream bitsOut, TreeNode root, int first) {

		int bitCount = 0;

		// write Burrows-Wheeler magic number
		bitsOut.writeBits(BITS_PER_INT, WHEELER_MAGIC);
		bitCount += BITS_PER_INT;

		// write first index
		bitsOut.writeBits(BITS_PER_INT, first);
		bitCount += BITS_PER_INT;

		// write header, non-saving-space code using SCF
		for (int k = 0; k < ALPH_SIZE; k++) {
			bitsOut.writeBits(BITS_PER_INT, myLetterCount[k]);
			bitCount += BITS_PER_INT;
		}

		return bitCount;
	}
	

	// protected void showString(String s) {
	// myViewer.update(s);
	// }

	// public String treeToString(TreeNode root, String prefix) {
	// if (root == null)
	// return "";
	// prefix += "     ";
	// String leftSubtree = treeToString(root.myLeft, prefix);
	// String rightSubtree = treeToString(root.myRight, prefix);
	// if ((leftSubtree.length() != 0) && (rightSubtree.length() != 0))
	// return root.myValue + "\n" + prefix + leftSubtree + "\n" + prefix
	// + rightSubtree;
	// if (leftSubtree.length() != 0)
	// return root.myValue + "\n" + prefix + leftSubtree;
	// if (rightSubtree.length() != 0)
	// return root.myValue + "\n" + prefix + rightSubtree;
	// return "" + root.myValue;
	// }

	// protected void printCounts(int[] array){
	// for (int i = 0; i < array.length; i++) {
	// if (array[i] > 0)
	// myViewer.update("Letter " + i + "(" + (char) i + "): "
	// + array[i]);
	// }
	// }

	// protected void printMapping(Map<Integer, String> Mapping) {
	//
	// for (Integer character : Mapping.keySet()) {
	// if (character < 256) {
	// myViewer.update("Letter " + character + "("
	// + (char) character.intValue() + "): "
	// + Mapping.get(character));
	// } else {
	// myViewer.update("Letter " + character + "(Pseudo-EOF): "
	// + Mapping.get(character));
	// }
	// }

	// }
}
