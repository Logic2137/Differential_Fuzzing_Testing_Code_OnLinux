

package jdk.test.resources;

import java.util.ListResourceBundle;

public class TestResources extends ListResourceBundle {
    @Override
    public Object[][] getContents() {
        return new Object[][] {
            { "msg", "test" }
        };
    }
}
