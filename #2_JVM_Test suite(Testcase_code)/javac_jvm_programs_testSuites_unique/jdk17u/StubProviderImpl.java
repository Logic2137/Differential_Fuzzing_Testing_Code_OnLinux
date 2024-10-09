import java.security.cert.CertPathParameters;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;

public class StubProviderImpl extends CertPathBuilderSpi {

    public boolean called;

    public StubProviderImpl() {
        super();
        called = false;
    }

    public CertPathBuilderResult engineBuild(CertPathParameters params) {
        called = true;
        return null;
    }
}
