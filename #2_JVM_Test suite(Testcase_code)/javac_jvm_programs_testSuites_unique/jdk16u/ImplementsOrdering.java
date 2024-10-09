

package pkg1;

import java.io.IOException;
import java.nio.channels.Channel;

public interface ImplementsOrdering extends Channel {
    
    @Override
    void close() throws IOException;
}
