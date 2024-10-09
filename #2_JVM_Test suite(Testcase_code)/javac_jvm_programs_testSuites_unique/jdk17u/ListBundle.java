
package resources;

import java.util.ListResourceBundle;

public class ListBundle extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] { { "dummy", "foo" } };
    }
}
