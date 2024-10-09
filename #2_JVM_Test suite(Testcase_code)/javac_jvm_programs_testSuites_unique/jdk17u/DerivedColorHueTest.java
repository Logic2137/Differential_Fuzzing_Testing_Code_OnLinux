import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class DerivedColorHueTest {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        Color base = UIManager.getColor("nimbusBlueGrey");
        float[] hsbBase = hsb(base);
        float hueShift = 0.5f - 10;
        Color derived = ((NimbusLookAndFeel) UIManager.getLookAndFeel()).getDerivedColor("nimbusBlueGrey", hueShift, 0, 0, 0, false);
        Color derivedCorrect = new Color(Color.HSBtoRGB(hsbBase[0] + hueShift, hsbBase[1], hsbBase[2]));
        float hueDerived = hsb(derived)[0];
        float hueCorrect = hsb(derivedCorrect)[0];
        if (hueCorrect < 0.01f || hueCorrect > 0.99f)
            throw new RuntimeException("Test indeterminate! (Hue too close to red)");
        System.out.println(" base: " + hsbString(base));
        System.out.println(" derived: " + hsbString(derived));
        System.out.println("derivedCorrect: " + hsbString(derivedCorrect));
        if (Math.abs(hueDerived - hueCorrect) < 0.001f) {
            System.out.println("[PASS]");
        } else {
            throw new RuntimeException("Nimbus derived hue color is not correct");
        }
    }

    private static float[] hsb(Color c) {
        return Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
    }

    private static String hsbString(Color c) {
        float[] hsb = hsb(c);
        return String.format("H=%.2f, S=%.2f, B=%.2f", hsb[0], hsb[1], hsb[2]);
    }
}
