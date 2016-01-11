import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;

/**
 * SimpleViewer augmented with menus and controls Jotto game. Students
 * don't need to modify this (hopefully) to play Jotto, but the view
 * has been designed to make modifications possible, though documentation
 * is sketchy for how the view uses the JottoPanel nested class.
 * @author Owen Astrachan
 * @version Sep 12, 2004
 * @version Feb 20, 2006
 * @version Sept 2011, only lets computer guess, not
 * computer guess or human/user guess as in the past
 */
public class JottoViewer extends JFrame {

    protected JList myOutput;
    protected JottoModel myModel;
    protected String myTitle;
    protected JTextField myMessage;
    protected int myCurrentGuess;
    protected JottoPanel[] myGuesses;
    protected int myModelRoundTripCounter;

    protected static int GUESSES = 15;
    protected static int ourCounter = 0;
    protected static final JFileChooser ourChooser = new JFileChooser(".");
    protected static final Font ourFont = new Font("SansSerif", Font.BOLD, 12);
    protected static final Font ourFixedFont = new Font("Monospaced", Font.BOLD, 12);
    

    /**
     * Construct Jotto view-controller
     * @param title is title displayed
     */
    public JottoViewer(String title, JottoModel model) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setModel(model);
        JPanel panel = (JPanel) getContentPane();
        panel.setLayout(new BorderLayout());
        setTitle(title);
        myTitle = title;
        myCurrentGuess = 0;
        myGuesses = new JottoPanel[GUESSES];
        panel.add(makeOutput(), BorderLayout.CENTER);
        panel.add(makeMessage(), BorderLayout.SOUTH);
        makeMenus();
        setStopped();
        pack();
        setVisible(true);
    }

    /**
     * Override menu-maker so as to add a new menu to the standard/default menu.
     * Note: constructs parent menu and adds new game menu to it.
     */
    protected JMenu makeGameMenu() {
        JMenu gameMenu = new JMenu("New Game");

        gameMenu.add(new AbstractAction("Play New Game") {
            public void actionPerformed(ActionEvent ev) {
                newGame();
            }
        });

        gameMenu.add(new AbstractAction("Smart/AI") {
            public void actionPerformed(ActionEvent ev) {
                myModel.playSmarter();
            }
        });
        return gameMenu;
    }

    /**
     * Reset/clear all textfields and make the first field the one 
     * with the focus. Set things up for computer guesses or user
     * guesses according to parameter.
     * @param userGuesses is true if user should guess secret word,
     * false if computer should guess secret word
     */
    protected void clearState(boolean userGuesses) {
        for (int k = myGuesses.length - 1; k >= 0; k--) {
            myGuesses[k].setUserGuesses(userGuesses);
            myGuesses[k].clear();

        }
        myCurrentGuess = 0;
        myModelRoundTripCounter = 0;
        myGuesses[0].requestFocus();
        myGuesses[0].setEnabled(true);
    }

    /**
     * Make a new game for human-user guessing secret word.
     * @param guesses is max number of guesses before game over
     */
    protected void newGame() {
        clearState(false);
        myModel.newGame();
    }

    /**
     * Turn off all user input, nothing will be allowed.
     * This will happen, for example, when game is over.
     */
    public void setStopped() {
        for (int k = 0; k < myGuesses.length; k++) {
            myGuesses[k].setEnabled(false);
        }
    }

    public void setModel(JottoModel model) {
        myModel = model;
        model.addView(this);
    }

    protected JPanel makeMessage() {
        JPanel p = new JPanel(new BorderLayout());
        myMessage = new JTextField(30);
        p.setBorder(BorderFactory.createTitledBorder("message"));
        p.add(myMessage, BorderLayout.CENTER);
        return p;
    }

    protected JPanel makeOutput() {

        JPanel p = new JPanel();
        BoxLayout layout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
        p.setLayout(layout);
        for (int k = 0; k < GUESSES; k++) {
            myGuesses[k] = new JottoPanel();
            p.add(myGuesses[k]);
            p.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        myGuesses[0].setEnabled(true);
        p.setBorder(BorderFactory.createTitledBorder("guesses"));
        return p;

    }

    protected JMenu makeFileMenu() {
        JMenu fileMenu = new JMenu("File");

        fileMenu.add(new AbstractAction("Open") {
            public void actionPerformed(ActionEvent ev) {
                int retval = ourChooser.showOpenDialog(null);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    File file = ourChooser.getSelectedFile();
                    try {
                        myModel.initialize(new Scanner(file));
                    } catch (FileNotFoundException e) {
                        showError("file not found " + file.getName());
                    }
                }
            }
        });

        fileMenu.add(new AbstractAction("Quit") {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });
        return fileMenu;
    }

    protected void makeMenus() {
        JMenuBar bar = new JMenuBar();
        bar.add(makeFileMenu());
        bar.add(makeGameMenu());
        setJMenuBar(bar);
    }

    public void badUserResponse(String word) {
        Toolkit.getDefaultToolkit().beep();
        myModelRoundTripCounter--;
        myGuesses[myCurrentGuess].redo();
    }

    protected void sendToModel(String s){
        myModelRoundTripCounter++;
        myModel.process(s);
    }
    
    public void processGuess(String s) {
        
        if (myModelRoundTripCounter == 2){
            myGuesses[myCurrentGuess].setUpNext();
            myCurrentGuess++;
            myModelRoundTripCounter = 0;
        }
        myModelRoundTripCounter++;
        myGuesses[myCurrentGuess].setResponse(s);
        if (myModelRoundTripCounter == 2){
            myCurrentGuess++;
            myModelRoundTripCounter = 0;
        }
    }

    public void showMessage(String s) {
        myMessage.setText(s);
    }

    public void showError(String s) {
        JOptionPane.showMessageDialog(this, s, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showModalInfo(String s) {
        JOptionPane.showMessageDialog(this, s, "Jotto Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    protected class JottoPanel extends JPanel {
        
        private JTextField myGuessField;
        private JTextField myCorrectField;
        private JTextField[] myFields;
        private int myInputField;
        private int myCount;

        public JottoPanel() {

            myGuessField = new JTextField(10);
            myGuessField.setFont(ourFont);
            myGuessField.setEnabled(false);

            myCorrectField = new JTextField(2);
            myCorrectField.setEnabled(false);
            myCorrectField.setFont(ourFont);

            myFields = new JTextField[2];
            myFields[0] = myGuessField;
            myFields[1] = myCorrectField;

            setUserGuesses(true);
            
            myGuessField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sendToModel(e.getActionCommand());
                }

            }); 
            myCorrectField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sendToModel(e.getActionCommand());
                }

            });

            BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
            setLayout(layout);
            myCount = ourCounter++;
            String space = ourCounter < 10 ? " " : "";
            JLabel countLabel = new JLabel(space + ourCounter);
            countLabel.setFont(ourFixedFont);
            countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            this.add(countLabel);
            countLabel.setLabelFor(myGuessField);
            this.add(Box.createRigidArea(new Dimension(10, 0)));
            this.add(myGuessField);
            this.add(Box.createRigidArea(new Dimension(10, 0)));
            this.add(myCorrectField);
        }

        public void setUserGuesses(boolean value) {
            if (value) {
                myInputField = 0;
            } else {
                myInputField = 1;
            }
        }

        public void setEnabled(boolean value) {
            myFields[myInputField].setEnabled(value);
            if (value) {
                myFields[myInputField].requestFocus();
            }
        }
        
        public void redo(){
            myGuesses[myCount].setEnabled(true);
            myFields[myInputField].setText("");
            myFields[myInputField].requestFocus();
        }

        public void clear() {
            myGuessField.setText("");
            myCorrectField.setText("");
            myFields[myInputField].requestFocus();
        }

        public void setResponse(String s) {
            myFields[1 - myInputField].setText(s);
            myFields[1 - myInputField].setEnabled(false);
            setUpNext();
            
        }
        public void setUpNext(){
            if (myModelRoundTripCounter == 2){
                myGuesses[myCount].setEnabled(false);
                if (myCount < myGuesses.length - 1) {
                    myGuesses[myCount + 1].setEnabled(true);
                }
            }
        }
    }
}
