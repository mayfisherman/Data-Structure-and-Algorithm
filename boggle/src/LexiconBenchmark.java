import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ProgressMonitorInputStream;

public class LexiconBenchmark {

	private static String SMALL_LEXICON = "/bogwords.txt";
	private static String LARGE_LEXICON = "/ospd3.txt";
	private static int PERCENTAGE = 3;
	private static Random ourRandom = new Random(1234567);
	private static ArrayList<String> myList;

	public static String iterate(ILexicon lex) {
		// public static String iterate(TrieLexicon lex){

		double start = System.currentTimeMillis();
		int count = 0;
		for (String s : lex) {
			count++;
		}
		double end = System.currentTimeMillis();
		assert lex.size() == count : "sizes differ";
		return String.format("iter time: %f\tsize: %d", (end - start) / 1000.0,
				count);
	}

	public static String wordTest(ILexicon lex) {
		// public static String wordTest(TrieLexicon lex){
		double start = System.currentTimeMillis();
		int count = 0;
		for (String s : myList) {
			assert LexStatus.WORD == lex.wordStatus(s) : "isword failed for "
					+ s;
			count++;
		}
		double end = System.currentTimeMillis();
		assert lex.size() == count : "sizes differ after word test";
		return String.format("word time: %f\twords: %d",
				(end - start) / 1000.0, count);
	}

	public static String prefix(ILexicon lex) {
		// public static String prefix(TrieLexicon lex){
		ArrayList<String> pres = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>(myList);

		ourRandom.setSeed(123456);
		for (String s : myList) {
			if (ourRandom.nextInt(PERCENTAGE) == 0) {
				String sub = s.substring(0, s.length() - 1);
				if (!set.contains(sub)) {
					pres.add(sub);
				}
			}
		}
		int preCount = pres.size();
		double start = System.currentTimeMillis();
		for (String s : pres) {
			LexStatus stat = lex.wordStatus(s);
			assert LexStatus.PREFIX == stat : "prefix fail for " + s;
		}
		double end = System.currentTimeMillis();
		return String.format("pref time: %f\tsize: %d", (end - start) / 1000.0,
				preCount);
	}

	public static void doStress(ILexicon lex) {
		// public static void doStress(TrieLexicon lex){
		String name = lex.getClass().getName();
		System.out.println("size of " + name + ": " + lex.size());
		String result = iterate(lex);
		System.out.println("\t" + result);
		result = wordTest(lex);
		System.out.println("\t" + result);
		result = prefix(lex);
		System.out.println("\t" + result + "\n");

		// count the nodes and oneWay nodes when implementing TrieLexicon and
		// CompressedTrieLexicon
		// System.out.printf("\tnode count: %d\toneway count: %d\n",
		// lex.nodeCount(),lex.oneWayCount());

	}

	public static void stress(ILexicon lex) {
		// public static void stress(TrieLexicon lex){
		lex.load(myList);
		doStress(lex);
	}

	public static void main(String[] args) {

		InputStream is = args.getClass().getResourceAsStream(SMALL_LEXICON);
		ProgressMonitorInputStream pmis = StoppableReader.getMonitorableStream(
				is, "reading...");
		Scanner s = new Scanner(pmis);
		myList = new ArrayList<String>();
		while (s.hasNext()) {
			myList.add(s.next());
		}
		// stress(new SimpleLexicon());
		// stress(new BinarySearchLexicon());
		// stress(new ArrayLexicon());
		stress(new TrieLexicon());
		stress(new CompressedTrieLexicon());
		// stress(new SmallTrieLexicon());
	}

}
