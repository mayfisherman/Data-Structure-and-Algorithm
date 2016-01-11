/*
 * Created on Mar 18, 2005
 *
 */

/**
 * Encapsulate a (row,column) entry so that the entry
 * can be hashed, compared, and stored as a single
 * entry. This implementation provides simple methods
 * for determining when one BoardCell is a neighbor of
 * another. Here neighbor means adjacent horizontally,
 * vertically, or diagonally.
 * <P>
 * @author Owen Astrachan
 *
 */
public class BoardCell implements Comparable<BoardCell> {

    public int row;
    public int col;
    private int hash;  
    
    public BoardCell(int r, int c){
        row = r;
        col = c;
        hash = new Integer(row*1000+col).hashCode();
    }
    
    /**
     * Returns true iff c is a neighbor of this board
     * cell; neighbor means adjacent horizontally,
     * vertically, or diagonally (not self).
     * @param c is BoardCell compared to this one
     * for adjacency
     * @return true iff c adjacent to this BoardCell
     */
    public boolean isNeighbor(BoardCell c){
        if (c == null) return true;
        if (row == c.row) return Math.abs(col - c.col) == 1;
        if (col == c.col) return Math.abs(row - c.row) == 1;
        return Math.abs(col - c.col) + Math.abs(row - c.row) == 2;
    }
    public boolean equals(Object o){
        BoardCell c = (BoardCell) o;
        return c.row == row && c.col == col;
    }
    public int hashCode(){
        return hash;
    }
    
    /**
     * Returns value indicating whether cell is less
     * than, equal to, or greater than this cell as determined
     * first by row value, then by column value for 
     * cells whose rows are equal
     * @param cell compared to this BoardCell
     * @return row difference if rows aren't the same,
     * column difference if rows are the same
     */
    public int compareTo(BoardCell cell){
        int rd = row - cell.row;
        if (rd != 0) return rd;
        return col - cell.col;
    }
}
