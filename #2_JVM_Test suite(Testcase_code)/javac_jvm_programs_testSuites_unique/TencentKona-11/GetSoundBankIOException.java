

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;


public class GetSoundBankIOException {

    public static void main(String args[]) throws Exception {
        boolean failed = false;
        try {
            String filename = "GetSoundBankIOException.java";
            System.out.println("Opening "+filename+" as soundbank...");
            File midiFile = new File(System.getProperty("test.src", "."), filename);
            MidiSystem.getSoundbank(midiFile);
            
            System.err.println("InvalidMidiDataException was not thrown!");
            failed = true;
        } catch (InvalidMidiDataException invMidiEx) {
            System.err.println("InvalidMidiDataException was thrown. OK.");
        } catch (IOException ioEx) {
            System.err.println("Unexpected IOException was caught!");
            System.err.println(ioEx.getMessage());
            ioEx.printStackTrace();
            failed = true;
        }

        if (failed) throw new Exception("Test FAILED!");
        System.out.println("Test passed.");
    }

    private static class NonMarkableIS extends InputStream {
        int counter = 0;

        public NonMarkableIS() {
        }

        public int read() throws IOException {
            if (counter > 1000) return -1;
            return (++counter) % 256;
        }

        public synchronized void mark(int readlimit) {
            System.out.println("Called mark with readlimit= "+readlimit);
        }

        public synchronized void reset() throws IOException {
            throw new IOException("mark/reset not supported");
        }

        public boolean markSupported() {
            return false;
        }

    }
}
