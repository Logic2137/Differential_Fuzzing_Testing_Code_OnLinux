



import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.border.Border;

public class Test6461042 extends JComponent implements Border {
    public static void main(String[] args) {
        new Test6461042().toString();
    }

    public Test6461042() {
        setBorder(this);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    }

    public Insets getBorderInsets(Component c) {
        return null;
    }

    public boolean isBorderOpaque() {
        return false;
    }
}
