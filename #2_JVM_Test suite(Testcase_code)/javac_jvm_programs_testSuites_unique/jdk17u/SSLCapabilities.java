import java.util.List;
import javax.net.ssl.SNIServerName;

public abstract class SSLCapabilities {

    public abstract String getRecordVersion();

    public abstract String getHelloVersion();

    public abstract List<SNIServerName> getServerNames();
}
