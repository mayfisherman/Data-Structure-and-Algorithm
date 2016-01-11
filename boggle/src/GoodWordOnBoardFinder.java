import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Yuanjie
 * 
 */
public class GoodWordOnBoardFinder implements IWordOnBoardFinder {

	public List<BoardCell> cellsForWord(BoggleBoard board, String word) {
		ArrayList<BoardCell> myList = new ArrayList<BoardCell>();
		for (int r = 0; r < board.size(); r++) {
			for (int c = 0; c < board.size(); c++) {
				if (PathExist(board, r, c, myList, word, 0)) {
					return myList;
				}

			}

		}

		return myList;
	}

	private boolean PathExist(BoggleBoard board, int row, int col,
			ArrayList<BoardCell> list, String word, int Index) {

		if (Index == word.length())
			return true;

		if (isOffBoard(board, row, col))
			return false;

		BoardCell myCell = new BoardCell(row, col);
		if (list.contains(myCell))
			return false;

		String letter = word.charAt(Index) + "";
		if (letter.equals("q")) {
			if (Index == word.length() - 1)
				return false;
			if (word.charAt(Index + 1) != 'u')
				return false;
			if (!board.getFace(row, col).equals("qu"))
				return false;
			Index++;
		}

		else if (!board.getFace(row, col).equals(letter))
			return false;

		list.add(myCell);
		int[] rdelta = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] cdelta = { -1, 0, 1, -1, 1, -1, 0, 1 };
		for (int k = 0; k < rdelta.length; k++) {
			if (PathExist(board, row + rdelta[k], col + cdelta[k], list, word,
					Index + 1))
				return true;
		}
		list.remove(myCell);
		return false;

	}

	private boolean isOffBoard(BoggleBoard board, int row, int col) {
		return row < 0 || row >= board.size() || col < 0 || col >= board.size();
	}

}
