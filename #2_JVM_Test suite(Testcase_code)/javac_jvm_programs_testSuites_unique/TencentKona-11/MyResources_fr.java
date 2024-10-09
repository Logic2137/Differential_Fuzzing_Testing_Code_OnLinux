

package jdk.test.resources.eu;

import java.util.ListResourceBundle;

public class MyResources_fr extends ListResourceBundle {
    @Override
    public Object[][] getContents() {
        return new Object[][] {
            { "key", "fr: message" }
        };
    }
}
