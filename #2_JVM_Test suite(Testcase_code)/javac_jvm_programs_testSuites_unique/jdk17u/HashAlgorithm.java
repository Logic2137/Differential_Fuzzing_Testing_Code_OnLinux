public enum HashAlgorithm {

    SHA1("SHA-1"), SHA256("SHA-256"), SHA384("SHA-384"), SHA512("SHA-512");

    public final String name;

    private HashAlgorithm(String name) {
        this.name = name;
    }
}
