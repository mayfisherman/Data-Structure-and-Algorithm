import java.util.*;

/**
 * This is the base class for a Jotto model. The idea is
 * that you (the student) will implement the stub methods
 * that are currently marked TODO to create a program
 * that plays Jotto. You'll likely need to add state or 
 * instance variables, and perhaps helper methods as well.
 * <P>
 * <OL>
 * <LI>
 * The method <code>newGame</code> clears/initializes state 
 * so that the computer can begin to guess the user's word.
 * </LI>
 * <P>
 * <LI>
 * The method <code>processResponse</code> is called by the
 * view-controller when the model is guessing the human's
 * secret word and the human responds with the number of letters
 * in common with the last word guessed by the computer. The model
 * then generates a new guess or decides the game is over, e.g.,
 * when the computer has guessed correctly (number sent is 6) or
 * there are no more words to guess.
 * </LI>
 * <P>
 * <LI>
 * You may want to implement some private, helper methods
 * that are called in the code below to avoid 
 * duplicate code, that will depend on what you write.
 * </LI>
 * </OL>
 * @author ola
 * @version 1.2, 2011, revised from Abstract
 * class last used in 2006, now concrete with
 * stubs students implement
 */

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class JottoModel {
    private JottoViewer myView;             // view and controller
    private ArrayList<String> myWordList;   // dictionary of legal words
    private ArrayList<String> candidateList;   //dictionary of possible words
    private ArrayList<String> guessedWordList;   //list of words already guessed
    private ArrayList<Integer> guessedWordComNum;   //list of corresponding numbers of common letters for guessed words
    private HashSet<Character> wrongLetterList, rightLetterList;  //set of known wrong letters or right letters
    private boolean playSmart = false;         //smart/AI mode or not
    String guessWord;    //the word just guessed
    int guesses;     //the number of guesses left
  
    
    
    /**
     * Initialize the model appropriately.
     */
    public JottoModel() {
        myWordList = new ArrayList<String>();
    }

    /**
     * Associate a view with this model.
     * @param view is view that's notified when model
     * changes
     */
    public void addView(JottoViewer view) {
        myView = view;
    }

    
    /**
     * Display a dialog box the user must dismiss as part
     * of the view, e.g., when the game is over.
     * @param s is the string displayed in the modal dialog
     */
    
    private void showModalMessage(String s){
        myView.showModalInfo(s);
    }

    /**
     * Display a small message in the view's message area.
     * @param s is message displayed
     */
    private void messageViews(String s) {
        myView.showMessage(s);
    }
    
    /**
     * Communicate the guess to the view.
     * @param s is the guess
     */
    private void doGuess(String s){
        myView.processGuess(s);
    }

    /**
     * Read words and store them for guessing and validation
     * when user guessing secret word.
     * @param s is scanner that is source of words
     */
    public void initialize(Scanner s) {
        myWordList.clear();
        while (s.hasNext()) {
            myWordList.add(s.next());
        }
        messageViews("choose new game menu");
    }

    /**
     * Process input from the user. The input is the
     * number of letters in common with the user's secret
     * word. This method does
     * rudimentary analysis of response for legality
     * e.g., number in range, and then calls 
     * unimplemented methods that students must write.
     * @param o is the response from the user. This is a String
     * representing an int that's the number of letters in
     * common with last word guessed by computer. 
     */
    public void process(Object o) {
        String response = (String) o;
        if (response.length() == 0) {
            myView.badUserResponse("not a number");            
            return;
        }
        try {
            int n = Integer.parseInt(response);
            if (n < 0 || n > 6) {
                myView.badUserResponse("out of range " + n);
                showModalMessage(response + " is out of range. Please enter a number from 0 to 6.");
                return;
            }
            processResponse(n);
        } catch (NumberFormatException e) {
            myView.badUserResponse("not a number: " + response);
            showModalMessage(response + " is not a number. Please enter a number from 0 to 6.");
        }
    }

    /**
     * Make the view not respond to user input except
     * by choosing new game or quit (call view method
     * that disables user input).
     */
    public void stopGame() {
        myView.setStopped();
    }
 


    /**
     * @param ComNum
     */
	private void updateLetterList(int ComNum) {
		if (ComNum == 0) {
			for (char wrongLetter : guessWord.toCharArray()) {
				wrongLetterList.add(new Character(wrongLetter));
			}

		}
        
		/**loop through the list of guessed words 
		 * to see if more right or wrong words can
		 * be found based on the updated information
		 */

		for (int i = 0; i < guessedWordList.size(); i++) {

			int RightLetNum = containNumofLets(guessWord, rightLetterList)[0];
			int WrongLetNum = containNumofLets(guessWord, wrongLetterList)[0];

			/**
			 * When the number of right letters in
			 * the guessed word is greater than (meaning 
			 * there're duplicate right letters) or
			 * equal to the number of common letters
			 * the guessed word has, the rest of the
			 * letters in the guessed word are all 
			 * wrong letters.
			 */
			if ((RightLetNum >= guessedWordComNum.get(i)) && (RightLetNum != 0)) {
				for (char temp2 : guessedWordList.get(i).toCharArray()) {
					if (!rightLetterList.contains(new Character(temp2))) {
						wrongLetterList.add(new Character(temp2));

					}
				}
			}
			
			/**
			 * When the number of wrong letters in
			 * the guessed word plus the number of 
			 * common letters the guessed word equals
			 * to the word length 5, the rest of the
			 * letters in the guessed word are all 
			 * right letters.
			 */
			if ((WrongLetNum + guessedWordComNum.get(i) == 5)
					&& (guessedWordComNum.get(i) != 0)) {
				for (char temp3 : guessedWordList.get(i).toCharArray()) {
					if (!wrongLetterList.contains(new Character(temp3))) {
						rightLetterList.add(new Character(temp3));

					}
				}
			}
		}
	}
    
	
    /**
     * return an array of two integers.
     * the first one is the number of 
     * common letters the word has compared
     * to the LetterSet, the second one
     * is the number of unique
     * common letters in the word.
     *  
     */
    
    private int[] containNumofLets(String word, HashSet<Character> LetterSet){
    	int[] Count = {0,0};
    	HashSet<Character> LetterSetCopy = new HashSet<Character>(); 
    	LetterSetCopy.addAll(LetterSet);
    	for (char letter:word.toCharArray()){
	    	   if (LetterSet.contains(new Character(letter)))
	               Count[0]++;
	    	   if (LetterSetCopy.remove(new Character(letter)))
	    		   Count[1]++;
    }
    	return Count;
    }
    
    
    /**
     * A human has entered the number of letters in common
     * for a computer-generated guess, likely the last guess
     * generated by the model and sent to the view. Process
     * the number of letters in common and then generate a new guess
     * or otherwise notify the view of the state of the game.
     * @param n is the number of letters in common with the
     * last computer-generated guess
     */
        
	public void processResponse(int n) {

		if (n == 6) {
			showModalMessage("Great! I got your word");
			stopGame();
			
		}

	    /**
	     *  Stop game if 15 guesses are used up
	     */ 
		else if (guesses == 0) {
			showModalMessage("Game over: I've run out of guesses!");
			stopGame();
			return;
		}

		else {
			candidateList.remove(guessWord);
			for (int i = 0; i < candidateList.size(); i++) {
				if (commonCount(guessWord, candidateList.get(i)) != n) {
					candidateList.remove(i);
					i--;
				}
			}

			if (playSmart) {
				guessedWordList.add(guessWord);
				guessedWordComNum.add(n);
				updateLetterList(n);
				
				
				for (int s = 0; s < candidateList.size(); s++) {
					int NumofWrongLets = containNumofLets(candidateList.get(s),
							wrongLetterList)[0];
					int uniqueNumofRightLets = containNumofLets(
							candidateList.get(s), rightLetterList)[1];
					
					/**
				     *  get rid of words with wrong letters.
				     */
					if (NumofWrongLets > 0) {
						candidateList.remove(s);
						s--;
						continue;
					}

					/**
				     *  get rid of words not having all the right letters.
				     */
					if (uniqueNumofRightLets < rightLetterList.size()) {
						candidateList.remove(s);
						s--;
					}

				}
			}
			
			/**
		     *  when the list of possible words runs
		     *  out, show the following message
		     */
			if (candidateList.size() == 0) {
				showModalMessage("Game over: your word is not in the dictionary \n or you entered conflicting common counts!");
				stopGame();
				return;
			}

			guesses--;
			messageViews("guesses left: " + guesses +
			"    possible words remaining: " + candidateList.size());
			guessWord = pickWord(candidateList);
			doGuess(guessWord);
		}

	}
    
   
    
   
    
    /**
     * Start a new game -- set up state and generate first guess 
     * made by computer.
     */
    
    public void newGame() {
    	guesses = 14;
    	candidateList = new ArrayList<String>();
    	candidateList.addAll(myWordList);
    	wrongLetterList = new HashSet<Character>();
    	rightLetterList = new HashSet<Character>();
    	guessedWordList = new ArrayList<String>();
    	guessedWordComNum = new ArrayList<Integer>();
    	messageViews("guesses left: " + guesses + "    possible words remaining: " + candidateList.size());
    	guessWord = pickWord(candidateList);
        doGuess(guessWord);
    }
    
    
    
    /**
     * This method is used to pick a random element 
     * from an ArrayList of Strings.
     *
     */    
    
    private String pickWord(ArrayList<String> wordList){
    	Random selectWord = new Random ();
    	int randomIndex = selectWord.nextInt(wordList.size());
    	String randomWord = wordList.get(randomIndex); 
    	return randomWord;
    }
    
    /**
     * Called by View/Menu to play a smarter
     * game than the naive, eliminate words from list
     * version of the game. For Compsci 100 in
     * Fall 2011 this is an extra credit function
     */
    
    public void playSmarter() {
    	playSmart = true;
    	
    }
    
    /**
     * Returns number of letters in common to a and b, ensuring
     * each common letter only counts once in total returned.
     * @param a is one string being compared
     * @param b is other string being compared
     * @return number of letters in common to a and b
     */
    private int commonCount(String a, String b) {
        char[] aletters = a.toCharArray();
        char[] bletters = b.toCharArray();
        int count = 0;
        for (int k = 0; k < aletters.length; k++) {
            for (int j = 0; j < bletters.length; j++) {
                if (aletters[j] == bletters[k]) {
                    aletters[j] = '*';  // don't use this again
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
