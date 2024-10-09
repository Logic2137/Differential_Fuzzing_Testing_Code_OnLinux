import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class MultipleInferenceHooksTest {

    public static void main(String[] args) {
        Set<String> result = Collections.newSetFromMap(new IdentityHashMap<>() {
        });
    }
}
