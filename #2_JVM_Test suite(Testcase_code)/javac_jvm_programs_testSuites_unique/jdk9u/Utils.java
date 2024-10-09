

package jdk.test.failurehandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public final class Utils {
    private static final int BUFFER_LENGTH = 1024;

    public static String prependPrefix(String prefix, String name) {
        return  (prefix == null || prefix.isEmpty())
                ? name
                : (name == null || name.isEmpty())
                  ? prefix
                  : String.format("%s.%s", prefix, name);
    }

    public static void copyStream(InputStream in, OutputStream out)
            throws IOException {
        int n;
        byte[] buffer = new byte[BUFFER_LENGTH];
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        out.flush();
    }

    public static void copyStream(Reader in, Writer out)
            throws IOException {
        int n;
        char[] buffer = new char[BUFFER_LENGTH];
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        out.flush();
    }

    public static Properties getProperties(String name) {
        Properties properties = new Properties();
        String resourceName = String.format(
                "/%s.%s", name.toLowerCase(), "properties");
        InputStream stream = Utils.class.getResourceAsStream(resourceName);
        if (stream == null) {
            throw new IllegalStateException(String.format(
                    "resource '%s' doesn't exist%n", resourceName));
        }
        try {
            try {
                properties.load(stream);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(String.format(
                    "can't read resource '%s' : %s%n",
                    resourceName, e.getMessage()), e);
        }
        return properties;
    }

    private Utils() { }
}
