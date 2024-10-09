

package q;

import java.io.IOException;
import java.io.OutputStream;
import sun.security.util.DerEncoder;

public class NoRepl implements DerEncoder {
    public void derEncode(OutputStream out) throws IOException {
        throw new IOException();
    }
}
