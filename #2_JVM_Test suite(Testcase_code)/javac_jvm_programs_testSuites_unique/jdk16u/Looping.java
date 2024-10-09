

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class Looping {

    public static void main(String[] args) throws Exception {
        out("4204105: RFE: add loop() method(s) to Sequencer");
        boolean passed = testAll();
        if (passed) {
            out("Test PASSED.");
        } else {
            throw new Exception("Test FAILED.");
        }
    }

    
    private static boolean testAll() throws Exception {
        boolean result = true;
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < devices.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(devices[i]);
            if (device instanceof Sequencer) {
                result &= testSequencer((Sequencer) device);
            }
        }
        return result;
    }

    
    private static boolean testSequencer(Sequencer seq) throws Exception{
        boolean result = true;
        out("testing: " + seq);

        result &= testGetSet(seq);

        seq.setSequence(createSequence());

        result &= testGetSet(seq);

        result &= testPlay(seq);

        return result;
    }

    private static boolean testGetSet(Sequencer seq) {
        boolean result = true;
        Sequence sequence = seq.getSequence();
        boolean isSequenceLoaded = (sequence != null);

        out("TestGetSet");

        try {
            if (seq.getLoopStartPoint() != 0) {
                out("start point", isSequenceLoaded,
                    "isn't 0!");
                result = false;
            }
        } catch (IllegalArgumentException iae) {
            if (!isSequenceLoaded) {
                out("Caught permissable IllegalArgumentException:");
            } else {
                out("Threw unacceptable IllegalArgumentException! FAILED");
                result = false;
            }
            out(iae.toString());
        }

        if (seq.getLoopEndPoint() != -1) {
            out("end point", isSequenceLoaded,
                "isn't -1!");
            result = false;
        }

        try {
            seq.setLoopStartPoint(25);
            if (seq.getLoopStartPoint() != 25) {
                out("setLoopStartPoint()", isSequenceLoaded,
                    "doesn't set the start point correctly!");
                result = false;
            }
        } catch (IllegalArgumentException iae) {
            if (!isSequenceLoaded) {
                out("Caught permissable IllegalArgumentException:");
            } else {
                out("Threw unacceptable IllegalArgumentException! FAILED");
                result = false;
            }
            out(iae.toString());
        }

        try {
            seq.setLoopEndPoint(26);
            if (seq.getLoopEndPoint() != 26) {
                out("setLoopEndPoint()", isSequenceLoaded,
                    "doesn't set the end point correctly!");
                result = false;
            }
        } catch (IllegalArgumentException iae) {
            if (!isSequenceLoaded) {
                out("Caught permissable IllegalArgumentException:");
            } else {
                out("Threw unacceptable IllegalArgumentException! FAILED");
                result = false;
            }
            out(iae.toString());
        }

        try {
            seq.setLoopStartPoint(0);
            if (seq.getLoopStartPoint() != 0) {
                out("setLoopStartPoint()", isSequenceLoaded,
                    "doesn't set the start point correctly!");
                result = false;
            }
        } catch (IllegalArgumentException iae) {
            if (!isSequenceLoaded) {
                out("Caught permissable IllegalArgumentException:");
            } else {
                out("Threw unacceptable IllegalArgumentException! FAILED");
                result = false;
            }
            out(iae.toString());
        }

        if (isSequenceLoaded) {
            seq.setLoopEndPoint(sequence.getTickLength());
            if (seq.getLoopEndPoint() != sequence.getTickLength()) {
                out("setLoopEndPoint()", isSequenceLoaded,
                    "doesn't set the end point correctly!");
                result = false;
            }
        } else {
            
            seq.setLoopEndPoint(-1);
            if (seq.getLoopEndPoint() != -1) {
                out("setLoopEndPoint()", isSequenceLoaded,
                    "doesn't set the end point correctly!");
                result = false;
            }
        }

        if (seq.getLoopCount() != 0) {
            out("loop count", isSequenceLoaded,
                "isn't 0!");
            result = false;
        }

        seq.setLoopCount(1001);
        if (seq.getLoopCount() != 1001) {
            out("setLoopCount()", isSequenceLoaded,
                "doesn't set the loop count correctly!");
            result = false;
        }

        seq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        if (seq.getLoopCount() != Sequencer.LOOP_CONTINUOUSLY) {
            out("setLoopCount(Sequencer.LOOP_CONTINUOUSLY)", isSequenceLoaded,
                "doesn't set the loop count correctly!");
            result = false;
        }

        try {
            seq.setLoopCount(-55);
            out("setLoopCount()", isSequenceLoaded,
                "doesn't throw IllegalArgumentException on illegal value!");
            result = false;
        } catch (IllegalArgumentException e) {
            
            out("Caught permissable IAE");
        }

        seq.setLoopCount(0);
        if (seq.getLoopCount() != 0) {
            out("setLoopCount()", isSequenceLoaded,
                "doesn't set the loop count correctly!");
            result = false;
        }

        return result;
    }

    private static boolean testPlay(Sequencer seq) {
        boolean result = true;
        long stopTime;

        out("TestPlay");

        TestMetaEventListener listener = new TestMetaEventListener();
        seq.addMetaEventListener(listener);
        long startTime = System.currentTimeMillis();
        try {
            seq.open();
            out("Playing sequence, length="+(seq.getMicrosecondLength()/1000)+"millis");
            seq.start();
            while (true) {
                stopTime = listener.getStopTime();
                if (stopTime != 0) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            long measuredDuration = stopTime - startTime;
            out("play duration (us): " + measuredDuration);
        } catch (Exception e) {
            out("test not executed; exception:");
            e.printStackTrace();
        }
        seq.close();
        return result;
    }

    
    private static Sequence createSequence() {
        Sequence sequence = null;
        int lengthInSeconds = 2;
        long lengthInMicroseconds = lengthInSeconds * 1000000;
        int resolution = 480;
        long lengthInTicks = (lengthInMicroseconds * 120 * resolution) / 60000000l;
        out("length in ticks: " + lengthInTicks);
        try {
            sequence = new Sequence(Sequence.PPQ, resolution, 1);
            Track track = sequence.createTrack();
            ShortMessage mm = new ShortMessage();
            mm.setMessage(0xF6, 0, 0);
            MidiEvent me = new MidiEvent(mm, lengthInTicks);
            track.add(me);
        } catch (InvalidMidiDataException e) {
            
        }
        out("sequence length (ticks): " + sequence.getTickLength());
        out("sequence length (us): " + sequence.getMicrosecondLength());
        return sequence;
    }


    private static void out(String m1, boolean isSequenceLoaded, String m2) {
        out(m1 + (isSequenceLoaded ? " with Sequence " : " without Sequence ") + m2);
    }

    private static void out(String message) {
        System.out.println(message);
    }

    private static class TestMetaEventListener implements MetaEventListener {
        private long stopTime;


        public void meta(MetaMessage m) {
            System.out.print("  Got MetaMessage: ");
            if (m.getType() == 47) {
                stopTime = System.currentTimeMillis();
                System.out.println(" End Of Track -- OK");
            } else {
                System.out.println(" unknown. Ignored.");
            }
        }

        public long getStopTime() {
            return stopTime;
        }
    }
}
