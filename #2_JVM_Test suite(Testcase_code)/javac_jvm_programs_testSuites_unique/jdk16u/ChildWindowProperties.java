



import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Window;

public class ChildWindowProperties {

    private Dialog parentDialog;
    private Window windowChild;
    private Frame parentFrame;
    private Window frameChildWindow;
    private Label parentLabel;
    private Font parentFont;
    private Label childLabel;

    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;

    public void testChildPropertiesWithDialogAsParent() {

        parentDialog = new Dialog((Dialog) null, "parent Dialog");
        parentDialog.setSize(WIDTH, HEIGHT);
        parentDialog.setLocation(100, 100);
        parentDialog.setBackground(Color.RED);
        parentLabel = new Label("ParentForegroundAndFont");
        parentFont = new Font("Courier New", Font.ITALIC, 15);
        parentDialog.setForeground(Color.BLUE);
        parentDialog.setFont(parentFont);

        parentDialog.add(parentLabel);
        parentDialog.setVisible(true);

        windowChild = new Window(parentDialog);
        windowChild.setSize(WIDTH, HEIGHT);
        windowChild.setLocation(WIDTH + 200, 100);
        childLabel = new Label("ChildForegroundAndFont");
        windowChild.add(childLabel);
        windowChild.setVisible(true);

        if (parentDialog.getBackground() == windowChild.getBackground()) {
            dispose();
            throw new RuntimeException("Child Window Should NOT Inherit "
                    + "Parent Dialog's Background Color");
        }
        if (parentDialog.getForeground() == windowChild.getForeground()) {
            dispose();
            throw new RuntimeException("Child Window Should NOT Inherit "
                    + "Parent Dialog's Foreground Color");
        }
        if (parentDialog.getFont() == windowChild.getFont()) {
            dispose();
            throw new RuntimeException("Child Window Should NOT Inherit "
                    + "Parent Dialog's Font Color");
        }
    }

    public void testChildPropertiesWithFrameAsParent() {

        parentFrame = new Frame("parent Frame");
        parentFrame.setSize(WIDTH, HEIGHT);
        parentFrame.setLocation(100, 400);
        parentFrame.setBackground(Color.BLUE);
        parentLabel = new Label("ParentForegroundAndFont");
        parentFont = new Font("Courier New", Font.ITALIC, 15);
        parentFrame.setForeground(Color.RED);
        parentFrame.setFont(parentFont);
        parentFrame.add(parentLabel);
        parentFrame.setVisible(true);

        frameChildWindow = new Window(parentFrame);
        frameChildWindow.setSize(WIDTH, HEIGHT);
        frameChildWindow.setLocation(WIDTH + 200, 400);
        childLabel = new Label("ChildForegroundAndFont");
        frameChildWindow.add(childLabel);
        frameChildWindow.setVisible(true);

        if (parentFrame.getBackground() == frameChildWindow.getBackground()) {
            dispose();
            throw new RuntimeException("Child Window Should NOT Inherit "
                    + "Parent Frame's Background Color");
        }
        if (parentDialog.getForeground() == windowChild.getForeground()) {
            dispose();
            throw new RuntimeException("Child Window Should NOT Inherit "
                    + "Parent Frame's Foreground Color");
        }
        if (parentDialog.getFont() == windowChild.getFont()) {
            dispose();
            throw new RuntimeException("Child Window Should NOT Inherit "
                    + "Parent Frame's Font Color");
        }
    }

    public void testPanelBackground() {
        Window window = new Frame();
        window.setBackground(Color.GREEN);
        Panel panel = new Panel();
        window.add(panel);
        window.pack();
        window.setVisible(true);
        if (panel.getBackground() != Color.GREEN) {
            window.dispose();
            throw new RuntimeException("Panel Background Color Not Valid");
        }
        window.dispose();
    }

    private void dispose() {

        if (parentDialog != null) {
            parentDialog.dispose();
        }
        if (parentFrame != null) {
            parentFrame.dispose();
        }
    }

    public static void main(String[] args) throws Exception {

        ChildWindowProperties obj = new ChildWindowProperties();
        
        obj.testChildPropertiesWithDialogAsParent();
        
        obj.testChildPropertiesWithFrameAsParent();
        
        obj.testPanelBackground();
        obj.dispose();
    }

}

