



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

public class SMPTESequence {

    static int failed = 0;

    public static void main(String[] args) {
        test(Sequence.PPQ);
        test(Sequence.SMPTE_24);
        test(Sequence.SMPTE_25);
        test(Sequence.SMPTE_30);
        test(Sequence.SMPTE_30DROP);

        if (failed > 0) {
            throw new RuntimeException("" + failed + " tests failed");
        }
    }

    static boolean test(float divisionType) {
        boolean result = false;
        try {
            log("Testing divisionType == " + divisionType);
            Sequence sequence = new Sequence(divisionType, 16, 1);
            float div1 = sequence.getDivisionType();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            MidiSystem.write(sequence, 1, outStream);

            InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());

            sequence = MidiSystem.getSequence(inStream);
            float div2 = sequence.getDivisionType();

            log("After write/read got divisionType == " + div2);
            if (Math.abs(div2 - div1) < 0.001f) {
                result = true;
            }
        } catch (InvalidMidiDataException ex) {
            log(ex);
        } catch (IOException ex) {
            log(ex);
        } catch (IllegalArgumentException ex) {
            log(ex);
        }
        if (result) {
            log("OK");
        } else {
            log("FAIL");
            failed++;
        }
        return result;
    }

    static void log(String s) {
        System.out.println(s);
    }

    static void log(Exception ex) {
        log("got exception (" + ex.getClass().getSimpleName() + "): " + ex.getMessage());
    }

}
