


public enum NamedGroup {

    SECP256R1("secp256r1"),
    SECP384R1("secp384r1"),
    SECP521R1("secp521r1"),

    X448("x448"),
    X25519("x25519"),

    FFDHE2048("ffdhe2048"),
    FFDHE3072("ffdhe3072"),
    FFDHE4096("ffdhe4096"),
    FFDHE6144("ffdhe6144"),
    FFDHE8192("ffdhe8192");

    public final String name;

    private NamedGroup(String name) {
        this.name = name;
    }
}
