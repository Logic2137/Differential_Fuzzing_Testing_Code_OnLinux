



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.text.Keymap;


public class TestNimbusOverride extends JFrame
{
    private static TestNimbusOverride tf;
    private static boolean passed = false;

    public static void main(String [] args) throws Exception {
        try {
            Robot robot = new Robot();
            SwingUtilities.invokeAndWait(() -> {
                try {
                    UIManager.setLookAndFeel(
                            "javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                tf = new TestNimbusOverride();
                tf.pack();
                tf.setVisible(true);
            });
            robot.setAutoDelay(100);
            robot.waitForIdle();
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_SPACE);
            robot.waitForIdle();
            if (!passed) {
                throw new RuntimeException(
                        "Setting Nimbus.Overrides property affects custom" +
                                " keymap installation");
            }
        } finally {
            SwingUtilities.invokeAndWait(() -> tf.dispose());
        }
    }

    public TestNimbusOverride()
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        JEditorPane pp = new JEditorPane();
        UIDefaults defaults = new UIDefaults();

        pp.putClientProperty("Nimbus.Overrides", defaults);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        setContentPane(contentPanel);

        contentPanel.setPreferredSize(new Dimension(400, 300));
        contentPanel.add(pp, BorderLayout.CENTER);

        Keymap origKeymap = pp.getKeymap();
        Keymap km = JEditorPane.addKeymap("Test keymap", origKeymap);

        km.addActionForKeyStroke(KeyStroke.getKeyStroke(' '),
                new AbstractAction("SHOW_SPACE") {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                passed = true;
            }
        });

        pp.setKeymap(km);
    }
}
