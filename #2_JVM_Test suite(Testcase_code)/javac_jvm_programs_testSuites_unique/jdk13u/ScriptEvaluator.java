
package jdk.nashorn.internal.test.framework;

import java.io.IOException;
import java.io.OutputStream;

public interface ScriptEvaluator {

    public int run(OutputStream out, OutputStream err, String[] args) throws IOException;
}
