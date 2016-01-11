


/**
 * @author Owen Astrachan
 * @version Sep 10, 2004
 */
import java.util.Scanner;
public class Jotto {
    public static void main(String[] args){
        JottoModel model = new JottoModel();
        JottoViewer viewer = new JottoViewer("Duke Jotto", model);
    }
}
