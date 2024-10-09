

import java.awt.Toolkit;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;


public final class InitializationHang {

    public static void main(final String[] argv) {
        try {
            MidiSystem.getReceiver();
            Toolkit.getDefaultToolkit();
        } catch (final MidiUnavailableException ignored) {
            
        }
    }
}

