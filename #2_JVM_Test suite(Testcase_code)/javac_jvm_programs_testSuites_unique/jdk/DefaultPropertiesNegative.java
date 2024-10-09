

import java.nio.file.Paths;

import com.sun.media.sound.JDK13Services;


public class DefaultPropertiesNegative {

    private static final Class[] lineTypeClasses = {
        javax.sound.midi.Receiver.class,
        javax.sound.midi.Transmitter.class,
        javax.sound.midi.Sequencer.class,
        javax.sound.midi.Synthesizer.class,
    };

    public static void main(String[] args) throws Exception {
        boolean allOk = true;
        String path = Paths.get(System.getProperty("test.src", "."),
                                "testdata", "conf", "sound.properties")
                           .toAbsolutePath().normalize().toString();
        System.setProperty("javax.sound.config.file", path);

        for (int i = 0; i < lineTypeClasses.length; i++) {
            Class cls = lineTypeClasses[i];
            
            String result = JDK13Services.getDefaultProviderClassName(cls);
            if (result != null) {
                out("type " + cls + " failed: provider class should be 'null' "
                            + "but is '" + result + "'!");
                allOk = false;
            }
            result = JDK13Services.getDefaultInstanceName(cls);
            if (result != null) {
                out("type " + cls + " failed: instance name should be 'null' "
                            + "but is '" + result + "'!");
                allOk = false;
            }
        }
        if (! allOk) {
            throw new Exception("Test failed");
        } else {
            out("Test passed");
        }
    }

    private static void out(String message) {
        System.out.println(message);
    }
}

