import java.util.Random;

/**
 * Factory for generating boggle boards. The current
 * implementation uses <code>StandardBoardMaker</code>
 * which has standard Boggle and Big Boggle cubes for 4x4
 * and 5x5 boards, respectively. The board generator/maker can
 * be changed via <codE>setMaker</code>.
 * <P>
 * The factory is also the source for a game's random-number
 * generator. This facilitates consistent debugging, e.g.,
 * the default random number generator uses a set seed
 * so that the same sequence of games results every time
 * since the source of randomness is specified. This can
 * be altered via <code>setRandom</code>.
 * <P>
 * @author Owen Astrachan
 *
 */
public class BoggleBoardFactory {

    private static Random ourRandom = new Random();
    
    private static IBoardMaker ourMaker = new StandardBoardMaker();
   
    /**
     * Return a random board of specified size using the
     * factory's BoardMaker.
     * @param size specifies one dimension of square board
     * returned
     * @return board of the specified size
     */
    public static BoggleBoard getBoard(int size){
        return ourMaker.makeBoard(size);
    }
    
    /**
     * Change the board maker used by the factor.
     * @param maker is new Boggle board maker
     */
    public static void setMaker(IBoardMaker maker){
        ourMaker = maker;
    }
    
    /**
     * Return the factory's random number generator, this
     * helps ensure consist debugging or consistent play.
     * @return the factory's Random source
     */
    public static Random getRandom(){
        return ourRandom;
    }

    /**
     * Change the source of a game's random number generator.
     * @param r is the new source of random values for a game
     */
    public static void setRandom(Random r){
        ourRandom = r;
        ourMaker = new StandardBoardMaker();
    }
}
