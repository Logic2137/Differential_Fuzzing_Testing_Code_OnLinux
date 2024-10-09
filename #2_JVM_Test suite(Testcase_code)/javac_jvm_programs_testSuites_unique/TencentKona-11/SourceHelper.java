
package jdk.nashorn.test.models;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import jdk.nashorn.internal.runtime.Source;

@SuppressWarnings("javadoc")
public final class SourceHelper {

    private SourceHelper() {
    }

    public static String baseURL(final URL url) {
        return Source.baseURL(url);
    }

    public static String readFully(final File file) throws IOException {
        return new String(Source.readFully(file));
    }

    public static String readFully(final URL url) throws IOException {
        return Source.sourceFor(url.toString(), url).getString();
    }

    public static String readFully(final Reader reader) throws IOException {
        return new String(Source.readFully(reader));
    }
}
