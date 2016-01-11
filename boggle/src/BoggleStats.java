import java.io.InputStream;
import java.util.*;

import javax.swing.ProgressMonitorInputStream;

public class BoggleStats {

	ArrayList<Integer> myBoardScores, myLexiconScores;

	private static final int MIN_WORD = 3;
	private static final int NUM_TRIALS = 10000;

	public BoggleStats() {
		myBoardScores = new ArrayList<Integer>();
		myLexiconScores = new ArrayList<Integer>();
	}

	public String wordTester(IAutoPlayer player, ILexicon lex,
			ArrayList<Integer> log, int count) {
		BoggleBoardFactory.setRandom(new Random(12345));
		log.clear();
		BoggleBoard maxBoard = new BoggleBoard();
		int maxScore = 0;
		double start = System.currentTimeMillis();

		for (int k = 0; k < count; k++) {
			BoggleBoard board = BoggleBoardFactory.getBoard(4);
			player.findAllValidWords(board, lex, MIN_WORD);
			int score = player.getScore();
			log.add(player.getScore());
			if (k == 0) {
				maxBoard = board;
				maxScore = score;
			} else if (score > maxScore) {
				maxBoard = board;
				maxScore = score;
			}

		}

		double end = System.currentTimeMillis();
		int max = Collections.max(log);
		return String.format(
				"%s %s\t count: %d\tmax: %d\ttime: %f\tmaxBoard: \n%s", player
						.getClass().getName(), lex.getClass().getName(), count,
				max, (end - start) / 1000.0, maxBoard.toString());

	}

	public void doTests(ILexicon lex) {
		IAutoPlayer ap1 = new LexiconFirstAutoPlayer();
		String result = wordTester(ap1, lex, myBoardScores, NUM_TRIALS);
		System.out.println(result);
		IAutoPlayer ap2 = new BoardFirstAutoPlayer();
		String result2 = wordTester(ap2, lex, myLexiconScores, NUM_TRIALS);
		System.out.println(result2);
		for (int k = 0; k < NUM_TRIALS; k++) {
			if (!myBoardScores.get(k).equals(myLexiconScores.get(k))) {
				System.out.println(k + "\t" + myBoardScores.get(k) + "\t"
						+ myLexiconScores.get(k));
			}
		}
	}

	public void runTests(ILexicon lex, ArrayList<String> list) {
		lex.load(list);
		doTests(lex);

	}

	public static void main(String[] args) {

		ILexicon lex = new SimpleLexicon();

		InputStream is = lex.getClass().getResourceAsStream("/ospd3.txt");
		ProgressMonitorInputStream pmis = StoppableReader.getMonitorableStream(
				is, "reading...");
		Scanner s = new Scanner(pmis);
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNext()) {
			list.add(s.next().toLowerCase());
		}

		BoggleStats bs = new BoggleStats();
		bs.runTests(lex, list);
		lex = new BinarySearchLexicon();
		bs.runTests(lex, list);
		lex = new TrieLexicon();
		bs.runTests(lex, list);
		lex = new CompressedTrieLexicon();
		bs.runTests(lex, list);
	}
}
