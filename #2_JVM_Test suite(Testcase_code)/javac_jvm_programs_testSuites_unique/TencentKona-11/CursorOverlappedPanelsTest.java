

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public class CursorOverlappedPanelsTest extends JApplet {
    

    public void init() {
        
        
        
        this.setLayout(new BorderLayout());

        String[] instructions = {
            "Verify that the Crosshair cursor from enabled panel"
            + " is displayed on the panels intersection",
            "1) Move the mosue cursor on the Enabled and Disabled panels"
            + " intersection",
            "2) Check that the Crosshair cursor is displayed ",
            "If so, press PASS, else press FAIL."
        };
        Sysout.createDialogWithInstructions(instructions);

    }

    public void start() {
        
        setSize(200, 200);
        setVisible(true);
        validate();
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    
    
    private static JPanel createPanel(Point location, boolean enabled) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setEnabled(enabled);
        panel.setSize(new Dimension(200, 200));
        panel.setLocation(location);
        panel.setBorder(BorderFactory.createTitledBorder(
                enabled ? "Enabled" : "Disabled"));
        panel.setCursor(Cursor.getPredefinedCursor(
                enabled ? Cursor.CROSSHAIR_CURSOR : Cursor.DEFAULT_CURSOR));
        System.out.println("cursor: " + Cursor.getPredefinedCursor(enabled ? Cursor.CROSSHAIR_CURSOR : Cursor.DEFAULT_CURSOR));
        return panel;
    }

    private static void createAndShowGUI() {
        final JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(400, 400));
        JPanel enabledPanel = createPanel(new Point(10, 10), true);
        JPanel disabledPanel = createPanel(new Point(100, 100), false);
        layeredPane.add(disabledPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(enabledPanel, JLayeredPane.DEFAULT_LAYER);

        frame.getContentPane().add(layeredPane);
        frame.pack();
        frame.setVisible(true);
    }
}




class Sysout {

    private static TestDialog dialog;

    public static void createDialogWithInstructions(String[] instructions) {
        dialog = new TestDialog(new Frame(), "Instructions");
        dialog.printInstructions(instructions);
        dialog.setVisible(true);
        println("Any messages for the tester will display here.");
    }

    public static void createDialog() {
        dialog = new TestDialog(new Frame(), "Instructions");
        String[] defInstr = {"Instructions will appear here. ", ""};
        dialog.printInstructions(defInstr);
        dialog.setVisible(true);
        println("Any messages for the tester will display here.");
    }

    public static void printInstructions(String[] instructions) {
        dialog.printInstructions(instructions);
    }

    public static void println(String messageIn) {
        dialog.displayMessage(messageIn);
    }
}


class TestDialog extends Dialog {

    TextArea instructionsText;
    TextArea messageText;
    int maxStringLength = 80;

    
    public TestDialog(Frame frame, String name) {
        super(frame, name);
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea("", 15, maxStringLength, scrollBoth);
        add("North", instructionsText);

        messageText = new TextArea("", 5, maxStringLength, scrollBoth);
        add("Center", messageText);

        pack();

        setVisible(true);
    }

    
    public void printInstructions(String[] instructions) {
        
        instructionsText.setText("");

        

        String printStr, remainingStr;
        for (int i = 0; i < instructions.length; i++) {
            
            remainingStr = instructions[ i];
            while (remainingStr.length() > 0) {
                
                if (remainingStr.length() >= maxStringLength) {
                    
                    int posOfSpace = remainingStr.
                            lastIndexOf(' ', maxStringLength - 1);

                    if (posOfSpace <= 0) {
                        posOfSpace = maxStringLength - 1;
                    }

                    printStr = remainingStr.substring(0, posOfSpace + 1);
                    remainingStr = remainingStr.substring(posOfSpace + 1);
                } 
                else {
                    printStr = remainingStr;
                    remainingStr = "";
                }

                instructionsText.append(printStr + "\n");

            }

        }

    }

    
    public void displayMessage(String messageIn) {
        messageText.append(messageIn + "\n");
        System.out.println(messageIn);
    }
}
