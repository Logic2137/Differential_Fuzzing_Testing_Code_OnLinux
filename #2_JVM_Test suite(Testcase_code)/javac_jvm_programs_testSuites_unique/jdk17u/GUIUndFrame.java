import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIUndFrame extends Frame {

    JFrame jframe1, jframe2, jframe3;

    Component comp;

    JButton jbutton1, jbutton2, jbutton3, jbutton4;

    JTextArea jtextarea;

    volatile boolean win_act, win_deact, win_ico, win_deico, win_close;

    public GUIUndFrame() {
        jframe1 = new JFrame();
        jframe1.getContentPane().setLayout(new FlowLayout());
        jframe1.setSize(500, 255);
        jframe1.setUndecorated(true);
        jframe1.getContentPane().setBackground(Color.red);
        jframe1.addWindowListener(new WindowAdapter() {

            public void windowActivated(WindowEvent e) {
                comp = null;
                comp = e.getComponent();
                if (e.getComponent() == jframe1)
                    win_act = true;
            }

            public void windowDeactivated(WindowEvent e) {
                win_deact = true;
            }
        });
        jbutton1 = new JButton("Hide me");
        jbutton1.addActionListener(e -> jframe1.setVisible(false));
        jframe2 = new JFrame();
        jframe2.getContentPane().setLayout(new FlowLayout());
        jframe2.setLocation(0, 270);
        jframe2.setSize(500, 255);
        jframe2.getContentPane().setBackground(Color.blue);
        jbutton2 = new JButton("Show hiddenJFrame");
        jbutton2.addActionListener(e -> jframe1.setVisible(true));
        jframe3 = new JFrame();
        jframe3.getContentPane().setLayout(new FlowLayout());
        jframe3.setSize(500, 255);
        jframe3.getContentPane().setBackground(Color.green);
        jframe3.setUndecorated(true);
        jframe3.addWindowListener(new WindowAdapter() {

            public void windowActivated(WindowEvent e) {
                comp = null;
                comp = e.getComponent();
                if (e.getComponent() == jframe3) {
                    win_act = true;
                }
            }

            public void windowIconified(WindowEvent e) {
                win_ico = true;
            }

            public void windowDeiconified(WindowEvent e) {
                win_deico = true;
            }

            public void windowDeactivated(WindowEvent e) {
                win_deact = true;
            }

            public void windowClosed(WindowEvent e) {
                win_close = true;
            }
        });
        jbutton3 = new JButton("Minimize me");
        jbutton3.addActionListener(e -> jframe3.setState(Frame.ICONIFIED));
        jbutton4 = new JButton("Maximize me");
        jbutton4.addActionListener(e -> {
            if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
                jframe3.setExtendedState(Frame.MAXIMIZED_BOTH);
            }
        });
        jtextarea = new JTextArea("Textarea");
    }
}
