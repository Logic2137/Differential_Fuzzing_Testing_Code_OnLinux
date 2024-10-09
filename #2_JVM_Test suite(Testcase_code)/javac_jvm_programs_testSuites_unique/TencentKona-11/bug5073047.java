



import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

public class bug5073047 {

    public static void main(String[] args) throws Exception{
        MyTheme theme = new MyTheme();
        MetalLookAndFeel.setCurrentTheme(theme);
        UIManager.setLookAndFeel(new MetalLookAndFeel());
        if (UIManager.get("Button.font") != theme.ctf) {
            throw new RuntimeException("Unexpected font");
        }
    }

    private static class MyTheme extends DefaultMetalTheme {
        public final FontUIResource ctf = new FontUIResource(
                super.getControlTextFont().deriveFont(40.0f));
        public FontUIResource getControlTextFont() {
            return ctf;
        }
    }
}
