

import javax.sound.sampled.AudioFileFormat;


public final class TypeEqualsToNull {

    public static void main(final String[] args) {
        final AudioFileFormat.Type type;
        try {
            type = new AudioFileFormat.Type(null, null);
        } catch (final Exception ignored) {
            
            return;
        }
        final Object stub = new Object() {
            @Override
            public String toString() {
                return null;
            }
        };
        if (stub.equals(type) || type.equals(stub)) {
            throw new RuntimeException("Should not be equal");
        }
    }
}
