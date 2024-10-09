


import javax.swing.*;
import javax.swing.text.*;

public class bug4984669 extends JApplet
{
    public void init() {
        JEditorPane pane = new JEditorPane();
        this.getContentPane().add(new JScrollPane(pane));
        pane.setEditorKit(new StyledEditorKit());

        try {
            pane.getDocument().insertString(0,"12   \n",null);
            MutableAttributeSet attrs = new SimpleAttributeSet();

            StyleConstants.setFontSize(attrs, 36);
            StyleConstants.setBold(attrs, true);
            StyleConstants.setUnderline(attrs, true);
            pane.getDocument().insertString(6, "aa\n", attrs);
            pane.getDocument().insertString(9, "bbb\n", attrs);
            pane.getDocument().insertString(13, "cccc\n", attrs);
            pane.getDocument().insertString(18, "ddddd\n", attrs);
        } catch (Exception e) {
            throw new Error("Failed: Unexpected Exception", e);
        }
    }
}
