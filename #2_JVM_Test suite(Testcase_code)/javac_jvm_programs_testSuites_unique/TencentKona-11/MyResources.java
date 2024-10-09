

package jdk.test.resources;

import java.util.ListResourceBundle;

public class MyResources extends ListResourceBundle {
    @Override
    public Object[][] getContents() {
        return new Object[][] {
            { "key", "root: message" }
        };
    }
}
