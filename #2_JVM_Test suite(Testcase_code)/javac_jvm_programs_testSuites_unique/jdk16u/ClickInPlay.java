

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;


public class ClickInPlay {

    static int sampleRate = 8000;
    static double frequency = 2000.0;
    static double RAD = 2.0 * Math.PI;

    static byte[] audioData = new byte[sampleRate/2];
    static DataLine.Info info;
    static Clip source;

    
    static AudioFormat audioFormat;
    

    public static void print(String s) {
        System.out.print(s);
    }
    public static void println(String s) {
        System.out.println(s);
    }

    public static void key() {
        println("");
        print("Press ENTER to continue...");
        try {
            System.in.read();
        } catch (IOException ioe) {
        }
    }

    public static void play(Mixer mixer) {
        int res = 0;
        try {
            println("Getting clip from mixer...");
            source = (Clip) mixer.getLine(info);
            println("Opening clip...");
            source.open(audioFormat, audioData, 0, audioData.length);
            println("Starting clip...");
            source.loop(Clip.LOOP_CONTINUOUSLY);
            println("Now open your ears:");
            println("- if you hear a sine wave playing,");
            println("  listen carefully if you can hear clicks.");
            println("  If no, the bug is fixed.");
            println("- if you don't hear anything, it's possible");
            println("  that this mixer is not connected to an ");
            println("  amplifier, or that its volume is set to 0");
            key();
        } catch (IllegalArgumentException iae) {
            println("IllegalArgumentException: "+iae.getMessage());
            println("Sound device cannot handle this audio format.");
            println("ERROR: Test environment not correctly set up.");
            if (source!=null) {
                source.close();
                source = null;
            }
            return;
        } catch (LineUnavailableException lue) {
            println("LineUnavailableException: "+lue.getMessage());
            println("This is normal for some mixers.");
        } catch (Exception e) {
            println("Unexpected Exception: "+e.toString());
        }
        if (source != null) {
            println("Stopping...");
            source.stop();
            println("Closing...");
            source.close();
            println("Closed.");
            source = null;
        }
    }

    public static void main(String[] args) throws Exception {
        println("This is an interactive test.");
        println("If you can hear clicks during playback in");
        println("any mixer, the test is failed.");
        println("");
        println("Make sure that you have speakers connected");
        println("and that the system mixer is not muted.");
        println("");
        println("Press a key to start the test.");
        key();
        Mixer.Info[] mixers=null;

            println("   ...using self-generated sine wave for playback");
            audioFormat = new AudioFormat((float)sampleRate, 8, 1, true, true);
            for (int i=0; i<audioData.length; i++) {
                audioData[i] = (byte)(Math.sin(RAD*frequency/sampleRate*i)*127.0);
            }
        info = new DataLine.Info(Clip.class, audioFormat);

        mixers = AudioSystem.getMixerInfo();
        int succMixers = 0;
        for (int i=0; i<mixers.length; i++) {
            try {
                Mixer mixer = AudioSystem.getMixer(mixers[i]);
                if (mixer.isLineSupported(info)) {
                    succMixers++;
                    println("  ...using mixer "+mixer.getMixerInfo());
                    play(mixer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (succMixers == 0) {
                println("No mixers available! ");
                println("Cannot run test.");
        }
    }

}
