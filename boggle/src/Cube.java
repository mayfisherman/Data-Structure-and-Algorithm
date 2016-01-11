
/**
 * Represents a cube on a Boggle board. The cube
 * can be constructed from a string or from
 * an array of values. Cube faces presumably represent
 * single characters, or "Qu", but can be arbitrary
 * strings since this class exports only methods
 * that return a String for the face/side of a cube.
 * <P>
 * @author Owen Astrachan
 * @version Oct 30, 2004
 */

import java.util.Random;

public class Cube {
    private String[] myFaces;
    private int myCurrentSide;

    /**
     * Construct cube from an array of strings where
     * each entry of the array represents a face of the cube.
     * @param faces is array of values, one per face of cube
     */
    public Cube(String[] faces)    {
      myFaces = faces;
    }
    
    /**
     * Construct cube from string, each face of the
     * cube is one character of the string, but "Q"
     * is replaced by "Qu" in making a cube
     * @param s is string that is the source of
     * values for the cube's faces
     */
    public Cube(String s){
        myFaces = new String[s.length()];
        for(int k=0; k < s.length(); k++){
            myFaces[k] = s.substring(k,k+1);
            if (myFaces[k].equals("q")){
                myFaces[k] += "u";
            }
        }
    }

    /**
     * Return a random face of the cube, effectively
     * "rolling" the cube and returning the value
     * shown.
     * @return a string representing one face of cube,
     * the face is generated at random
     */
    public String getRandomFace()    {
      randomize();
      return myFaces[myCurrentSide];
    }

    private void randomize()    {
      Random randomizer = BoggleBoardFactory.getRandom();
      myCurrentSide = randomizer.nextInt(myFaces.length);
    }
}
