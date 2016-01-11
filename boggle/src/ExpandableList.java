import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * Subclass of JList intended to grow as elements are
 * added to the list (via the model, but that's not
 * seen by clients).
 *
 * Features added to this JList not part of standard JList
 * <UL>
 *   <LI> growable by calling add(..), JList updates after call
 *   <P>
 *   <LI> supports double-clicking action/action listeners
 *   <P>
 *        the default JList supports ListSelectionListeners,
 *        this class adds ActionListeners via double-clicking,
 *        selected items are converted to strings via toString()
 *   <P>
 *   <LI> because of action events, supports single selection only
 *   <P>
 *   <LI> constructable with # visible rows as parameter
 *        (done via JList.setVisibleRowCount)
 *
 * </UL>
 *
 * @author Owen Astrachan
 * @version 1.0 2/21/2001
 */

public class ExpandableList extends JList
{
    /**
     * construct an initially empty expandable list
     * with default # of entries (which is 8)
     */
    
    public final static int INITIAL_SIZE =15;
    
    private DefaultListModel myModel;       // model for JList view
    private List<ActionListener> myListeners;   // action listeners
    
    public ExpandableList()
    {
        this(INITIAL_SIZE);
    }

    /**
     * construct an initially empty expandable list
     * with specified # of visible slots
     * @param size is the number of visible slots
     */
    
    public ExpandableList(int size)
    {
    	myModel = new DefaultListModel();
    	myListeners = new ArrayList<ActionListener>();
    	setModel(myModel);
    	addMouseListener(new DoubleClick());
        this.setPrototypeCellValue("words are very long");
    	setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	setVisibleRowCount(size);
    }

    /**
     * add object to end of list
     * @param o is the object added
     */
    
    public synchronized void add(Object o)
    {
        myModel.addElement(o);   // old 1.1 vector interface
    }

    /**
     * Adds the specified action listener
     * @param a the action listener
     */
    
    public synchronized void addActionListener(ActionListener a) {
        myListeners.add(a);
    }

    /**
     * Removes the specified action listener
     * @param a the action listener 
     */
    
    public synchronized void removeActionListener(ActionListener a) {
        myListeners.remove(a);
    }
    /**
     * Notifies all listeners that have registered interest for
     * notification of action events.
     * Listeners are processed last-to-first (registration order)
     * @param selected is the item selected
     */
    
    protected void fireActionPerformed(Object selected) {

        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                        selected.toString());
	
        // process listeners registered last-to-first 

        for (int k = myListeners.size()-1; k >= 0; k--) {
    	    myListeners.get(k).actionPerformed(e);
    	}          
    }

   
    
    /**
     * from JList API, see also Core Java Volume I
     */
    
    class DoubleClick extends MouseAdapter
    {
    	public void mouseClicked(MouseEvent me)
    	{
    	    if (me.getClickCount() == 2) {
        		JList source = (JList) me.getSource();
        		Object selected = source.getSelectedValue();
        		ExpandableList.this.fireActionPerformed(selected);
    	    }
    	}
    }

    /**
     * 
     */
    public void clear() {
        myModel.clear();
    }
}
