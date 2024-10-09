

import javax.accessibility.AccessibilityProvider;
import java.io.UncheckedIOException;
import java.io.IOException;
import java.io.PrintWriter;

public final class FooProvider extends AccessibilityProvider {

    private final String name = "FooProvider";

    public String getName() {
        return name;
    }

    public void activate() {
        
        try (PrintWriter writer = new PrintWriter("FooProvider.txt")) {
            writer.println("FooProvider-activated");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
