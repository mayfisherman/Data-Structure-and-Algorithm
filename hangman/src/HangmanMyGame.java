import java.io.*;
import java.util.*;

public class HangmanMyGame {
	
    Scanner myInput;
	
	public HangmanMyGame(){
		myInput = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
	}
	
	
	/**
	 * Get a line from the user and return the line as a String
	 * @param prompt is printed as an instruction to the user
	 * @return entire line entered by the user
	 */
	public String readString(String prompt) {
		System.out.printf("%s ",prompt);
		String entered = myInput.nextLine();
		return entered;
	}
	
	/**
	 * Play one game of hangman. This should prompt
	 * user for parameters and then play a complete game
	 * You'll likely want to call other functions from this one.
	 */
	
	
	private void printDisplay(char[] Display){
		for(char letters:Display)
			 System.out.print(letters+" ");
	 System.out.println();
	}
	
	
	private void printGuessResult(int miss, String letterlist){
		System.out.println("misses left:  " + miss);
		System.out.print("guesses so far: ");
		printDisplay(letterlist.toCharArray());
	}
	
	
	private boolean updateDisplay(char NewLetter, String TrueWord, char[] oldDisplay){
		boolean contain=false;
		for(int k=0; k < TrueWord.length(); k += 1){
			   
		      if(NewLetter == TrueWord.toCharArray()[k]){
			      oldDisplay[k] = NewLetter;
			      contain=true;
			 }
	      }
	    return contain; 
	      
	}
	
	
	private boolean gameWon(char[] guessedLetters, String actualWord){
		 String guessedpart=new String(guessedLetters);
	     if(guessedpart.equals(actualWord)){
			    	 return true;
	     }
	     return false;
	}
	
	
	
	public void play() {
		
		 String userChoice = readString("which category? A)GRE words B)Country names   ");
		 HangmanMyLoader data = new HangmanMyLoader();
		 
		 switch (userChoice.charAt(0)) {
		 case 'A': data.readFile("GRE words.txt");
		 break;
		 case 'B': data.readFile("Country names.txt");
		 break;
		 default: System.out.println("Invalid input!");
		 break;
		 }
	    
		 String inputguess = readString("# guesses to hanging:  ");
		 int guessCount = Integer.parseInt(inputguess);
		 int misses=guessCount;
		 
		 String secretWord = data.getRandomWord();
		 //System.out.printf("%d letter secret word is %s \n", wordlength,secretWord);
		 
		 char[] myDisplay = new char[secretWord.length()];
		 for (int i=0;i<myDisplay.length;i++){
			 myDisplay[i]='_';
		 }
		 		 
		 String wrongletters="";		 	 		 
		 ArrayList<Character> letterList=new ArrayList<Character>();
		 
		 printDisplay(myDisplay);
		 while (guessCount>wrongletters.length()){
		     
		     printGuessResult(misses,wrongletters);
		     String inputString = readString("guess letter:  ");
		     char guessletter = inputString.trim().charAt(0);
		     
		     
		     if (letterList.contains(Character.valueOf(guessletter))){
			     System.out.printf("You already guessed %c\n",guessletter);
			     printDisplay(myDisplay);
			     continue;
		       }
		     letterList.add(guessletter);   
		   	   		      
		     if (!updateDisplay(guessletter,secretWord,myDisplay)){
				  wrongletters += Character.toString(guessletter);
				  misses--;
				  System.out.printf("no %c\n",guessletter);
			 }
		     
		     printDisplay(myDisplay);
		     
		     if(gameWon(myDisplay,secretWord)){
		    	 System.out.println("You guessed my word!");
		    	 break;
		     }		      		     
		    }
		  
		 
		 if(!gameWon(myDisplay,secretWord))
		 System.out.printf("you are hung :-(, secret word was %s\n",secretWord);
		 
	
	}
}
