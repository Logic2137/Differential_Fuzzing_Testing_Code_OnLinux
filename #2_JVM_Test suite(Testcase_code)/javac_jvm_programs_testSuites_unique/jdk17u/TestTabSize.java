import java.awt.geom.Rectangle2D;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

public class TestTabSize {

    private static JScrollPane jScrollPane1;

    private static JTextArea jTextArea1;

    private static JFrame f;

    private static Rectangle2D rect;

    private static Rectangle2D rect1;

    private static boolean excpnthrown = false;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                jScrollPane1 = new javax.swing.JScrollPane();
                jTextArea1 = new javax.swing.JTextArea();
                jTextArea1.setTabSize(8);
                f = new JFrame();
                jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 10));
                String str = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!#\n" + "! Some Text\t\t\t\t\t#";
                jTextArea1.setText(str);
                jScrollPane1.setViewportView(jTextArea1);
                GroupLayout layout = new javax.swing.GroupLayout(f.getContentPane());
                f.getContentPane().setLayout(layout);
                layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE).addContainerGap()));
                layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1).addContainerGap()));
                f.pack();
                int first = str.indexOf("#");
                jTextArea1.setCaretPosition(first);
                Caret caret = jTextArea1.getCaret();
                rect = jTextArea1.modelToView2D(caret.getDot());
                System.out.println("caret x position " + rect.getX());
                jTextArea1.setCaretPosition(str.indexOf("#", first + 1));
                caret = jTextArea1.getCaret();
                rect1 = jTextArea1.modelToView2D(caret.getDot());
                System.out.println("2nd caret x position " + rect1.getX());
            } catch (BadLocationException ex) {
                excpnthrown = true;
            } finally {
                if (f != null) {
                    f.dispose();
                }
            }
        });
        if (excpnthrown) {
            throw new RuntimeException("BadLocationException thrown");
        }
        if ((int) rect.getX() != (int) rect1.getX()) {
            throw new RuntimeException("Tab width calculation wrong");
        }
    }
}
