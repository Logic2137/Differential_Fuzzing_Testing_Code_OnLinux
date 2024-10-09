

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;


public class NoSimpleInputDevice {

    public static void main(String[] args) throws Exception {
        out("4936397: Verify that there'll be either SimpleInputDevice OR DirectAudioDevice");
        boolean foundSimpleInputDevice = false;
        boolean foundDirectAudioDevice = false;

        Mixer.Info[] aInfos = AudioSystem.getMixerInfo();
        for (int i = 0; i < aInfos.length; i++) {
            try {
                Mixer mixer = AudioSystem.getMixer(aInfos[i]);
                String mixerClass = mixer.getClass().toString();
                if (mixerClass.indexOf("SimpleInputDevice") >= 0) {
                    out("Found SimpleInputDevice: "+aInfos[i]);
                    foundSimpleInputDevice = true;
                }
                if (mixerClass.indexOf("DirectAudioDevice") >= 0) {
                    out("Found DirectAudioDevice: "+aInfos[i]);
                    foundDirectAudioDevice = true;
                }
            } catch (Exception e) {
                out("Unexpected exception: "+e);
            }
        }
        if (aInfos.length == 0) {
            out("[No mixers available] - cannot exercise this test.");
        } else {
            if (foundSimpleInputDevice && foundDirectAudioDevice) {
                out("Found both types of capture devices!");
                throw new Exception("Test FAILED!");
            }
            out("Did not find both types of capture devices. Test passed");
        }
    }

    static void out(String s) {
        System.out.println(s); System.out.flush();
    }
}
