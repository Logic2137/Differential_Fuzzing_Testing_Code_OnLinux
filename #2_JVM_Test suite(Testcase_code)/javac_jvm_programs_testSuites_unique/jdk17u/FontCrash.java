import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;

public class FontCrash {

    public static void main(String[] args) {
        System.setProperty("java2d.font.usePlatformFont", "true");
        Font f = new Font(Font.DIALOG, Font.PLAIN, 12);
        Toolkit tk = Toolkit.getDefaultToolkit();
        tk.getFontMetrics(f);
    }
}
