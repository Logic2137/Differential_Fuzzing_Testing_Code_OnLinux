import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

public class RubyTheme extends DefaultMetalTheme {

    public static String NAME = "Ruby";

    public String getName() {
        return NAME;
    }

    private final ColorUIResource primary1 = new ColorUIResource(80, 10, 22);

    private final ColorUIResource primary2 = new ColorUIResource(193, 10, 44);

    private final ColorUIResource primary3 = new ColorUIResource(244, 10, 66);

    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    protected ColorUIResource getPrimary3() {
        return primary3;
    }
}
