import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * The GUI/View for Huffman coding assignment. Clients communicate
 * with this view by attaching a model and then using the menu choices/options that
 * are part of the GUI. Thus client code that fails to call <code>setModel</code> will
 * almost certainly not work and generate null pointer problems because the view/GUI will
 * not have an associated model.
 * <P>
 * @author Owen Astrachan
 *
 */
public class HuffViewer extends JFrame {
    
    private static String HUFF_SUFFIX = ".hf";
    private static String UNHUFF_SUFFIX = ".unhf";   
    private static String WHEELER_SUFFIX = ".bwm";
    private static String UNWHEELER_SUFFIX = ".unbw";
    private static boolean FAST_READER = true;
    
    
    protected JTextArea myOutput;
    protected IHuffProcessor myModel;
    protected String myTitle;
    protected JTextField myMessage;
    protected File myFile;
    private boolean myForce;
    private boolean myTransform;
    private Thread myFirstFileThread;
    private boolean myFirstReadingDone;

    protected static JFileChooser ourChooser = 
        new JFileChooser(System.getProperties().getProperty("user.dir"));

    public HuffViewer(String title) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = (JPanel) getContentPane();
        panel.setLayout(new BorderLayout());
        setTitle(title);
        myTitle = title;
        myForce = false;

        panel.add(makeOutput(), BorderLayout.CENTER);
        panel.add(makeMessage(), BorderLayout.SOUTH);
        makeMenus();

