



import java.util.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.*;
import java.net.*;
import javax.net.*;
import javax.net.ssl.*;

public class CipherSuites {
    public static final String[] STANDARD = {
        "SSL_RSA_WITH_RC4_128_MD5",
        "SSL_RSA_WITH_RC4_128_SHA",
        "SSL_CK_RC4_128_WITH_MD5",
        "SSL_CK_RC4_128_EXPORT40_WITH_MD5",
        "SSL_CK_RC2_128_CBC_WITH_MD5",
        "SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5",
        "SSL_CK_IDEA_128_CBC_WITH_MD5",
        "SSL_CK_DES_64_CBC_WITH_MD5",
        "SSL_CK_DES_192_EDE3_CBC_WITH_MD5"
    };
    public static final String[] CUSTOM = {
        "SSL_RSA_WITH_RC4_128_MD5",
        "SSL_RSA_WITH_RC4_128_SHA",
        "TLS_RSA_WITH_RC4_1024_SHA",    
        "SSL_CK_RC4_128_WITH_MD5",
        "SSL_CK_RC4_128_EXPORT40_WITH_MD5",
        "SSL_CK_RC2_128_CBC_WITH_MD5",
        "SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5",
        "SSL_CK_IDEA_128_CBC_WITH_MD5",
        "SSL_CK_DES_64_CBC_WITH_MD5",
        "SSL_CK_DES_192_EDE3_CBC_WITH_MD5"
    };
}
