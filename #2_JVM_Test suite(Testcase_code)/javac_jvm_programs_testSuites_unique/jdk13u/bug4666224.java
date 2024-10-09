import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class bug4666224 extends JApplet {

    final static int[] placements = { JTabbedPane.LEFT, JTabbedPane.RIGHT, JTabbedPane.TOP, JTabbedPane.BOTTOM };

    private JTabbedPane tabPane;

    private JPanel mainPanel;

    public bug4666224() throws Exception {
        java.awt.EventQueue.invokeAndWait(() -> {
            tabPane = new JTabbedPane();
            tabPane.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    Point pt = e.getPoint();
                    System.out.println("Index at location: " + tabPane.indexAtLocation(pt.x, pt.y));
                }
            });
            InputMap inputMap = createInputMap();
            SwingUtilities.replaceUIInputMap(getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);
            ActionMap actionMap = createActionMap();
            SwingUtilities.replaceUIActionMap(getRootPane(), actionMap);
            tabPane.setTabPlacement(JTabbedPane.TOP);
            tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(200, 300));
            tabPane.addTab("Number Zero", panel);
            panel = new JPanel();
            panel.setPreferredSize(new Dimension(200, 300));
            tabPane.addTab("Number One", panel);
            panel = new JPanel();
            panel.setPreferredSize(new Dimension(200, 300));
            tabPane.addTab("Number Two", panel);
            panel = new JPanel();
            panel.setPreferredSize(new Dimension(200, 300));
            tabPane.addTab("Number Three", new JColorChooser());
            panel = new JPanel();
            panel.setPreferredSize(new Dimension(200, 300));
            tabPane.addTab("Number Four", panel);
            mainPanel = new JPanel();
            mainPanel.add(tabPane);
            getContentPane().add(mainPanel);
            tabPane.requestFocus();
        });
    }

    public void init() {
        String[][] instructionsSet = { { " Note : Incase of Assertion failure,user can enter", " remarks by pressing 'Assertion Fail Remarks ' button", " ", " You would see an applet with JTabbedPane. Keep the size of applet variable.", " ", " ON ALL PLATFORMS", "1. Click on any of the tabs, focus indicator is visible", "2. Lose focus on the window by clicking on some other window ", "3. Focus indicator should disappear", "4. Regain focus on the window the focus indicator should reappear.", " If focus doesn't behave as above, ", " press 'Assertion Fail' else press 'Assertion Pass'" }, { " Note : Incase of Assertion failure,user can enter", " remarks by pressing 'Assertion Fail Remarks ' button", " ", " You would see an applet with JTabbedPane. Keep the size of applet variable.", " ", " ON ALL PLATFORMS", "1. type 'R' to align the tabs to the right side ", "2. Lose focus on the window by clicking on some other window ", "3. Focus indicator should disappear", "4. Regain focus on the window the focus indicator should reappear.", " If focus doesn't behave as above, ", " press 'Assertion Fail' else press 'Assertion Pass'" }, { " Note : Incase of Assertion failure,user can enter", " remarks by pressing 'Assertion Fail Remarks ' button", " ", " You would see an applet with JTabbedPane. Keep the size of applet variable.", " ", " ON ALL PLATFORMS", "1. type 'B' to align the tabs to the bottom side ", "2. Lose focus on the window by clicking on some other window ", "3. Focus indicator should disappear", "4. Regain focus on the window the focus indicator should reappear.", " If focus doesn't behave as above, ", " press 'Assertion Fail' else press 'Assertion Pass'" }, { " Note : Incase of Assertion failure,user can enter", " remarks by pressing 'Assertion Fail Remarks ' button", " ", " You would see an applet with JTabbedPane. Keep the size of applet variable.", " ", " ON ALL PLATFORMS", "1. type 'L' to align the tabs to the left side ", "2. Lose focus on the window by clicking on some other window ", "3. Focus indicator should disappear", "4. Regain focus on the window the focus indicator should reappear.", " If focus doesn't behave as above, ", " press 'Assertion Fail' else press 'Assertion Pass'" }, { " Note : Incase of Assertion failure,user can enter", " remarks by pressing 'Assertion Fail Remarks ' button", " ", " You would see an applet with JTabbedPane. Keep the size of applet variable.", " ", " ON ALL PLATFORMS", "1. type 'C' to change the tab layout to WRAP_TAB_LAYOUT ", "2. Lose focus on the window by clicking on some other window ", "3. Focus indicator should disappear", "4. Regain focus on the window the focus indicator should reappear.", " If focus doesn't behave as above, ", " press 'Assertion Fail' else press 'Assertion Pass'" }, { " Note : Incase of Assertion failure,user can enter", " remarks by pressing 'Assertion Fail Remarks ' button", " ", " You would see an applet with JTabbedPane. Keep the size of applet variable.", " ", " ON ALL PLATFORMS", "1. type 'T' to align the tabs to the top side ", "2. Lose focus on the window by clicking on some other window ", "3. Focus indicator should disappear", "4. Regain focus on the window the focus indicator should reappear.", " If focus doesn't behave as above, ", " press 'Assertion Fail' else press 'Assertion Pass'" }, { " Note : Incase of Assertion failure,user can enter", " remarks by pressing 'Assertion Fail Remarks ' button", " ", " You would see an applet with JTabbedPane. Keep the size of applet variable.", " ", " ON ALL PLATFORMS", "1. type 'B' to align the tabs to the bottom side ", "2. Lose focus on the window by clicking on some other window ", "3. Focus indicator should disappear", "4. Regain focus on the window the focus indicator should reappear.", " If focus doesn't behave as above, ", " press 'Assertion Fail' else press 'Assertion Pass'" }, { " Note : Incase of Assertion failure,user can enter", " remarks by pressing 'Assertion Fail Remarks ' button", " ", " You would see an applet with JTabbedPane. Keep the size of applet variable.", " ", " ON ALL PLATFORMS", "1. type 'R' to align the tabs to the right side ", "2. Lose focus on the window by clicking on some other window ", "3. Focus indicator should disappear", "4. Regain focus on the window the focus indicator should reappear.", " If focus doesn't behave as above, ", " press 'Assertion Fail' else press 'Assertion Pass'" } };
        String[] exceptionsSet = { "Focus painted incorrectly in tabbed pane(SCROLL_TAB_LAYOUT) when tabs aligned to the TOP of the window", "Focus painted incorrectly in tabbed pane(SCROLL_TAB_LAYOUT) when tabs aligned to the RIGHT of the window", "Focus painted incorrectly in tabbed pane(SCROLL_TAB_LAYOUT) when tabs aligned to the BOTTOM of the window", "Focus painted incorrectly in tabbed pane(SCROLL_TAB_LAYOUT) when tabs aligned to the LEFT of the window", "Focus painted incorrectly in tabbed pane(WRAP_TAB_LAYOUT) when tabs aligned to the LEFT of the window", "Focus painted incorrectly in tabbed pane(WRAP_TAB_LAYOUT) when tabs aligned to the TOP of the window", "Focus painted incorrectly in tabbed pane(WRAP_TAB_LAYOUT) when tabs aligned to the BOTTOM of the window", "Focus painted incorrectly in tabbed pane(WRAP_TAB_LAYOUT) when tabs aligned to the RIGHT of the window" };
        Sysout.setInstructionsWithExceptions(instructionsSet, exceptionsSet);
    }

    public void start() {
    }

    public void destroy() {
        if (Sysout.failStatus()) {
            String failMsg = Sysout.getFailureMessages();
            failMsg = failMsg.replace('\n', ' ');
            throw new RuntimeException(failMsg);
        }
    }

    protected InputMap createInputMap() {
        return LookAndFeel.makeComponentInputMap(getRootPane(), new Object[] { "R", "right", "L", "left", "T", "top", "B", "bottom", "C", "changeLayout", "D", "dump" });
    }

    protected ActionMap createActionMap() {
        ActionMap map = new ActionMap();
        map.put("right", new RotateAction(JTabbedPane.RIGHT));
        map.put("left", new RotateAction(JTabbedPane.LEFT));
        map.put("top", new RotateAction(JTabbedPane.TOP));
        map.put("bottom", new RotateAction(JTabbedPane.BOTTOM));
        map.put("changeLayout", new ChangeLayoutAction());
        map.put("dump", new DumpAction());
        return map;
    }

    private class RotateAction extends AbstractAction {

        private int placement;

        public RotateAction(int placement) {
            this.placement = placement;
        }

        public void actionPerformed(ActionEvent e) {
            tabPane.setTabPlacement(placement);
        }
    }

    private class ChangeLayoutAction extends AbstractAction {

        private boolean a = true;

        public void actionPerformed(ActionEvent e) {
            if (a) {
                tabPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
                a = false;
            } else {
                tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                a = true;
            }
        }
    }

    private class DumpAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < tabPane.getTabCount(); i++) {
                System.out.println("Tab: " + i + " " + tabPane.getUI().getTabBounds(tabPane, i));
            }
        }
    }
}

