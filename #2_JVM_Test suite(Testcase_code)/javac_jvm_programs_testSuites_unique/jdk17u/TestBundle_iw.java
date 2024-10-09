import java.util.ListResourceBundle;
import java.awt.ComponentOrientation;

public class TestBundle_iw extends ListResourceBundle {

    protected Object[][] getContents() {
        return new Object[][] { { "Orientation", ComponentOrientation.RIGHT_TO_LEFT } };
    }
}
