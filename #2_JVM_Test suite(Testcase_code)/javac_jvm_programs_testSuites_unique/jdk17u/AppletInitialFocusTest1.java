import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class AppletInitialFocusTest1 extends Frame implements FocusListener {

    Button button1 = new Button("Button1");

    Button button2 = new Button("Button2");

    private static volatile Object focused;

    public static void main(final String[] args) throws Exception {
        AppletInitialFocusTest1 app = new AppletInitialFocusTest1();
        try {
            app.setSize(200, 200);
            app.setLocationRelativeTo(null);
            app.setLayout(new FlowLayout());
            app.button1.addFocusListener(app);
            app.button2.addFocusListener(app);
            app.add(app.button1);
            app.add(app.button2);
            app.setVisible(true);
            app.button2.requestFocus();
            Thread.sleep(10000);
            if (app.button2 != focused) {
                throw new RuntimeException("Wrong focus owner: " + focused);
            }
        } finally {
            app.dispose();
        }
    }

    public void focusGained(FocusEvent e) {
        focused = e.getSource();
        System.out.println("focused = " + focused);
    }

    public void focusLost(FocusEvent e) {
    }
}
