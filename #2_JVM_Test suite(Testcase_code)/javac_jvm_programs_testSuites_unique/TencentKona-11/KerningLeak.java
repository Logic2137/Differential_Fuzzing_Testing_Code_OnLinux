



import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class KerningLeak {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                leak();
            }
        });
    }

    private static void leak() {
        Map<TextAttribute, Object> textAttributes = new HashMap<>();
        textAttributes.put(TextAttribute.FAMILY, "Sans Serif");
        textAttributes.put(TextAttribute.SIZE, 12);
        textAttributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        Font font = Font.getFont(textAttributes);
        JLabel label = new JLabel();
        int dummy = 0;
        for (int i = 0; i < 500; i++) {
            if (i % 10 == 0) System.out.println("Starting iter " + (i+1));
            for (int j = 0; j <1000; j++) {
                FontMetrics fm = label.getFontMetrics(font);
                dummy += SwingUtilities.computeStringWidth(fm, Integer.toString(j));
            }
        }
        System.out.println("done " + dummy);
    }
}
