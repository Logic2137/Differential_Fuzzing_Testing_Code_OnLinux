import java.util.ListResourceBundle;
import java.awt.ComponentOrientation;

public class TestBundle extends ListResourceBundle {

    protected Object[][] getContents() {
        return new Object[][] { { "Orientation", ComponentOrientation.UNKNOWN } };
    }
}
