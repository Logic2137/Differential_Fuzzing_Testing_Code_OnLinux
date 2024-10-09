import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.spi.SoundbankReader;
import static java.util.ServiceLoader.load;

public final class ExpectedNPEOnNull {

    public static void main(final String[] args) throws Exception {
        testMS();
        for (final SoundbankReader sbr : load(SoundbankReader.class)) {
            testSBR(sbr);
        }
    }

    private static void testMS() throws Exception {
        try {
            MidiSystem.getSoundbank((InputStream) null);
            throw new RuntimeException("NPE is expected");
        } catch (final NullPointerException ignored) {
        }
        try {
            MidiSystem.getSoundbank((URL) null);
            throw new RuntimeException("NPE is expected");
        } catch (final NullPointerException ignored) {
        }
        try {
            MidiSystem.getSoundbank((File) null);
            throw new RuntimeException("NPE is expected");
        } catch (final NullPointerException ignored) {
        }
    }

    private static void testSBR(final SoundbankReader sbr) throws Exception {
        try {
            sbr.getSoundbank((InputStream) null);
            throw new RuntimeException("NPE is expected");
        } catch (final NullPointerException ignored) {
        }
        try {
            sbr.getSoundbank((URL) null);
            throw new RuntimeException("NPE is expected");
        } catch (final NullPointerException ignored) {
        }
        try {
            sbr.getSoundbank((File) null);
            throw new RuntimeException("NPE is expected");
        } catch (final NullPointerException ignored) {
        }
    }
}
