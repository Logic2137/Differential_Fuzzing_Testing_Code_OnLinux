

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import javax.accessibility.AccessibilityProvider;

public final class BarProvider extends AccessibilityProvider {
    private final String name = "BarProvider";

    public String getName() {
        return name;
    }

    public void activate() {
        
        try (PrintWriter writer = new PrintWriter("BarProvider.txt")) {
            writer.println(" BarProvider-activated");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
