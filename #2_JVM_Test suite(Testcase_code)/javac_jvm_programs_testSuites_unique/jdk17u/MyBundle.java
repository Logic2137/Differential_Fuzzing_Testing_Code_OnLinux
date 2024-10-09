import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyBundle extends ResourceBundle {

    Map<String, String> mapping = new ConcurrentHashMap<>();

    protected Object handleGetObject(String key) {
        return mapping.computeIfAbsent(key, (k) -> k + "-" + System.identityHashCode(this.getClass().getClassLoader()));
    }

    public Enumeration<String> getKeys() {
        return Collections.enumeration(mapping.keySet());
    }
}
