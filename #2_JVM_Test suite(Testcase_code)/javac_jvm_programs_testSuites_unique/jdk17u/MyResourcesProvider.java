
package jdk.test.resources.spi;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.Set;
import java.util.spi.AbstractResourceBundleProvider;

public class MyResourcesProvider extends AbstractResourceBundleProvider {

    private final String region;

    private final Set<Locale> supportedLocales;

    private final List<String> formats;

    protected MyResourcesProvider() {
        region = "";
        supportedLocales = null;
        formats = Collections.emptyList();
    }

    protected MyResourcesProvider(String format, String region, Locale... locales) {
        super(format);
        this.region = region;
        this.supportedLocales = new HashSet<>(Arrays.asList(locales));
        this.formats = Collections.singletonList(format);
    }

    @Override
    public ResourceBundle getBundle(String baseName, Locale locale) {
        if (isSupportedInModule(locale)) {
            return super.getBundle(baseName, locale);
        }
        return null;
    }

    @Override
    protected String toBundleName(String baseName, Locale locale) {
        String name = addRegion(baseName);
        return Control.getControl(Control.FORMAT_DEFAULT).toBundleName(name, locale);
    }

    private String addRegion(String baseName) {
        if (region.isEmpty()) {
            return baseName;
        }
        int index = baseName.lastIndexOf('.');
        return baseName.substring(0, index + 1) + region + baseName.substring(index);
    }

    protected boolean isSupportedInModule(Locale locale) {
        return supportedLocales.contains(locale);
    }
}
