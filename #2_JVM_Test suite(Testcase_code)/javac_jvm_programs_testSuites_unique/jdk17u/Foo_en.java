
package jdk.test.internal.resources;

import java.util.ListResourceBundle;

public class Foo_en extends ListResourceBundle {

    @Override
    public Object[][] getContents() {
        return new Object[][] { { "key", "en: message" } };
    }
}
