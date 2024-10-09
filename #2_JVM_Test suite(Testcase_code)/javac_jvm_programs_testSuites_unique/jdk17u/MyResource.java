
package testlogger;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class MyResource extends ResourceBundle {

    Map<String, Object> bundle = new HashMap<>();

    @Override
    protected Object handleGetObject(String key) {
        bundle.put(key, "Localized: " + key);
        return bundle.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        final Hashtable<String, Object> h = new Hashtable<>(bundle);
        return h.keys();
    }
}
