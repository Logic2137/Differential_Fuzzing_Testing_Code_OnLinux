

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;


public class BufferSizeCheck {

    public static void main(String[] args) throws Exception {
        boolean realTest = false;
        if (!isSoundcardInstalled()) {
            return;
        }

        try {
            out("4661602: Buffersize is checked when re-opening line");
            AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(info);
            out("Opening with buffersize 12000...");
            sdl.open(format, 12000);
            out("Opening with buffersize 11000...");
            realTest=true;
            sdl.open(format, 11000);
            try {
                sdl.close();
            } catch(Throwable t) {}
        } catch (Exception e) {
            e.printStackTrace();
            
            if (realTest || !(e instanceof LineUnavailableException)) {
                throw e;
            }
        }
        out("Test passed");
    }

    static void out(String s) {
        System.out.println(s); System.out.flush();
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
