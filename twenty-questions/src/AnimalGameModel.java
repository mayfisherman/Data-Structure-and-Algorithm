/**
 * @author Yuanjie Jin
 * @date TOMORROW
 */
import java.io.*;
import java.util.*;

public class AnimalGameModel implements IAnimalModel {

	private AnimalGameViewer myView;
	private AnimalNode myRoot, myCurrentNode;
	private int myNodeCount;
	private ArrayList<String> myPath;

	@Override
	public void addNewKnowledge(String question) {		
		myCurrentNode.info = question;
		newGame();
	}

	@Override
	public void addNewQuestion(String noResponse) {
		StringBuffer animal = new StringBuffer(noResponse);		
		AnimalNode leftChild = new AnimalNode(myCurrentNode.info, null, null);
		AnimalNode rightChild = new AnimalNode(animal.toString(), null, null);
		myCurrentNode.yesLink = leftChild;
		myCurrentNode.noLink = rightChild;
		myNodeCount = myNodeCount + 2;
		StringBuffer display = new StringBuffer();
		display.append("To add new knowledge to this game\n");
		display.append("you must create a question that differentiates\n");
		display.append("from my guess and what you were thinking of.\n\n");
		display.append("What has a YES answer for:\n");
		display.append("\t" + leftChild.info + "\n");
		display.append("and a NO answer for:\n");
		display.append("\t" + rightChild.info + "\n");
		myView.update(display.toString());
		myView.getDifferentiator();

	}

	@Override
	public void initialize(Scanner s) {
		myView.setEnabled(true);
		myNodeCount = 0;
		myRoot = readAll(s);
		newGame();
	}

	//this helper method recursively reads the file and store 
	//information in the tree in preorder traversal
	public AnimalNode readAll(Scanner s) {
		AnimalNode root = null;
		if (s.hasNextLine()) {
			String line = s.nextLine();
			myNodeCount++;
			if (line.startsWith("#Q:")) {
				AnimalNode leftNode = readAll(s);
				AnimalNode rightNode = readAll(s);
				root = new AnimalNode(line.substring(3), leftNode, rightNode);
			} else
				root = new AnimalNode(line, null, null);
		}
		return root;
	}

	@Override
	public void newGame() {
		myCurrentNode = myRoot;
		myView.update(myCurrentNode.info);
		myView.showMessage("# nodes in tree read: " + myNodeCount);
		myPath = new ArrayList<String>();
	}

	@Override
	public void processYesNo(boolean yes) {
		String path;
		if (yes) {
			if (myCurrentNode.yesLink == null) {
				myView.showDialog("I win!!!");
				newGame();
			} else {
				path = "You answered YES to " + myCurrentNode.toString();
				myPath.add(path);
				myCurrentNode = myCurrentNode.yesLink;
				myView.update(myCurrentNode.info);
			}

		} else {
			path = "You answered NO to " + myCurrentNode.toString();
			myPath.add(path);
			if (myCurrentNode.noLink == null) {
				StringBuffer question = new StringBuffer();
				question.append("Please phrase as a question, e.g.,\n");
				question.append("Are you a rhinoceros?\n");
				question.append("Are you 'The Grapes of Wrath'\n\n");
				question.append("Your path so far:\n");
				for (String element : myPath)
					question.append(element + "\n");
				myView.update(question.toString());		
				myView.getNewInfoLeaf();
			} else {
				myCurrentNode = myCurrentNode.noLink;
				myView.update(myCurrentNode.info);
			}

		}

	}

	@Override
	public void setView(AnimalGameViewer view) {
		myView = view;
	}

	@Override
	public void write(FileWriter writer) {
		try {
			writeAll(myRoot, writer);
		} catch (IOException e) {
			myView.showError("an unexpected error occured while attempting to write the file!");
			e.printStackTrace();
			return;
		}
	}

	// a recursive helper method to write all questions in the tree to a file
	// in a preorder traversal
	private void writeAll(AnimalNode root, FileWriter writer)
			throws IOException {
		if (root == null)
			return;

		if (root.yesLink != null) {
			writer.write("#Q:" + root.info + "\n");
			writeAll(root.yesLink, writer);
			writeAll(root.noLink, writer);
			return;
		}

		writer.write(root.info + "\n");
	}

}
