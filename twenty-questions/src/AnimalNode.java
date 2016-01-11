
/**
 * This is a POJO (Plain Old Java Object), used to implement a binary tree
 * in implementing a game of 20-questions.
 * <P>
 * @author ola
 *
 */
public class AnimalNode {
   
    public AnimalNode yesLink;
    public AnimalNode noLink;
    public String info;
    public AnimalNode(String value, AnimalNode lptr, AnimalNode rptr){
        info = value;
        yesLink = lptr;
        noLink = rptr;
    }
    public String toString(){
    	return info;
    }
    
}
