import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

public class bug6411624 {

    public static void main(String[] args) throws Exception {
        log("This test should only be run on solaris or linux system");
        log("without audio card installed (to test on SunRay set");
        log("incorrect $AUDIODEV value).");
        readln();
        boolean testRecv = false;
        boolean testTrans = false;
        boolean testSeq = true;
        try {
            MidiDevice.Info[] midis = MidiSystem.getMidiDeviceInfo();
            log("MidiDevices (total " + midis.length + "):");
            for (int i = 0; i < midis.length; i++) {
                log("" + i + ": " + midis[i].toString());
            }
        } catch (Exception ex) {
            log("!!!EXCEPTION:");
            ex.printStackTrace();
        }
        log("");
        log("getting default receiver...");
        try {
            Receiver rec = MidiSystem.getReceiver();
            log(" - OK: " + rec);
            testRecv = checkDevice(rec);
            rec.close();
        } catch (MidiUnavailableException e) {
            log("MidiUnavailableException has been thrown - OK");
            testRecv = true;
        }
        log("");
        log("getting default transmitter...");
        try {
            Transmitter trans = MidiSystem.getTransmitter();
            log(" - OK: " + trans);
            testTrans = checkDevice(trans);
            trans.close();
        } catch (MidiUnavailableException e) {
            log("MidiUnavailableException has been thrown - OK");
            testTrans = true;
        }
        log("");
        log("getting default synth...");
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            log(" - OK: " + synth);
            synth.close();
        } catch (MidiUnavailableException e) {
            log("MidiUnavailableException has been thrown - OK:");
            e.printStackTrace();
        }
        log("");
        log("getting default sequencer (connected)...");
        try {
            Sequencer seq = MidiSystem.getSequencer();
            log("OK: " + seq);
            log("  receivers:");
            log("    max=" + seq.getMaxReceivers());
            List<Receiver> recvList = seq.getReceivers();
            log("    count=" + recvList.size());
            Iterator<Receiver> recvIter = recvList.iterator();
            int i = 0;
            while (recvIter.hasNext()) {
                Receiver recv = recvIter.next();
                log("    " + (++i) + ": " + recv);
            }
            log("  transmitters:");
            log("    max=" + seq.getMaxTransmitters());
            List<Transmitter> transList = seq.getTransmitters();
            log("    count=" + transList.size());
            Iterator<Transmitter> transIter = transList.iterator();
            i = 0;
            while (transIter.hasNext()) {
                Transmitter trans = transIter.next();
                log("    " + (++i) + ": " + trans);
                Receiver recv = trans.getReceiver();
                log("      recv: " + recv);
                if (!checkDevice(recv))
                    testSeq = false;
            }
            log("opening sequencer...");
            seq.open();
            log("OK.");
            log("closing...");
            seq.close();
            log("OK.");
        } catch (MidiUnavailableException e) {
            log("MidiUnavailableException has been thrown - OK:");
            e.printStackTrace();
        }
        log("");
        log("getting default sequencer (non-connected)...");
        try {
            Sequencer seq = MidiSystem.getSequencer(false);
            log("OK: " + seq);
            log("  receivers:");
            log("    max=" + seq.getMaxReceivers());
            List<Receiver> recvList = seq.getReceivers();
            log("    count=" + recvList.size());
            Iterator<Receiver> recvIter = recvList.iterator();
            int i = 0;
            while (recvIter.hasNext()) {
                Receiver recv = recvIter.next();
                log("    " + (++i) + ": " + recv);
            }
            log("  transmitters:");
            log("    max=" + seq.getMaxTransmitters());
            List<Transmitter> transList = seq.getTransmitters();
            log("    count=" + transList.size());
            Iterator<Transmitter> transIter = transList.iterator();
            i = 0;
            while (transIter.hasNext()) {
                Transmitter trans = transIter.next();
                log("    " + (++i) + ": " + trans);
                Receiver recv = trans.getReceiver();
                log("      recv: " + recv);
            }
            seq.close();
        } catch (MidiUnavailableException e) {
            log("MidiUnavailableException has been thrown (shouln't?):");
            e.printStackTrace();
        }
        log("");
        log("Test result:");
        if (testRecv && testTrans && testSeq) {
            log("  All tests sucessfully passed.");
        } else {
            log("  Some tests failed:");
            log("    receiver test:    " + (testRecv ? "OK" : "FAILED"));
            log("    transmitter test: " + (testTrans ? "OK" : "FAILED"));
            log("    sequencer test:   " + (testSeq ? "OK" : "FAILED"));
        }
        log("\n\n\n");
    }

    static boolean checkDevice(Object dev) {
        String className = dev.getClass().toString().toLowerCase();
        boolean result = (className.indexOf("sequencer") < 0);
        if (!result)
            log("ERROR: inapropriate device");
        return result;
    }

    static long startTime = currentTimeMillis();

    static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    static void log(String s) {
        long time = currentTimeMillis() - startTime;
        long ms = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long min = time % 60;
        time /= 60;
        System.out.println("" + (time < 10 ? "0" : "") + time + ":" + (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec + "." + (ms < 10 ? "00" : (ms < 100 ? "0" : "")) + ms + " (" + Thread.currentThread().getName() + ") " + s);
    }

    static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    static void readln() {
        log("");
        log("Press ENTER to continue...");
        try {
            while (System.in.read() != 10) ;
        } catch (IOException e) {
        }
        log("");
    }
}
