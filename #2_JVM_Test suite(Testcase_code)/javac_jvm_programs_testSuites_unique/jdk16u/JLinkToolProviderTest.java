

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessControlException;
import java.util.spi.ToolProvider;


public class JLinkToolProviderTest {
    static final ToolProvider JLINK_TOOL = ToolProvider.findFirst("jlink")
        .orElseThrow(() ->
            new RuntimeException("jlink tool not found")
        );

    private static void checkJlinkOptions(String... options) {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);

        try {
            JLINK_TOOL.run(pw, pw, options);
            throw new AssertionError("SecurityException should have been thrown!");
        } catch (AccessControlException ace) {
            if (! ace.getPermission().getClass().getName().contains("JlinkPermission")) {
                throw new AssertionError("expected JlinkPermission check failure");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        checkJlinkOptions("--help");
        checkJlinkOptions("--list-plugins");
    }
}
