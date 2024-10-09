

package jdk.test.internal.resources;

import java.util.ListResourceBundle;

public class Foo_fr extends ListResourceBundle {
    @Override
    public Object[][] getContents() {
        return new Object[][] {
            { "key", "fr: message" }
        };
    }
}