class Sysout {

    private static TestDialog dialog;

    public static void createDialogWithInstructions(String[] instructions) {
        dialog = new TestDialog(new Frame(), "Instructions");
        dialog.printInstructions(instructions);
        dialog.show();
        println("Any messages for the tester will display here.");
    }

    public static void createDialog() {
        dialog = new TestDialog(new Frame(), "Instructions");
        String[] defInstr = { "Instructions will appear here. ", "" };
        dialog.printInstructions(defInstr);
        dialog.show();
        println("Any messages for the tester will display here.");
    }

    public static void printInstructions(String[] instructions) {
        dialog.printInstructions(instructions);
    }

    public static void println(String messageIn) {
        dialog.displayMessage(messageIn);
    }

    public static void setInstructionsWithExceptions(String[][] instructionsSet, String[] exceptionsSet) {
        createDialogWithInstructions(instructionsSet[0]);
        dialog.setInstructions(instructionsSet);
        dialog.setExceptionMessages(exceptionsSet);
    }

    public static String getFailureMessages() {
        return dialog.failureMessages;
    }

    public static boolean failStatus() {
        return dialog.failStatus;
    }
}

class TestDialog extends Dialog {

    TextArea instructionsText;

    TextArea messageText;

    int maxStringLength = 70;

    Panel assertPanel;

