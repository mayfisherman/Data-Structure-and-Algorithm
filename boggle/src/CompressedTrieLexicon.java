import java.util.*;

public class CompressedTrieLexicon extends TrieLexicon {

	public static class Node {
		String info;
		boolean isWord;

		Map<String, Node> children;
		Node parent;

		Node(char ch, Node p) {
			info = "" + ch;
			isWord = false;
			children = new TreeMap<String, Node>();
			parent = p;

		}

		Node(String str, Node p) {
			info = str;
			isWord = true;
			children = new TreeMap<String, Node>();
			parent = p;
		}
	}

	protected Node myRoot;

	public CompressedTrieLexicon() {
		myRoot = new Node('x', null);
		mySize = 0;
	}

	public boolean add(String s) {
		Node t = myRoot;

		for (int k = 0; k < s.length(); k++) {

			char ch = s.charAt(k);
			Node child = t.children.get(ch + "");
			if (child == null) {
				child = new Node(ch, t);
				t.children.put(ch + "", child);
			}
			t = child;
		}

		if (!t.isWord) {
			t.isWord = true; // walked down path, mark this as a word
			mySize++;
			return true;
		}
		return false; // already in set
	}

	public Iterator<String> iterator() {
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder str = new StringBuilder();
		for (Node n : myRoot.children.values()) {
			str.append(n.info);
			fillUp(n, list, str);
			str.delete(str.length() - n.info.length(), str.length());
		}
		return list.iterator();
	}

	protected void fillUp(Node root, ArrayList<String> list, StringBuilder str) {
		if (root.isWord) {
			list.add(str.toString());
		}
		for (Node n : root.children.values()) {
			str.append(n.info);
			fillUp(n, list, str);
			str.delete(str.length() - n.info.length(), str.length());
		}
	}

	@Override
	public void load(ArrayList<String> list) {
		super.load(list);
		compress();
	}

	private void compress() {
		ArrayList<Node> leafList = new ArrayList<Node>();
		lookForSuffix(myRoot, leafList);
		for (int i = 0; i < leafList.size(); i++) {
			StringBuilder buildingSuffix = new StringBuilder();
			generateSuffixNode(leafList.get(i), buildingSuffix);
		}
	}

	private void lookForSuffix(Node root, ArrayList<Node> list) {
		if (root.children.keySet().isEmpty()) {
			list.add(root);
			return;
		}
		for (Node n : root.children.values())
			lookForSuffix(n, list);
	}

	private void generateSuffixNode(Node root, StringBuilder str) {
		str = new StringBuilder(root.info + str.toString());
		if ((root.parent.isWord) || (root.parent.children.keySet().size() > 1)) {
			root.parent.children.remove(root.info);
			Node suffix = new Node(str.toString(), root.parent);
			root.parent.children.put(str.toString(), suffix);
			return;
		}
		generateSuffixNode(root.parent, str);
	}

	public LexStatus wordStatus(StringBuilder s) {
		return wordStatus(s.toString());//
	}

	public LexStatus wordStatus(String s) {
		Node t = myRoot;
		for (int k = 0; k < s.length(); k++) {
			char ch = s.charAt(k);
			if (t.children.get(ch + "") == null) {
				if (t.children.get(s.substring(k)) != null)
					return LexStatus.WORD;
				for (String preStr : t.children.keySet()) {
					if (preStr.startsWith(s.substring(k)))
						return LexStatus.PREFIX;
				}
				return LexStatus.NOT_WORD;
			}
			t = t.children.get(ch + "");
		}
		return t.isWord ? LexStatus.WORD : LexStatus.PREFIX;
	}

	public int oneWayCount() {
		return oneWay(myRoot);
	}

	protected int oneWay(Node root) {
		int count = 0;
		if (root == null)
			return 0;
		if (root.children.keySet().size() == 1)
			count = 1;
		for (Node n : root.children.values()) {
			count += oneWay(n);
		}
		return count;
	}

	public int nodeCount() {
		return doCount(myRoot);
	}

	protected int doCount(Node root) {
		int count = 1; // count this node
		if (root == null)
			return 0;
		for (Node n : root.children.values()) {
			count += doCount(n);
		}
		return count;
	}

}
