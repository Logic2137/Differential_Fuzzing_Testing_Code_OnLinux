import com.sun.jarsigner.ContentSignerParameters;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.zip.ZipFile;

public class DefaultMethod implements ContentSignerParameters {

    @Override
    public String[] getCommandLine() {
        return new String[0];
    }

    @Override
    public URI getTimestampingAuthority() {
        return null;
    }

    @Override
    public X509Certificate getTimestampingAuthorityCertificate() {
        return null;
    }

    @Override
    public byte[] getSignature() {
        return new byte[0];
    }

    @Override
    public String getSignatureAlgorithm() {
        return null;
    }

    @Override
    public X509Certificate[] getSignerCertificateChain() {
        return new X509Certificate[0];
    }

    @Override
    public byte[] getContent() {
        return new byte[0];
    }

    @Override
    public ZipFile getSource() {
        return null;
    }
}
