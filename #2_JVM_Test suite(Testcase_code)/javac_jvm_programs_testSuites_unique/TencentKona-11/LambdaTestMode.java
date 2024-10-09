
package java.util.stream;

public enum LambdaTestMode {

    NORMAL, SERIALIZATION;

    private static final boolean IS_LAMBDA_SERIALIZATION_MODE = Boolean.getBoolean("org.openjdk.java.util.stream.sand.mode");

    public static LambdaTestMode getMode() {
        return IS_LAMBDA_SERIALIZATION_MODE ? SERIALIZATION : NORMAL;
    }

    public static boolean isNormalMode() {
        return getMode() == NORMAL;
    }
}
