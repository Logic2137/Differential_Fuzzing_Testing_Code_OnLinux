

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;


public class ClipDrain {
    static int successfulTests = 0;
    static AudioFormat format = new AudioFormat(8000, 16, 1, true, false);
    
    static byte[] soundData = new byte[(int) (format.getFrameRate() * format.getFrameSize() * 10)];

    static int TOLERANCE_MS = 2500; 

    private static void doMixerClip(Mixer mixer) throws Exception {
        boolean waitedEnough=false;
        try {
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) mixer.getLine(info);
            clip.open(format, soundData, 0, soundData.length);

            
            if (clip.getMicrosecondLength()/1000 < 9900) {
                throw new Exception("clip's microsecond length should be at least 9900000, but it is "+clip.getMicrosecondLength());
            }
            long start = System.currentTimeMillis();

            System.out.println(" ---------- start --------");
            clip.start();
            
            Thread.sleep(300);
            System.out.println("drain ... ");
            clip.drain();
            long elapsedTime = System.currentTimeMillis() - start;
            System.out.println("close ... ");
            clip.close();
            System.out.println("... done");
            System.out.println("Playback duration: "+elapsedTime+" milliseconds.");
            waitedEnough = elapsedTime >= ((clip.getMicrosecondLength() / 1000) - TOLERANCE_MS);
        } catch (Throwable t) {
                System.out.println("  - Caught exception. Not failed.");
                System.out.println("  - "+t.toString());
                return;
        }
        if (!waitedEnough) {
            throw new Exception("Drain did not wait long enough to play entire clip.");
        }
        successfulTests++;
    }


    private static void doAll() throws Exception {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (int i=0; i<mixers.length; i++) {
            Mixer mixer = AudioSystem.getMixer(mixers[i]);
            System.out.println("--------------");
            System.out.println("Testing mixer: "+mixers[i]);
            doMixerClip(mixer);
        }
        if (mixers.length==0) {
            System.out.println("No mixers available!");
        }
    }

    public static void main(String[] args) throws Exception {
        if (!isSoundcardInstalled()) {
            return;
        }
        doAll();
        if (successfulTests==0) {
            System.out.println("Could not execute any of the tests. Test NOT failed.");
        } else {
            System.out.println("Test PASSED.");
        }
    }

    
    public static boolean isSoundcardInstalled() {
        boolean result = false;
        try {
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            if (mixers.length > 0) {
                result = AudioSystem.getSourceDataLine(null) != null;
            }
        } catch (Exception e) {
            System.err.println("Exception occured: "+e);
        }
        if (!result) {
            System.err.println("Soundcard does not exist or sound drivers not installed!");
            System.err.println("This test requires sound drivers for execution.");
        }
        return result;
    }
}
