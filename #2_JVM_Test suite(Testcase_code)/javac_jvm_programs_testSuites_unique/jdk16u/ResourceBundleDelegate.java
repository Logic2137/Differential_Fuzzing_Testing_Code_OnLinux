

package jdk.test;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleDelegate {
    public static ResourceBundle getBundle(String baseName, Locale locale) {
        return ResourceBundle.getBundle(baseName, locale);
    }

    public static ResourceBundle getBundle(String baseName, Locale locale, Module module) {
        return ResourceBundle.getBundle(baseName, locale, module);
    }
}
