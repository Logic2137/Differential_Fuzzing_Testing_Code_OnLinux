

import javax.sound.sampled.AudioFormat;


public final class EncodingEqualsToNull {

    public static void main(final String[] args) {
        final AudioFormat.Encoding enc;
        try {
            enc = new AudioFormat.Encoding(null);
        } catch (final Exception ignored) {
            
            return;
        }
        final Object stub = new Object() {
            @Override
            public String toString() {
                return null;
            }
        };
        if (stub.equals(enc) || enc.equals(stub)) {
            throw new RuntimeException("Should not be equal");
        }
    }
}
