
package jar1;

import java.io.InputStream;

public class GetResource {

    public GetResource() throws Exception {
        InputStream in;
        in = getClass().getResourceAsStream("/res1.txt");
        in.available();
    }
}
