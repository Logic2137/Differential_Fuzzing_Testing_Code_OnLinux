





import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

public class EntrySetIteratorRemoveInvalidatesEntry {
    static enum TestEnum { e00, e01, e02 }

    public static void main(String[] args) throws Exception {
        final EnumMap<TestEnum, String> enumMap = new EnumMap<>(TestEnum.class);

        for (TestEnum e : TestEnum.values()) {
            enumMap.put(e, e.name());
        }

        Iterator<Map.Entry<TestEnum, String>> entrySetIterator =
            enumMap.entrySet().iterator();
        Map.Entry<TestEnum, String> entry = entrySetIterator.next();

        entrySetIterator.remove();

        try {
            entry.getKey();
            throw new RuntimeException("Test FAILED: Entry not invalidated by removal.");
        } catch (Exception e) { }
    }
}
