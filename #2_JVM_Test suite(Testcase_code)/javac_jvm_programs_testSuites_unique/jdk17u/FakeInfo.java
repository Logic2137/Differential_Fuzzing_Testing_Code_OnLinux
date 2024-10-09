import java.util.Collection;
import java.util.HashSet;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.spi.MidiDeviceProvider;
import static java.util.ServiceLoader.load;

public final class FakeInfo {

    private static final class Fake extends Info {

        Fake() {
            super("a", "b", "c", "d");
        }
    }

    public static void main(final String[] args) {
        final Info fake = new Fake();
        try {
            MidiSystem.getMidiDevice(fake);
            throw new RuntimeException("IllegalArgumentException expected");
        } catch (final MidiUnavailableException e) {
            throw new RuntimeException("IllegalArgumentException expected", e);
        } catch (final IllegalArgumentException ignored) {
        }
        final Collection<String> errors = new HashSet<>();
        for (final MidiDeviceProvider mdp : load(MidiDeviceProvider.class)) {
            try {
                if (mdp.isDeviceSupported(fake)) {
                    throw new RuntimeException("fake is supported");
                }
                final MidiDevice device = mdp.getDevice(fake);
                System.err.println("MidiDevice: " + device);
                throw new RuntimeException("IllegalArgumentException expected");
            } catch (final IllegalArgumentException e) {
                errors.add(e.getMessage());
            }
        }
        if (errors.size() != 1) {
            throw new RuntimeException("Wrong number of messages:" + errors);
        }
    }
}
