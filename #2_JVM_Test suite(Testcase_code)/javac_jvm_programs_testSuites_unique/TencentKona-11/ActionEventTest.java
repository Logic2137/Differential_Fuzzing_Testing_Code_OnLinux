



import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ActionEventTest extends Frame {

    MenuBar menuBar;
    TextArea instructions;
    public static boolean isProgInterruption = false;
    static Thread mainThread = null;
    static int sleepTime = 300000;

    public ActionEventTest() {
        menuBar = new MenuBar();
        Menu menu = new Menu("Menu1");
        MenuItem menuItem = new MenuItem("MenuItem");

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("actionPerformed");
                int md = ae.getModifiers();
                int expectedMask = ActionEvent.ALT_MASK | ActionEvent.CTRL_MASK
                        | ActionEvent.SHIFT_MASK;

                isProgInterruption = true;
                mainThread.interrupt();
                if ((md & expectedMask) != expectedMask) {
                    throw new RuntimeException("Action Event modifiers are not"
                        + " set correctly.");
                }
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        setMenuBar(menuBar);

        instructions = new TextArea(10, 50);
        instructions.setText(
        " This is a manual test\n" +
        " Keep the Alt, Shift & Ctrl Keys pressed while doing next steps\n" +
        " Click 'Menu1' Menu from the Menu Bar\n" +
        " It will show 'MenuItem'\n" +
        " Left mouse Click the 'MenuItem'\n" +
        " Test exits automatically after mouse click.");
        add(instructions);

        setSize(400, 400);
        setVisible(true);
        validate();
    }


    public static void main(final String[] args) throws Exception {
        mainThread = Thread.currentThread();
        ActionEventTest test = new ActionEventTest();
        try {
            mainThread.sleep(sleepTime);
        } catch (InterruptedException e) {
            if (!isProgInterruption) {
                throw e;
            }
        }
        test.dispose();
        if (!isProgInterruption) {
            throw new RuntimeException("Timed out after " + sleepTime / 1000
                    + " seconds");
        }
    }
}
