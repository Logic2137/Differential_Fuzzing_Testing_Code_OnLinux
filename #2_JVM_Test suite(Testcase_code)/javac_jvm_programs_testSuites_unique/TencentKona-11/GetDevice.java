import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import com.sun.media.sound.*;

public class GetDevice {

    private static void assertEquals(Object a, Object b) throws Exception {
        if (!a.equals(b))
            throw new RuntimeException("assertEquals fails!");
    }

    private static void assertTrue(boolean value) throws Exception {
        if (!value)
            throw new RuntimeException("assertTrue fails!");
    }

    public static void main(String[] args) throws Exception {
        SoftProvider provider = new SoftProvider();
        Info[] infos = provider.getDeviceInfo();
        assertTrue(infos.length > 0);
        for (int i = 0; i < infos.length; i++) {
            assertTrue(infos[i] != null);
            MidiDevice d = provider.getDevice(infos[i]);
            assertTrue(d instanceof SoftSynthesizer);
        }
    }
}
