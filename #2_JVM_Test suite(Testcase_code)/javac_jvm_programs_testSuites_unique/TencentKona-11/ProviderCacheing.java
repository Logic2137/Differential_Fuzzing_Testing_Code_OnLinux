

import java.util.List;

import com.sun.media.sound.JDK13Services;


public class ProviderCacheing {

    private static final Class[] providerClasses = {
        javax.sound.midi.spi.MidiDeviceProvider.class,
        javax.sound.midi.spi.MidiFileReader.class,
        javax.sound.midi.spi.MidiFileWriter.class,
        javax.sound.midi.spi.SoundbankReader.class,
    };

    public static void main(String[] args) throws Exception {
        boolean allCached = true;
        for (int i = 0; i < providerClasses.length; i++) {
            List list0 = JDK13Services.getProviders(providerClasses[i]);
            List list1 = JDK13Services.getProviders(providerClasses[i]);
            if (list0 == list1) {
                out("Providers should not be cached for " + providerClasses[i]);
                allCached = false;
            }
        }

        if (! allCached) {
            throw new Exception("Test failed");
        } else {
            out("Test passed");
        }
    }

    private static void out(String message) {
        System.out.println(message);
    }
}
