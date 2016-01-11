import java.util.*;

/**
 * Represent a Boggle Board to play a game of
 * Boggle. 
 * @author Owen Astrachan
 * @date March, 2008
 */
public class BoggleBoard {
    

    private String[] myFaces;
    private int mySize;

    /**
     * Create a square boggle board from an array
     * of Strings/Cube faces. The array should contain
     * sizexsize entries, e.g., 16 for a 4x4 Boggle game.
     * @param faces is row-major order of cubes used in
     * the board for Boggle
     */
    public BoggleBoard(String[] faces) {
        myFaces = faces;
        mySize = (int) Math.sqrt(faces.length);
    }

    public BoggleBoard(){
    	
    }
    /**
     * Returns dimension of a square board, e.g., 4 or 5
     * for Boggle or Big Boggle, respectively.
     * @return size of board
     */
    public int size() {
        return mySize;
    }

    /**
     * Return the cube face at specified location, this
     * will be a one-character string, presumably, or
     * "Qu" for a Q-cube.
     * @param row is row of cube whose face is returned
     * @param col is column of cube whose face is returned
     * @return String/face of specified cube
     * @throws ArrayIndexOutOfBoundsException if cube location
     * isn't valid
     */
    public String getFace(int row, int col) {
        return myFaces[row * mySize + col];
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int r=0; r < mySize; r++){
            for(int c=0; c < mySize; c++){
                sb.append(String.format("%3s",getFace(r,c)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
