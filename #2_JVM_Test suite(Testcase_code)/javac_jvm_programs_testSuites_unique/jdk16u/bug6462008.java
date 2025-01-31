


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class bug6462008 {

    private static final int DONT_CARE = -2;
    private static int anchorLead;
    private static boolean isAquaLAF;
    private static int controlKey;
    private static JList list;
    private static Robot robot;
    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        try {
            robot = new Robot();
            robot.setAutoDelay(100);

            isAquaLAF = "Aqua".equals(UIManager.getLookAndFeel().getID());
            controlKey = isAquaLAF ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;

            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    createAndShowGUI();
                }
            });

            robot.waitForIdle();
            robot.delay(1000);

            setAnchorLead(-1);
            robot.waitForIdle();

            testListSelection();

            setAnchorLead(100);
            robot.waitForIdle();

            testListSelection();
        } finally {
            if (frame != null) SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }

    public static void testListSelection() throws Exception {

        
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);

        robot.waitForIdle();
        checkSelection();
        resetList();
        robot.waitForIdle();

        
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        robot.waitForIdle();
        checkSelection();
        resetList();
        robot.waitForIdle();

        
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SHIFT);

        robot.waitForIdle();
        checkSelection();
        resetList();
        robot.waitForIdle();

        
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        robot.waitForIdle();
        checkSelection();
        resetList();
        robot.waitForIdle();


        

        robot.keyPress(controlKey);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(controlKey);

        robot.waitForIdle();
        checkSelectionAL(-1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        resetList();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        robot.waitForIdle();

        
        robot.keyPress(controlKey);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(controlKey);

        robot.waitForIdle();
        checkSelectionAL(0, 0, 0);
        resetList();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setSelectionInterval(5, 5);
        robot.waitForIdle();


        
        robot.keyPress(controlKey);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(controlKey);

        robot.waitForIdle();
        checkSelection(5);
        resetList();
        robot.waitForIdle();

        
        
        if (!isAquaLAF) {
            robot.keyPress(KeyEvent.VK_PAGE_DOWN);
            robot.keyRelease(KeyEvent.VK_PAGE_DOWN);

            robot.waitForIdle();
            checkSelection(9, 9, 9);
            resetList();
            robot.waitForIdle();
        }

        
        

        scrollDownExtendSelection();

        robot.waitForIdle();
        checkSelection(0, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        resetList();
        robot.waitForIdle();

        
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);

        robot.waitForIdle();
        checkSelectionAL(0, 0, 0);
        resetList();
        robot.waitForIdle();

        
        robot.keyPress(KeyEvent.VK_L);
        robot.keyRelease(KeyEvent.VK_L);

        robot.waitForIdle();
        checkSelectionAL(0, 0, 0);
        resetList();
        robot.waitForIdle();

        
        Point p = clickItem4();
        robot.mouseMove(p.x, p.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);


        robot.waitForIdle();
        checkSelectionAL(4, 4, 4);
        resetList();
        robot.waitForIdle();


        
        robot.keyPress(controlKey);
        p = clickItem4();
        robot.mouseMove(p.x, p.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.keyRelease(controlKey);


        robot.waitForIdle();
        checkSelectionAL(4, 4, 4);
        resetList();
        robot.waitForIdle();

        
        robot.keyPress(KeyEvent.VK_SHIFT);
        p = clickItem4();
        robot.mouseMove(p.x, p.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.keyRelease(KeyEvent.VK_SHIFT);


        robot.waitForIdle();
        checkSelectionAL(0, 4, 0, 1, 2, 3, 4);
        resetList();
        robot.waitForIdle();


        
        robot.keyPress(controlKey);
        robot.keyPress(KeyEvent.VK_SHIFT);
        p = clickItem4();
        robot.mouseMove(p.x, p.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(controlKey);

        robot.waitForIdle();
        checkSelectionAL(0, 4);
        resetList();
        robot.waitForIdle();
    }

    private static DefaultListModel getModel() {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < 10; i++) {
            listModel.addElement("List Item " + i);
        }
        return listModel;
    }

    private static Point clickItem4() throws Exception {

        final Point[] result = new Point[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Rectangle r = list.getCellBounds(4, 4);
                Point p = new Point(r.x + r.width / 2, r.y + r.height / 2);
                SwingUtilities.convertPointToScreen(p, list);
                result[0] = p;
            }
        });

        return result[0];
    }

    private static void resetList() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                list.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                list.getSelectionModel().clearSelection();
                setAnchorLeadNonThreadSafe();
            }
        });
    }

    private static void scrollDownExtendSelection() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                list.getActionMap().get("scrollDownExtendSelection").
                        actionPerformed(new ActionEvent(list,
                        ActionEvent.ACTION_PERFORMED, null));
            }
        });
    }

    private static void setSelectionMode(final int selectionMode) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                list.getSelectionModel().setSelectionMode(selectionMode);
                setAnchorLeadNonThreadSafe();
            }
        });
    }

    private static void setSelectionInterval(final int index0, final int index1) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                list.getSelectionModel().setSelectionInterval(index0, index1);
                setAnchorLeadNonThreadSafe();
            }
        });
    }

    private static void setAnchorLead(final int anchorLeadValue) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                anchorLead = anchorLeadValue;
                setAnchorLeadNonThreadSafe();
            }
        });
    }

    private static void setAnchorLeadNonThreadSafe() {
        list.getSelectionModel().setAnchorSelectionIndex(anchorLead);
        ((DefaultListSelectionModel) list.getSelectionModel()).moveLeadSelectionIndex(anchorLead);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("bug6462008");
        frame.setSize(200, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        list = new JList(getModel());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(list);
        frame.getContentPane().add(panel);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static void checkSelection(int... sels) throws Exception {
        checkSelectionAL(DONT_CARE, DONT_CARE, sels);
    }

    private static void checkSelectionAL(final int anchor, final int lead, final int... sels) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                checkSelectionNonThreadSafe(anchor, lead, sels);
            }
        });
    }

    private static void checkSelectionNonThreadSafe(int anchor, int lead, int... sels) {
        ListSelectionModel lsm = list.getSelectionModel();

        int actualAnchor = lsm.getAnchorSelectionIndex();
        int actualLead = lsm.getLeadSelectionIndex();

        if (anchor != DONT_CARE && actualAnchor != anchor) {
            throw new RuntimeException("anchor is " + actualAnchor + ", should be " + anchor);
        }

        if (lead != DONT_CARE && actualLead != lead) {
            throw new RuntimeException("lead is " + actualLead + ", should be " + lead);
        }

        Arrays.sort(sels);
        boolean[] checks = new boolean[list.getModel().getSize()];
        for (int i : sels) {
            checks[i] = true;
        }

        int index0 = Math.min(lsm.getMinSelectionIndex(), 0);
        int index1 = Math.max(lsm.getMaxSelectionIndex(), list.getModel().getSize() - 1);

        for (int i = index0; i <= index1; i++) {
            if (lsm.isSelectedIndex(i)) {
                if (i < 0 || i >= list.getModel().getSize() || !checks[i]) {
                    throw new RuntimeException(i + " is selected when it should not be");
                }
            } else if (i >= 0 && i < list.getModel().getSize() && checks[i]) {
                throw new RuntimeException(i + " is supposed to be selected");
            }
        }
    }
}
