import java.io.PrintStream;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class AsynchronousMidiChannel {

    static PrintStream log = System.err;

    static PrintStream ref = System.out;

    public static void main(String[] args) {
        doIt(args);
    }

    public static void doIt(String[] args) {
        Synthesizer synth = null;
        MidiChannel[] mChanArr;
        MidiChannel chan = null;
        boolean failed = false;
        int i = 0;
        int chanNum = 0;
        int val = 1;
        int contr = 0;
        Soundbank sBank;
        Instrument[] insArr;
        Instrument instr = null;
        Object ev = new Object();
        try {
            synth = MidiSystem.getSynthesizer();
            System.out.println("Got synth: " + synth);
            synth.open();
            int latency = (int) synth.getLatency();
            System.out.println("  -> latency: " + latency + " microseconds");
            mChanArr = synth.getChannels();
            while ((i < mChanArr.length) && (chan == null)) {
                chanNum = i;
                chan = mChanArr[i++];
            }
            if (chan == null) {
                System.out.println("No channels in " + "this synthesizer!");
                return;
            }
            System.out.println("Got MidiChannel: " + chan);
            sBank = synth.getDefaultSoundbank();
            if (sBank == null) {
                System.out.println("No default sound bank!");
                return;
            }
            insArr = sBank.getInstruments();
            for (int j = 0; j < insArr.length; j++) {
                if (insArr[j].getPatch().getBank() == val) {
                    instr = insArr[j];
                    synth.loadInstrument(instr);
                }
            }
            if (instr == null) {
                System.out.println("No instr. with this bank!");
                return;
            }
            chan.controlChange(contr, val);
            if (latency > 0) {
                try {
                    Thread.sleep(latency / 1000);
                } catch (InterruptedException inEx) {
                }
            }
            if (chan.getController(contr) != val) {
                failed = true;
                System.err.println("getController() does not " + "return proper value: " + chan.getController(contr));
            } else {
                System.out.println("getController(" + contr + ") returns proper value: " + chan.getController(contr));
            }
        } catch (MidiUnavailableException mue) {
            System.err.println("MidiUnavailableException was " + "thrown: " + mue);
            System.out.println("could not test.");
            return;
        } catch (SecurityException se) {
            se.printStackTrace();
            System.err.println("Sound access is not denied but " + "SecurityException was thrown!");
            return;
        } finally {
            if (synth != null)
                synth.close();
        }
        if (failed == true) {
            System.out.println("test failed");
        } else {
            System.out.println("OKAY");
        }
        return;
    }
}
