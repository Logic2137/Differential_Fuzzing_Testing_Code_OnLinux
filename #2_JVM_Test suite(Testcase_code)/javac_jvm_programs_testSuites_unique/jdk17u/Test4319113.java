import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintStream;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Test4319113 extends JApplet implements ActionListener {

    private final JFrame frame = new JFrame("frame");

    private JComboBox cbPlaf;

    @Override
    public void init() {
        try {
            java.awt.EventQueue.invokeLater(() -> {
                Test4319113.this.frame.setLayout(new GridLayout(2, 1));
                Test4319113.this.show(Test4319113.this.frame);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        Component component = object instanceof Component ? (Component) object : null;
        JDialog jDialog = JColorChooser.createDialog(component, "ColorChooser", false, new JColorChooser(Color.BLUE), null, null);
        jDialog.setVisible(true);
    }

    private void show(Window window) {
        JButton jButton = new JButton("Show ColorChooser");
        jButton.setActionCommand("Show ColorChooser");
        jButton.addActionListener(this);
        this.cbPlaf = new JComboBox<UIManager.LookAndFeelInfo>(UIManager.getInstalledLookAndFeels());
        this.cbPlaf.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            UIManager.LookAndFeelInfo lookAndFeelInfo = (UIManager.LookAndFeelInfo) Test4319113.this.cbPlaf.getSelectedItem();
                            try {
                                UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                                Frame[] arrframe = Frame.getFrames();
                                int n = arrframe.length;
                                while (--n >= 0) {
                                    Test4319113.updateWindowTreeUI(arrframe[n]);
                                }
                            } catch (Exception var2_3) {
                                System.err.println("Exception while changing L&F!");
                            }
                        }
                    });
                }
            }
        });
        window.add(this.cbPlaf);
        window.add(jButton);
        window.pack();
        window.setVisible(true);
    }

    private static void updateWindowTreeUI(Window window) {
        SwingUtilities.updateComponentTreeUI(window);
        Window[] arrwindow = window.getOwnedWindows();
        int n = arrwindow.length;
        while (--n >= 0) {
            Window window2 = arrwindow[n];
            if (!window2.isDisplayable())
                continue;
            Test4319113.updateWindowTreeUI(window2);
        }
    }
}
