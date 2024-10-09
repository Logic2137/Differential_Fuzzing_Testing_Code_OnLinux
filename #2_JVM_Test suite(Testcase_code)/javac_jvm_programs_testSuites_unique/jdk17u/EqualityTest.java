import java.awt.Color;
import java.awt.SystemColor;

public final class EqualityTest {

    private final static SystemColor[] colorArray = { SystemColor.desktop, SystemColor.activeCaption, SystemColor.activeCaptionText, SystemColor.activeCaptionBorder, SystemColor.inactiveCaption, SystemColor.inactiveCaptionText, SystemColor.inactiveCaptionBorder, SystemColor.window, SystemColor.windowBorder, SystemColor.windowText, SystemColor.menu, SystemColor.menuText, SystemColor.text, SystemColor.textText, SystemColor.textHighlight, SystemColor.textHighlightText, SystemColor.textInactiveText, SystemColor.control, SystemColor.controlText, SystemColor.controlHighlight, SystemColor.controlLtHighlight, SystemColor.controlShadow, SystemColor.controlDkShadow, SystemColor.scrollbar, SystemColor.info, SystemColor.infoText };

    public static void main(final String[] str) {
        for (final SystemColor system : colorArray) {
            Color color = new Color(system.getRGB(), system.getAlpha() < 255);
            System.out.printf("System color = %s = [%d]: color = %s [%d]%n", system, system.getRGB(), color, color.getRGB());
            boolean equalityStatement1 = color.equals(system);
            boolean equalityStatement2 = system.equals(color);
            if (!equalityStatement1 || !equalityStatement2) {
                System.out.println("COLOR.equals(SC) = " + equalityStatement1);
                System.out.println("SC.equals(COLOR) = " + equalityStatement2);
                throw new RuntimeException("The equals() method doesn't work correctly");
            }
        }
    }
}
