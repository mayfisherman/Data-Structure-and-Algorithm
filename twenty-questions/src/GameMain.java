


/**
 * @author Owen Astrachan
 * @date March 15, 2010
 */
public class GameMain {
    public static void main(String[] args){
	    AnimalGameViewer sv = new AnimalGameViewer("Compsci 100, Twenty-Questions");
	    IAnimalModel gm = new AnimalGameModel();
	    sv.setModel(gm);
    }
}
