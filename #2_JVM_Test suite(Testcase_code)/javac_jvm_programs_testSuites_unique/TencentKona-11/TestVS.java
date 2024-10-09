



import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;

public class TestVS {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TestVS().run();
            }
        });
    }

    private void run()  {
        Font ourFont = null;
        final String fontName = "ipaexm.ttf";
        
        
        try {
            ourFont = Font.createFont(Font.TRUETYPE_FONT,
                          new java.io.File(new java.io.File(
                              System.getProperty("user.home"),
                              "fonts"), fontName));
            ourFont = ourFont.deriveFont((float)48.0);
            final String actualFontName = ourFont.getFontName();
            if (!actualFontName.equals("IPAexMincho")) {
                System.err.println("*** Warning: missing font IPAexMincho.");
                System.err.println("*** Using font: " + actualFontName);
            }
        } catch(Throwable t) {
            t.printStackTrace();
            System.err.println("Fail: " + t);
            return;
        }
        JFrame frame = new JFrame(System.getProperty("java.version"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        final JTextArea label = new JTextArea("empty");
        label.setSize(400, 300);
        label.setBorder(new LineBorder(Color.black));
        label.setFont(ourFont);

        final String str = "\u845b\udb40\udd00\u845b\udb40\udd01\n";

        label.setText(str);

        panel.add(label);
        panel.add(new JLabel(ourFont.getFamily()));

        
        panel.add(new JLabel(new ImageIcon("TestVS-expect.png")));

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
