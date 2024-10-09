public enum JdkRelease {

    JDK6(6, "1.6"),
    JDK7(7, "1.7"),
    JDK8(8, "1.8"),
    JDK9(9, "9"),
    JDK10(10, "10"),
    JDK11(11, "11");

    public final int sequence;

    public final String release;

    private JdkRelease(int sequence, String release) {
        this.sequence = sequence;
        this.release = release;
    }

    public static JdkRelease getRelease(String jdkVersion) {
        if (jdkVersion.startsWith(JDK6.release)) {
            return JDK6;
        } else if (jdkVersion.startsWith(JDK7.release)) {
            return JDK7;
        } else if (jdkVersion.startsWith(JDK8.release)) {
            return JDK8;
        } else if (jdkVersion.startsWith(JDK9.release)) {
            return JDK9;
        } else if (jdkVersion.startsWith(JDK10.release)) {
            return JDK10;
        } else if (jdkVersion.startsWith(JDK11.release)) {
            return JDK11;
        }
        return null;
    }
}
