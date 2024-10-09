



import java.util.ListResourceBundle;
import java.awt.ComponentOrientation;

public class TestBundle_es extends ListResourceBundle {

    protected Object[][] getContents() {
        return new Object[][] {
            { "Orientation", ComponentOrientation.LEFT_TO_RIGHT },
        };
    }
};
