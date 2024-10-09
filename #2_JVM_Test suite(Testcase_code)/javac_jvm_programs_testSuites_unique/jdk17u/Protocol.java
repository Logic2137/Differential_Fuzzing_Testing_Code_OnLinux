public enum Protocol {

    SSLV2HELLO(0x0002, "SSLv2Hello"),
    SSLV3(0x0300, "SSLv3"),
    TLSV1(0x0301, "TLSv1"),
    TLSV1_1(0x0302, "TLSv1.1"),
    TLSV1_2(0x0303, "TLSv1.2"),
    TLSV1_3(0x0304, "TLSv1.3"),
    DTLS1_3(0xFEFC, "DTLSv1.3"),
    DTLS1_2(0xFEFD, "DTLSv1.2"),
    DTLS1_0(0xFEFF, "DTLSv1.0");

    public final int id;

    public final String name;

    private Protocol(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static Protocol protocol(String name) {
        for (Protocol protocol : values()) {
            if (protocol.name.equals(name)) {
                return protocol;
            }
        }
        return null;
    }
}
