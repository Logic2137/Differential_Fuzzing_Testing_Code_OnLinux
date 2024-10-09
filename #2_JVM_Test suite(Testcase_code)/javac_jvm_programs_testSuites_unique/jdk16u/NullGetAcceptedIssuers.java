



import javax.net.ssl.*;

public class NullGetAcceptedIssuers {

    public static void main(String[] args) throws Exception {
        SSLContext sslContext;

        
        TrustManager[] trustAllCerts =
            new TrustManager[] {new X509TrustManager() {

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs,
                        String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs,
                        String authType) {
                }

                
                
                public java.security.cert.X509Certificate[]
                        getAcceptedIssuers() {
                    return null;
                }
            }};

        sslContext = javax.net.ssl.SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, null);
    }
}
