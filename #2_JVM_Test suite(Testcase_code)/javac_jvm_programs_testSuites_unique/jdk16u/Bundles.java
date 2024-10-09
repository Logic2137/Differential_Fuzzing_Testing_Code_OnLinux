

package jdk.test.util;

import java.util.ResourceBundle;

public class Bundles {
    static final String MAIN_BUNDLES_RESOURCE = "jdk.test.resources.MyResources";

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(MAIN_BUNDLES_RESOURCE);
    }
}
