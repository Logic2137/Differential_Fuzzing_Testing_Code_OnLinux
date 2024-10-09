

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;


public class ChangingBuffer {

    final static int samplerate = 44100;
    final static byte[] buffer = new byte[16384];
    static int successfulTests = 0;

    private static void makeBuffer() {
        for (int i=0; i<buffer.length; i++) {
            buffer[i] = (byte) (i % 128);
        }
    }

    private static void checkBufferSDL() throws Exception {
        successfulTests++;
        for (int i=0; i<buffer.length; i++) {
            if (buffer[i] != ((byte) (i % 128))) {
                throw new Exception("Buffer was changed by SourceDataLine.write()!. Test FAILED");
            }
        }
        System.out.println("  -> passed for this line");
        System.out.println("");
    }

    private static void checkBufferClip() throws Exception {
        for (int i=0; i<buffer.length; i++) {
            if (buffer[i] != (i % 128)) {
                throw new Exception("Buffer was changed by Clip.open()!. Test FAILED");
            }
        }
        System.out.println("  -> passed for this clip");
        System.out.println("");
    }

    private static boolean doMixerClip(Mixer mixer, AudioFormat format) {
        if (mixer==null) return false;
        try {
            System.out.println("Trying mixer "+mixer+":");
                DataLine.Info info = new DataLine.Info(
                                          Clip.class,
                                          format,
                                          (int) samplerate);

                Clip clip = (Clip) mixer.getLine(info);
            System.out.println("  - got clip: "+clip);
            System.out.println("  - open with format "+format);
            clip.open(format, buffer, 0, buffer.length);
            System.out.println("  - playing...");
            clip.start();
            System.out.println("  - waiting while it's active...");
            while (clip.isActive())
                    Thread.sleep(100);
            System.out.println("  - waiting 100millis");
            Thread.sleep(100);
            System.out.println("  - drain1");
            clip.drain();
            System.out.println("  - drain2");
            clip.drain();
            System.out.println("  - stop");
            clip.stop();
            System.out.println("  - close");
            clip.close();
            System.out.println("  - closed");
        } catch (Throwable t) {
            System.out.println("  - Caught exception. Not failed.");
            System.out.println("  - "+t.toString());
            return false;
        }
        return true;
    }

    private static boolean doMixerSDL(Mixer mixer, AudioFormat format) {
        if (mixer==null) return false;
        try {
            System.out.println("Trying mixer "+mixer+":");
                DataLine.Info info = new DataLine.Info(
                                          SourceDataLine.class,
                                          format,
                                          (int) samplerate);

                SourceDataLine sdl = (SourceDataLine) mixer.getLine(info);
            System.out.println("  - got sdl: "+sdl);
            System.out.println("  - open with format "+format);
            sdl.open(format);
            System.out.println("  - start...");
            sdl.start();
            System.out.println("  - write...");
            sdl.write(buffer, 0, buffer.length);
            Thread.sleep(200);
            System.out.println("  - drain...");
            sdl.drain();
            System.out.println("  - stop...");
            sdl.stop();
            System.out.println("  - close...");
            sdl.close();
            System.out.println("  - closed");
        } catch (Throwable t) {
            System.out.println("  - Caught exception. Not failed.");
            System.out.println("  - "+t.toString());
            return false;
        }
        return true;
    }

    private static void doAll(boolean bigEndian) throws Exception {
        AudioFormat pcm = new AudioFormat(
                            AudioFormat.Encoding.PCM_SIGNED,
                            samplerate, 16, 1, 2, samplerate, bigEndian);
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            for (int i=0; i<mixers.length; i++) {
                Mixer mixer = AudioSystem.getMixer(mixers[i]);
                makeBuffer(); if (doMixerClip(mixer, pcm)) checkBufferClip();
                makeBuffer(); if (doMixerSDL(mixer, pcm)) checkBufferSDL();
            }
            if (mixers.length==0) {
                System.out.println("No mixers available!");
            }

    }

    public static void main(String args[]) throws Exception{
        doAll(true);
        doAll(false);
        if (successfulTests==0) {
            System.out.println("Could not execute any of the tests. Test NOT failed.");
        } else {
            System.out.println("Test PASSED.");
        }
    }
}
