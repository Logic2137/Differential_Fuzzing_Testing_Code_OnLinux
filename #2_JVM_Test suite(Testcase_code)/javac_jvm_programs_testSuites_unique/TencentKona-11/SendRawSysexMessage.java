

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import static javax.sound.midi.SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE;
import static javax.sound.midi.SysexMessage.SYSTEM_EXCLUSIVE;


public final class SendRawSysexMessage {

    private static final class RawMidiMessage extends MidiMessage {
        @Override
        public RawMidiMessage clone() {
            return new RawMidiMessage(getMessage());
        }
        @Override
        public int getStatus() {
            return SYSTEM_EXCLUSIVE; 
        }

        RawMidiMessage(byte[] data) {
            super(data.clone());
        }
    }

    public static void main(String[] args) throws Exception {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        System.err.println("List of devices to test:");
        for (int i = 0; i < infos.length; i++) {
            System.err.printf("\t%d.\t%s%n", i, infos[i]);
        }
        for (int i = 0; i < infos.length; i++) {
            System.err.printf("%d.\t%s%n", i, infos[i]);
            try {
                test(infos[i]);
            } catch (MidiUnavailableException ignored){
                
            }
        }
    }

    private static void test(MidiDevice.Info info) throws Exception {
        try (MidiDevice device = MidiSystem.getMidiDevice(info)) {
            System.err.println("Sending to " + device + " (" + info + ")");
            if (!device.isOpen())
                device.open();
            try (Receiver r = device.getReceiver()) {
                System.err.println("note on");
                r.send(new ShortMessage(ShortMessage.NOTE_ON, 5, 5), -1);
                System.err.println("sysex");
                r.send(new SysexMessage(new byte[]{
                        (byte) SYSTEM_EXCLUSIVE, 0x0, 0x03, 0x04,
                        (byte) SPECIAL_SYSTEM_EXCLUSIVE}, 5), -1);
                System.err.println("raw 1");
                r.send(new RawMidiMessage(new byte[]{
                        (byte) 0x02, 0x02, 0x03, 0x04}), -1);
                System.err.println("raw 2");
                r.send(new RawMidiMessage(new byte[]{
                        (byte) 0x09, 0x02, 0x03, 0x04}), -1);
                System.err.println("raw 3");
                r.send(new RawMidiMessage(new byte[]{
                        (byte) SYSTEM_EXCLUSIVE, 0x02, 0x03, 0x04}), -1);
                System.err.println("raw 4");
                r.send(new RawMidiMessage(new byte[]{
                        (byte) 0x02, 0x02, 0x03, 0x04}), -1);
                System.err.println("raw 5");
                r.send(new RawMidiMessage(new byte[]{
                        (byte) 0x02, 0x02, 0x03,
                        (byte) SPECIAL_SYSTEM_EXCLUSIVE}), -1);
                System.err.println("raw 6");
                r.send(new RawMidiMessage(new byte[]{
                        (byte) SYSTEM_EXCLUSIVE, 0x02, 0x03, 0x04}), -1);
                System.err.println("sleep");
                Thread.sleep(1000);
                System.err.println("raw 7");
                r.send(new RawMidiMessage(new byte[]{
                        (byte) 0x02, 0x02, 0x03, 0x04}), -1);
                System.err.println("sleep");
                Thread.sleep(1000);
                System.err.println("raw 8");
                r.send(new RawMidiMessage(new byte[]{
                        (byte) SPECIAL_SYSTEM_EXCLUSIVE}), -1);
                System.err.println("note off");
                r.send(new ShortMessage(ShortMessage.NOTE_OFF, 5, 5), -1);
                System.err.println("done, should quit");
                System.err.println();
            }
        }
    }
}
