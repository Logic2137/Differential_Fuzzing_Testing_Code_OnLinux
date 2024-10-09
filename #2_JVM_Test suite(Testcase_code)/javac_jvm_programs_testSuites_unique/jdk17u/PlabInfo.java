
package gc.g1.plab.lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlabInfo {

    private final Map<String, Long> plabInfo;

    public PlabInfo() {
        plabInfo = new HashMap<>();
    }

    private PlabInfo(Map<String, Long> map) {
        plabInfo = new HashMap<>(map);
    }

    public void put(String key, long value) {
        plabInfo.put(key, value);
    }

    public Stream<Map.Entry<String, Long>> entryStream() {
        return plabInfo.entrySet().stream();
    }

    public PlabInfo filter(List<String> fields) {
        return new PlabInfo(entryStream().filter(field -> fields.contains(field.getKey())).collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue())));
    }

    public boolean checkFields(List<String> fields) {
        for (String key : fields) {
            if (!plabInfo.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public Collection<Long> values() {
        return plabInfo.values();
    }

    public long get(String field) {
        return plabInfo.get(field);
    }
}
