public enum KeyAlgorithm {

    DSA("DSA"), RSA("RSA"), EC("EC"), RSASSAPSS("RSASSA-PSS");

    public final String name;

    private KeyAlgorithm(String name) {
        this.name = name;
    }
}