    Button assertPass, assertFail, remarks;

    HandleAssert handleAssert;

    boolean failStatus = false;

    int instructionCounter = 0;

    String[][] instructions;

    int exceptionCounter = 0;

    String[] exceptionMessages;

    String failureMessages = "<br>";

    String remarksMessage = null;

    RemarksDialog remarksDialog;

    public TestDialog(Frame frame, String name) {
        super(frame, name);
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea("", 14, maxStringLength, scrollBoth);
        add("North", instructionsText);
        messageText = new TextArea("", 3, maxStringLength, scrollBoth);
        add("Center", messageText);
        assertPanel = new Panel(new FlowLayout());
        assertPass = new Button("Assertion Pass");
        assertPass.setName("Assertion Pass");
        assertFail = new Button("Assertion Fail");
        assertFail.setName("Assertion Fail");
        remarks = new Button("Assertion Fail Remarks");
        remarks.setEnabled(false);
        remarks.setName("Assertion Remarks");
        assertPanel.add(assertPass);
        assertPanel.add(assertFail);
        assertPanel.add(remarks);
        handleAssert = new HandleAssert();
        assertPass.addActionListener(handleAssert);
        assertFail.addActionListener(handleAssert);
        remarks.addActionListener(handleAssert);
        add("South", assertPanel);
        pack();
        show();
    }

    public void printInstructions(String[] instructions) {
        instructionsText.setText("");
        String printStr, remainingStr;
        for (int i = 0; i < instructions.length; i++) {
            remainingStr = instructions[i];
            while (remainingStr.length() > 0) {
                if (remainingStr.length() >= maxStringLength) {
                    int posOfSpace = remainingStr.lastIndexOf(' ', maxStringLength - 1);
                    if (posOfSpace <= 0)
                        posOfSpace = maxStringLength - 1;
                    printStr = remainingStr.substring(0, posOfSpace + 1);
                    remainingStr = remainingStr.substring(posOfSpace + 1);
                } else {
                    printStr = remainingStr;
                    remainingStr = "";
                }
                instructionsText.append(printStr + "\n");
            }
        }
    }

    public void displayMessage(String messageIn) {
        messageText.append(messageIn + "\n");
    }

    public void emptyMessage() {
        messageText.setText("");
    }

    public void setInstructions(String[][] insStr) {
        instructions = insStr;
    }

    public void setExceptionMessages(String[] exceptionMessages) {
        this.exceptionMessages = exceptionMessages;
    }

    class HandleAssert implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == remarks) {
                remarksDialog = new RemarksDialog(TestDialog.this, "Assertion Remarks Dialog", true);
                remarks.setEnabled(false);
                if (remarksMessage != null)
                    failureMessages += ". User Remarks : " + remarksMessage;
            } else {
                if (instructionCounter < instructions.length - 1) {
                    emptyMessage();
                    instructionCounter++;
                    printInstructions(instructions[instructionCounter]);
                } else {
                    emptyMessage();
                    displayMessage("Testcase Completed");
                    displayMessage("Press 'Done' button in the " + "BaseApplet to close");
                    assertPass.setEnabled(false);
                    assertFail.setEnabled(false);
                }
                if (ae.getSource() == assertPass) {
                } else if (ae.getSource() == assertFail) {
                    remarks.setEnabled(true);
                    if (!failStatus)
                        failStatus = true;
                    if (exceptionCounter < exceptionMessages.length) {
                        failureMessages = failureMessages + "<br>" + exceptionMessages[exceptionCounter];
                    }
                }
                exceptionCounter++;
            }
        }
    }

    class RemarksDialog extends Dialog implements ActionListener {

        Panel rootPanel, remarksPanel;

        TextArea textarea;

        Button addRemarks, cancelRemarks;

        public RemarksDialog(Dialog owner, String title, boolean modal) {
            super(owner, title, modal);
            rootPanel = new Panel(new BorderLayout());
            remarksPanel = new Panel(new FlowLayout());
            textarea = new TextArea(5, 30);
            addRemarks = new Button("Add Remarks");
            addRemarks.addActionListener(this);
            cancelRemarks = new Button("Cancel Remarks");
            cancelRemarks.addActionListener(this);
            remarksPanel.add(addRemarks);
            remarksPanel.add(cancelRemarks);
            rootPanel.add(textarea, "Center");
            rootPanel.add(remarksPanel, "South");
            add(rootPanel);
            setBounds(150, 150, 400, 200);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent ae) {
            remarksMessage = null;
            if (ae.getSource() == addRemarks) {
                String msg = textarea.getText().trim();
                if (msg.length() > 0)
                    remarksMessage = msg;
            }
            dispose();
        }
    }
}
