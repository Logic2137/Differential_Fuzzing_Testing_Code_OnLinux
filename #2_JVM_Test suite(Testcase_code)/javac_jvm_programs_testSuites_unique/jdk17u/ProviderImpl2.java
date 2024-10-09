
package q;

import java.io.*;
import java.util.spi.ToolProvider;

public class ProviderImpl2 implements ToolProvider {

    public String name() {
        return "provider2";
    }

    public int run(PrintWriter out, PrintWriter err, String... args) {
        return 0;
    }
}
