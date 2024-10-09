

package java.lang;

import java.util.ServiceLoader;

public interface TestProvider {
    public static Iterable<TestProvider> providers() {
        return ServiceLoader.load(TestProvider.class);
    }
}
