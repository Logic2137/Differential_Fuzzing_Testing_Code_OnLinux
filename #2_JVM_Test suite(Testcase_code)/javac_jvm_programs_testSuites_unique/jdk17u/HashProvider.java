import java.security.*;

public final class HashProvider extends Provider {

    public HashProvider() {
        super("HashProvider", "1.0", "");
        addAlgorithms("SunRsaSign");
        addAlgorithms("SunJSSE");
        addAlgorithms("SUN");
    }

    private void addAlgorithms(String name) {
        Provider p = Security.getProvider(name);
        if (p != null) {
            System.out.println("Adding algorithms from provider " + name + " (" + p.size() + ")");
            putAll(p);
        }
    }
}
