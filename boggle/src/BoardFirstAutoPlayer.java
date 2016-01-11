import java.util.*;

public class BoardFirstAutoPlayer extends AbstractAutoPlayer {

	private List<BoardCell> myList;
	private StringBuilder myWord;

	@Override
	public void findAllValidWords(BoggleBoard board, ILexicon lex, int minLength) {
		clear();
		for (int r = 0; r < board.size(); r++) {
			for (int c = 0; c < board.size(); c++) {
				myList = new ArrayList<BoardCell>();
				myWord = new StringBuilder();
				findWordsAt(board, r, c, lex, minLength, myWord);
			}
		}
	}

	private void findWordsAt(BoggleBoard board, int row, int col, ILexicon lex,
			int minLength, StringBuilder buildingWord) {

		if (isOffBoard(board, row, col))
			return;

		BoardCell myCell = new BoardCell(row, col);
		if (myList.contains(myCell))
			return;

		myList.add(myCell);
		String myLetter = board.getFace(row, col);
		buildingWord.append(myLetter);
		LexStatus myStatus = lex.wordStatus(buildingWord);

		if (myStatus.equals(LexStatus.WORD)
				&& buildingWord.length() >= minLength)
			add(buildingWord.toString());
		if (myStatus.equals(LexStatus.NOT_WORD)) {
			myList.remove(myCell);
			buildingWord.delete(buildingWord.length() - myLetter.length(),
					buildingWord.length());
			return;
		}

		int[] rdelta = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] cdelta = { -1, 0, 1, -1, 1, -1, 0, 1 };
		for (int k = 0; k < rdelta.length; k++) {
			findWordsAt(board, row + rdelta[k], col + cdelta[k], lex,
					minLength, buildingWord);
		}

		myList.remove(myCell);
		buildingWord.delete(buildingWord.length() - myLetter.length(),
				buildingWord.length());
	}

	private boolean isOffBoard(BoggleBoard board, int row, int col) {
		return row < 0 || row >= board.size() || col < 0 || col >= board.size();
	}

}
