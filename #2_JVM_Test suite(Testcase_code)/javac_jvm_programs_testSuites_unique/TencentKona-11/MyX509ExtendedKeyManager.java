

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedKeyManager;

public class MyX509ExtendedKeyManager extends X509ExtendedKeyManager {

    static final String ERROR = "ERROR";
    X509ExtendedKeyManager akm;
    String expectedAP;
    boolean doCheck = true;

    MyX509ExtendedKeyManager(X509ExtendedKeyManager akm) {
        this.akm = akm;
    }

    public MyX509ExtendedKeyManager(
            X509ExtendedKeyManager akm, String expectedAP, boolean doCheck) {
        this.akm = akm;
        this.expectedAP = expectedAP;
        this.doCheck = doCheck;

    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return akm.getClientAliases(keyType, issuers);
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers,
            Socket socket) {
        String nap = ((SSLSocket) socket).getHandshakeApplicationProtocol();
        checkALPN(nap);

        return akm.chooseClientAlias(keyType, issuers, socket);
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return akm.getServerAliases(keyType, issuers);
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers,
            Socket socket) {
        String nap = ((SSLSocket) socket).getHandshakeApplicationProtocol();
        checkALPN(nap);

        return akm.chooseServerAlias(keyType, issuers, socket);
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        return akm.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        return akm.getPrivateKey(alias);
    }

    @Override
    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers,
            SSLEngine engine) {
        String nap = engine.getHandshakeApplicationProtocol();
        checkALPN(nap);

        return akm.chooseEngineClientAlias(keyType, issuers, engine);
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers,
            SSLEngine engine) {
        String nap = engine.getHandshakeApplicationProtocol();
        checkALPN(nap);

        return akm.chooseEngineServerAlias(keyType, issuers, engine);
    }

    private void checkALPN(String ap) {

        if (!doCheck) {
            System.out.println("Skipping KeyManager checks " +
                "because a callback has been registered");
            return;
        }

        if (ERROR.equals(expectedAP)) {
            throw new RuntimeException("Should not reach here");
        }

        System.out.println("Expected ALPN value: " + expectedAP
                + " Got: " + ap);

        if (ap == null) {
            throw new RuntimeException(
                    "ALPN should be negotiated, but null was received");
        }
        if (expectedAP.equals("NONE")) {
            if (!ap.isEmpty()) {
                throw new RuntimeException("Expected no ALPN value");
            } else {
                System.out.println("No ALPN value negotiated, as expected");
            }
        } else if (!expectedAP.equals(ap)) {
            throw new RuntimeException(expectedAP
                    + " ALPN value not available on negotiated connection");
        }

    }
}
