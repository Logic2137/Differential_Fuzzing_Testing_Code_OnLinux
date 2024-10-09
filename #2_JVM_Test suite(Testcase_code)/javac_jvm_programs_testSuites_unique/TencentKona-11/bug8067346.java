



import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class bug8067346 {

    private JMenuBar menuBar;
    private JFrame frame;
    private String[] menuClasses = {"MenuItem", "Menu",
        "CheckBoxMenuItem", "RadioButtonMenuItem"};
    private String MARGIN = ".margin";
    private String CHECKICONOFFSET = ".checkIconOffset";
    private static boolean runTest = true;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                bug8067346 test = new bug8067346();
                try {
                    
                    String lnf =
                           "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
                    UIManager.setLookAndFeel(lnf);
                } catch (UnsupportedLookAndFeelException e) {
                    runTest = false;
                } catch (ClassNotFoundException e) {
                    runTest = false;
                } catch (InstantiationException e) {
                    runTest = false;
                } catch (IllegalAccessException e) {
                    runTest = false;
                }
                if(runTest) {
                    test.createUI();
                    test.performTest();
                    test.dispose();
                }
            }
        });
    }

    public void createUI() {

        frame = new JFrame();
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu, submenu;
        JMenuItem menuItem;

        menu = new JMenu("A Menu");
        menuBar.add(menu);
        menu.addSeparator();

        submenu = new JMenu("A submenu");

        menuItem = new JMenuItem("An item in the submenu");
        submenu.add(menuItem);
        menu.add(submenu);
    }

    public void performTest() {
        try {
            String errorMessage = "Incorrect value for ";
            StringBuilder errorMessageBuilder = new StringBuilder(errorMessage);
            boolean error = false;
            int retVal = testMargin();
            if (retVal != 0) {
                errorMessageBuilder.append(menuClasses[retVal])
                        .append(MARGIN).append("\n");
                error = true;
            }
            retVal = testCheckIconOffset();
            if (retVal != 0) {
                errorMessageBuilder.append(errorMessage)
                        .append(menuClasses[retVal]).append(CHECKICONOFFSET);
            }
            if (error || retVal != 0) {
                throw new RuntimeException(errorMessageBuilder.toString());
            }
        } finally {
            dispose();
        }
    }

    private int testMargin() {

        for (int inx = 0; inx < menuClasses.length; inx++) {
            Insets margin = (Insets) UIManager.get(menuClasses[inx] + MARGIN);
            if (margin != null && margin.bottom == 0 && margin.left == 0
                    && margin.right == 0 && margin.top == 0) {
                return inx + 1;
            }
        }
        return 0;
    }

    private int testCheckIconOffset() {

        for (int inx = 0; inx < menuClasses.length; inx++) {
            Object checkIconOffset = UIManager.get(menuClasses[inx]
                    + CHECKICONOFFSET);
            if (checkIconOffset != null && ((Integer) checkIconOffset) == 0) {
                return inx + 1;
            }
        }
        return 0;
    }

    public void dispose() {
        frame.dispose();
    }
}
