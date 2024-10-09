import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class SynthesizerGetLatency {

    public static void main(String[] args) throws Exception {
        Synthesizer synth = null;
        boolean failed = false;
        boolean notexec = false;
        try {
            synth = MidiSystem.getSynthesizer();
            System.out.println("Got synth: " + synth);
            synth.open();
            int latency = (int) synth.getLatency();
            System.out.println("  -> latency: " + latency + " microseconds");
            if (latency < 5000 && latency > 0) {
                System.out.println("## This latency is VERY small, probably due to this bug.");
                System.out.println("## This causes failure of this test.");
                failed = true;
            }
        } catch (MidiUnavailableException mue) {
            System.err.println("MidiUnavailableException was " + "thrown: " + mue);
            System.out.println("could not test.");
            notexec = true;
        } catch (SecurityException se) {
            se.printStackTrace();
            System.err.println("Sound access is not denied but " + "SecurityException was thrown!");
            notexec = true;
        } finally {
            if (synth != null)
                synth.close();
        }
        if (failed) {
            throw new Exception("Test FAILED!");
        }
        if (notexec) {
            System.out.println("Test not failed.");
        } else {
            System.out.println("Test Passed.");
        }
    }
}
