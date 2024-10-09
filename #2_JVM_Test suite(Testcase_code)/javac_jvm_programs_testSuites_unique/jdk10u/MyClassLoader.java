
package xpath;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class MyClassLoader extends ClassLoader {
    boolean isCalled = false;

    public URL getResource(String name) {
        isCalled = true;
        return super.getResource(name);
    }

    public Enumeration getResources(String name) throws IOException {
        isCalled = true;
        return super.getResources(name);
    }

    public void reset() {
        isCalled = false;
    }

    public boolean isCalled() {
        return isCalled;
    }
}
