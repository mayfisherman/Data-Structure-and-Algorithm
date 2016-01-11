import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class HangmanMyLoader {
	
	ArrayList<String> Categories=new ArrayList <String>();
	Random number = new Random();

    public boolean readFile(String filename) {
        try {
            FileReader dataFile = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(dataFile);
            String currentLine = bufferedReader.readLine();

            while(currentLine != null) {
                String trimmedWord = currentLine.trim();
                Categories.add(trimmedWord);
                currentLine = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            System.err.println("A error occured reading file: " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }
   
   
    public String getRandomWord(){
       
        int randomindex=number.nextInt(Categories.size());
        String RandomWord= Categories.get(randomindex);
        return RandomWord;
    }
}
