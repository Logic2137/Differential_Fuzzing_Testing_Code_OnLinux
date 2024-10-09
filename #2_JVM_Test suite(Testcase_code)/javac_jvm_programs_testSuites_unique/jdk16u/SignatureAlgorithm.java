


public enum SignatureAlgorithm {

    RSA("RSA"),
    DSA("DSA"),
    ECDSA("ECDSA"),
    RSASSAPSS("RSASSA-PSS");

    public final String name;

    private SignatureAlgorithm(String name) {
        this.name = name;
    }
}
