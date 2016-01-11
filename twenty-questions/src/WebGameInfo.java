
/**
 * This class is basically a POJO (Plain Old Java Object), wrapping
 * a name and a URL together to facilitate use in the GUI as users
 * choose URLs to play different games.
 * <P>
 * The instance variables are left at 'package' scope to be accessed by the GUI
 * without getters/setters
 * 
 * @author ola
 * @date revised Oct 2009
 *
 */
public class WebGameInfo {
	String myName;
	String myURL;
	public WebGameInfo(String name, String url){
		myName = name;
		myURL = url;
	}
	public String toString(){
		return myName;
	}
	public String getURL(){
		return myURL;
	}
}
