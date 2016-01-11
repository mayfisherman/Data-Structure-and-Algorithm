import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;


public class StoppableReader {
    public static ProgressMonitorInputStream 
                   getMonitorableStream(InputStream stream, String message) {
        
        if (stream == null){
            System.out.println("whoops");
        }
        final ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(
                null, message, stream);

        ProgressMonitor progress = pmis.getProgressMonitor();
        progress.setMillisToDecideToPopup(1);
        progress.setMillisToPopup(1);

        return pmis;
    }
    
    public static void doProcess(final ProgressMonitorInputStream pmis, final ILexicon lex, String message){
        final ProgressMonitor progress = pmis.getProgressMonitor();
        Thread fileReaderThread = new Thread() {
            public void run() {
                lex.load(new Scanner(pmis,"UTF-8"));
                if (progress.isCanceled()) {
                    showError("reading cancelled");
                }
            }
        };
        fileReaderThread.start();
    }
    
    public static void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Boggle Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
