

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.spi.MidiDeviceProvider;

import static java.util.ServiceLoader.load;


public final class UnsupportedInfo {

    public static void main(final String[] args) {
        final MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (final MidiDeviceProvider mdp : load(MidiDeviceProvider.class)) {
            for (final MidiDevice.Info info : infos) {
                if (mdp.isDeviceSupported(info)) {
                    if (mdp.getDevice(info) == null) {
                        throw new RuntimeException("MidiDevice is null");
                    }
                } else {
                    try {
                        mdp.getDevice(info);
                        throw new RuntimeException(
                                "IllegalArgumentException expected");
                    } catch (final IllegalArgumentException ignored) {
                        
                    }
                }
            }
        }
    }
}