        pack();
        setSize(400, 400);
        setVisible(true);
    }

    /**
     * Associates this view with the given model. The GUI/View will 
     * attach itself to the model so that communication between the view
     * and the model as well as <em>vice versa</em> is supported.
     * @param model is the model for this view
     */
    public void setModel(IHuffProcessor model) {
        myModel = model;
        myModel.setViewer(this);
    }

    protected JPanel makeMessage() {
        JPanel p = new JPanel(new BorderLayout());
        myMessage = new JTextField(30);
        p.setBorder(BorderFactory.createTitledBorder("message"));
        p.add(myMessage, BorderLayout.CENTER);
        return p;
    }

    protected JPanel makeOutput() {
        JPanel p = new JPanel(new BorderLayout());
        myOutput = new JTextArea(10,40);
        p.setBorder(BorderFactory.createTitledBorder("output"));
        p.add(new JScrollPane(myOutput), BorderLayout.CENTER);
        return p;

    }

    protected File doRead() {

        int retval = ourChooser.showOpenDialog(null);
        if (retval != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        showMessage("reading/initializing");
        
       
        
        myFile = ourChooser.getSelectedFile();
        ProgressMonitorInputStream temp = null;
        if (FAST_READER){
            temp = getMonitorableStream(getFastByteReader(myFile),"counting/reading bits ...");
        }
        else {
            temp = getMonitorableStream(myFile,"counting/reading bits ...");
        }
        final ProgressMonitorInputStream pmis = temp;
        final ProgressMonitor progress = pmis.getProgressMonitor();
        try {
          
           
            myFirstFileThread = new Thread() {
                public void run() {
                    try {
                    	myFirstReadingDone = false;			    
                        int saved = myModel.preprocessCompress(pmis);
                        HuffViewer.this.showMessage("saved: "+saved+" bits");
                        myFirstReadingDone = true;
                    } catch (IOException e) {
                        HuffViewer.this.showError("reading exception");
                        e.printStackTrace();
                    }
                    if (progress.isCanceled()) {
                        HuffViewer.this.showError("reading cancelled");
                    }
                }
            };
            myFirstFileThread.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File ret = myFile;
        myFile = null;
        return ret;
    }

    protected JMenu makeOptionsMenu() {
        JMenu menu = new JMenu("Options");

        JCheckBoxMenuItem force = new JCheckBoxMenuItem(new AbstractAction(
                "Force Compression") {
            public void actionPerformed(ActionEvent ev) {
                myForce = !myForce;
            }
        });
        menu.add(force);
        return menu;

    }

    protected JMenu makeFileMenu() {
        JMenu fileMenu = new JMenu("File");

        fileMenu.add(new AbstractAction("Open/Count") {
            public void actionPerformed(ActionEvent ev) {
                doRead();
            }
        });

        fileMenu.add(new AbstractAction("Compress") {
            public void actionPerformed(ActionEvent ev) {
                myTransform = false;
                doSave();
            }
        });
        
        fileMenu.add(new AbstractAction("Transform"){
            
            public void actionPerformed(ActionEvent ev){
                myTransform = true;
                doSave();
            }
        });

        fileMenu.add(new AbstractAction("Uncompress") {
            public void actionPerformed(ActionEvent ev) {
                doDecode();
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
        bar.add(makeOptionsMenu());
        setJMenuBar(bar);
    }

    private void doDecode() {
        File file = null;
        showMessage("uncompressing");
        try {
            int retval = ourChooser.showOpenDialog(null);
            if (retval != JFileChooser.APPROVE_OPTION) {
                return;
            }
            file = ourChooser.getSelectedFile();
            String name = file.getName();
            String uname = name;
            if (name.endsWith(HUFF_SUFFIX)) {
                uname = name.substring(0,name.length() - HUFF_SUFFIX.length()) + UNHUFF_SUFFIX;
            }
            else if (name.endsWith(WHEELER_SUFFIX)){
                uname = name.substring(0,name.length() - WHEELER_SUFFIX.length()) + UNWHEELER_SUFFIX;
            }
            else {
                uname = name + UNHUFF_SUFFIX;
            }
            String newName = JOptionPane.showInputDialog(this,
                    "Name of uncompressed file", uname);
            if (newName == null) {
                return;
            }
            String path = file.getCanonicalPath();

            int pos = path.lastIndexOf(name);
            newName = path.substring(0, pos) + newName;
            final File newFile = new File(newName);
            ProgressMonitorInputStream temp = null;
            if (FAST_READER){
                temp = getMonitorableStream(getFastByteReader(file),"uncompressing bits ...");
            }
            else {
                temp = getMonitorableStream(file, "uncompressing bits...");
            }
            final ProgressMonitorInputStream stream = temp;
                
            final ProgressMonitor progress = stream.getProgressMonitor();
            final OutputStream out = new FileOutputStream(newFile);
            Thread fileReaderThread = new Thread() {
                public void run() {
                    try {
                        myModel.uncompress(stream, out);
                    } catch (IOException e) {
                        HuffViewer.this.showError("reading exception");
                        e.printStackTrace();
                    }
                    if (progress.isCanceled()) {
                        HuffViewer.this.showError("reading cancelled");
                    }
                }
            };
            fileReaderThread.start();
        } catch (FileNotFoundException e) {
            showError("could not open " + file.getName());
            e.printStackTrace();
        } catch (IOException e) {
            showError("IOException, uncompression halted from viewer");
            e.printStackTrace();
        }
    }

    private void doSave() {
        myFile = doRead();
        if (myFile == null){
            return;
        }
        String name = myFile.getName();
        showMessage("compressing "+name);
        String newName = name + HUFF_SUFFIX;
        if (myTransform){
            newName = name + WHEELER_SUFFIX;
        }
        
        
        newName = JOptionPane.showInputDialog(this,
                "Name of compressed file", newName);
        if (newName == null) {
            return;
        }
        String path = null;
        try {
            path = myFile.getCanonicalPath();
        } catch (IOException e) {
            showError("trouble with file canonicalizing");
            return;
        }
        int pos = path.lastIndexOf(name);
        newName = path.substring(0, pos) + newName;
        final File file = new File(newName);
        try {
            final FileOutputStream out = new FileOutputStream(file);
            ProgressMonitorInputStream  temp = null;
            if (FAST_READER){
                temp = getMonitorableStream(getFastByteReader(myFile),"compressing bits...");
            }
            else {
                temp = getMonitorableStream(myFile,"compressing bits ...");
            }
            final ProgressMonitorInputStream pmis = temp; 
            final ProgressMonitor progress = pmis.getProgressMonitor();
            Thread fileWriterThread = new Thread() {
                public void run() {
                    try {
			    while (! myFirstReadingDone){
				    try{
					    sleep(100);
				    } catch(InterruptedException e){
					    HuffViewer.this.showError("Trouble int Thread");
				    }
			    }
                        if (!myTransform){
                            myModel.compress(pmis, out,myForce);
                        }
                        else {
                            myModel.transform(pmis,out);
                        }
                    } catch (IOException e) {
                        HuffViewer.this.showError("compression exception");
                        e.printStackTrace();
                    }
                    if (progress.isCanceled()) {
                        HuffViewer.this.showError("compression cancelled");
                        cleanUp(file);
                    }
                }
            };
            fileWriterThread.start();
        } catch (FileNotFoundException e) {
            showError("could not open " + file.getName());
            e.printStackTrace();
        }
        myFile = null;
    }

    private void cleanUp(File f) {
        if (!f.delete()) {
            showError("trouble deleting " + f.getName());
        } else {
            // do something here?
        }
    }

    private ProgressMonitorInputStream getMonitorableStream(InputStream stream, String message) {

           
            final ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(
                    this, message, stream);

            ProgressMonitor progress = pmis.getProgressMonitor();
            progress.setMillisToDecideToPopup(1);
            progress.setMillisToPopup(1);

            return pmis;
    }
    
    
    
    private ProgressMonitorInputStream getMonitorableStream(File file,
            String message) {
        try {
            FileInputStream stream = new FileInputStream(file);
            if (stream == null){
                System.out.println("null on "+file.getCanonicalPath());
            }
            final ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(
                    this, message, stream);

            ProgressMonitor progress = pmis.getProgressMonitor();
            progress.setMillisToDecideToPopup(1);
            progress.setMillisToPopup(1);

            return pmis;
        } catch (IOException e) {
            showError("could not open " + file.getName());
            e.printStackTrace();
            return null;
        }
    }
    

    /**
     * To be called by model/client code to display strings in the GUI. Each object
     * in parameter elements will be displayed as a string in this view.
     * @param elements is source of objects that will be displayed, each object's
     * <code>toString</code> method wil be called to display.
     */
    public void update(Collection elements) {
        showMessage("");
        myOutput.setText("");
        for(Object o : elements){
            myOutput.append(o+"\n");
        }
    }

    /**
     * Display a text message in the view (e.g., in the small text area
     * at the bottom of the GUI), thus a modeless message the user can ignore.
     * @param s is the message displayed
     */
    public void showMessage(String s) {
        myMessage.setText(s);
    }

    /**
     * Show a modal-dialog indicating an error; the user must dismiss the
     * displayed dialog.
     * @param s is the error-message displayed
     */
    public void showError(String s) {
        JOptionPane.showMessageDialog(this, s, "Huff info",
                JOptionPane.INFORMATION_MESSAGE);
    }
    private ByteArrayInputStream getFastByteReader(File f){
        ByteBuffer buffer = null;
         try {
             FileChannel channel = new FileInputStream(f).getChannel();
             buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
             byte[] barray = new byte[buffer.limit()];
           
             if (barray.length != channel.size()){               
                 showError(String.format("Reading %s error: lengths differ %d %ld\n",f.getName(),barray.length,channel.size()));
             }
             buffer.get(barray);
             return new ByteArrayInputStream(barray);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return null;
    }
         
}
