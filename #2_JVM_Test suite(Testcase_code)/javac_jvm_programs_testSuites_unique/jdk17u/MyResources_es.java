
package jdk.test.resources.eu;

import java.util.ListResourceBundle;

public class MyResources_es extends ListResourceBundle {

    @Override
    public Object[][] getContents() {
        return new Object[][] { { "key", "es: message" } };
    }
}
