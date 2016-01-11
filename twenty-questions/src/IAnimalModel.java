import java.util.Scanner;
import java.io.FileWriter;

public interface IAnimalModel {
    /**
     * Initialize a new game/model from the information in the scanner.
     * @param s is data in the proper format for an animal/20 questions game
     */
    public void initialize(Scanner s);
    
    /**
     * Write information in model to file specified in writer so that
     * it can be read back in to play a game. Clients should close the
     * writer when all information is written.
     * @param writer is open/set for writing
     */
    public void write(FileWriter writer);
    
    /**
     * Called by view to process a yes/no response from the user. The response should
     * result in an update in the model that is conveyed to the view
     * by calling appropriate methods in the view.
     * @param yes is true if user entered 'yes', false if user entered 'no'
     */
    public void processYesNo(boolean yes);
    
    /**
     * Called by view after has entered the no/correct "animal" being
     * thought of that the computer failed to guess. This no-animal will be
     * a new leaf in the tree, and a question differentiating it from
     * what the computer guessed will be subsequently entered by the
     * user. Model should store some state and call
     * <code>getDifferentiator</code> in the view which will, in turn,
     * call <code>addNewQuestion</code> in this model to finalize the
     * process of adding new information to the tree.
     * @param noResponse is user's "correct" animal
     */
    public void addNewQuestion(String noResponse);
    
    /**
     * Called by view to add new node to tree, question is from user and differentiates between
     * yes response that model guessed that was wrong and noResponse view/user
     * sent to model in <code>addNewQuestion</code> prior to view calling this method.
     * @param question is user's question that will be internal node of game
     */
    public void addNewKnowledge(String question);
    
    /**
     * Set state in model so new game can begin, a
     * call to the view should display the first question.
     */
    public void newGame();
    
    /**
     * Set the model's view.
     * @param view is view for this model
     */
    public void setView(AnimalGameViewer view);
}
