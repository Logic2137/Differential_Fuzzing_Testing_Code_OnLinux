

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import static javax.sound.midi.SysexMessage.SYSTEM_EXCLUSIVE;


public final class Exceptions {

    public static void main(final String[] args) throws Exception {
        testInvalidMidiDataException();
        testIndexOutOfBoundsException();
        testNullPointerException();
    }

    private static void testInvalidMidiDataException() {
        try {
            
            new SysexMessage(new byte[0], 0);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final InvalidMidiDataException ignored) {
            
        }
        try {
            
            new SysexMessage(new byte[]{(byte) (SYSTEM_EXCLUSIVE)}, 0);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final InvalidMidiDataException ignored) {
            
        }
        try {
            
            new SysexMessage(0, new byte[0], 2);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final InvalidMidiDataException ignored) {
            
        }
        SysexMessage sysexMessage = new SysexMessage();
        try {
            
            sysexMessage.setMessage(new byte[0], 0);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final InvalidMidiDataException ignored) {
            
        }
        try {
            
            sysexMessage.setMessage(new byte[]{(byte) (SYSTEM_EXCLUSIVE)}, 0);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final InvalidMidiDataException ignored) {
            
        }
        try {
            
            sysexMessage.setMessage(new byte[]{0}, 0);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final InvalidMidiDataException ignored) {
            
        }
        try {
            
            sysexMessage.setMessage(0, new byte[0], 0);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final InvalidMidiDataException ignored) {
            
        }
    }

    private static void testIndexOutOfBoundsException() throws Exception {
        
        try {
            new SysexMessage(new byte[]{(byte) (0xF0 & 0xFF)}, 2);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final IndexOutOfBoundsException ignored) {
            
        }
        try {
            new SysexMessage(0xF0, new byte[0], 2);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final IndexOutOfBoundsException ignored) {
            
        }
        SysexMessage sysexMessage = new SysexMessage();
        try {
            sysexMessage.setMessage(new byte[]{(byte) (0xF0 & 0xFF)}, 2);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final IndexOutOfBoundsException ignored) {
            
        }
        try {
            sysexMessage.setMessage(0xF0, new byte[0], 2);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final IndexOutOfBoundsException ignored) {
            
        }

        
        try {
            new SysexMessage(new byte[]{(byte) (0xF0 & 0xFF)}, -1);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final IndexOutOfBoundsException ignored) {
            
        }
        try {
            new SysexMessage(0xF0, new byte[0], -1);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final IndexOutOfBoundsException ignored) {
            
        }
        sysexMessage = new SysexMessage();
        try {
            sysexMessage.setMessage(new byte[]{(byte) (0xF0 & 0xFF)}, -1);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final IndexOutOfBoundsException ignored) {
            
        }
        try {
            sysexMessage.setMessage(0xF0, new byte[0], -1);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final IndexOutOfBoundsException ignored) {
            
        }
    }

    private static void testNullPointerException() throws Exception {
        try {
            new SysexMessage(null, 0);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final NullPointerException ignored) {
            
        }
        try {
            new SysexMessage(SYSTEM_EXCLUSIVE, null, 2);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final NullPointerException ignored) {
            
        }
        SysexMessage sysexMessage = new SysexMessage();
        try {
            sysexMessage.setMessage(null, 0);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final NullPointerException ignored) {
            
        }
        sysexMessage = new SysexMessage();
        try {
            sysexMessage.setMessage(SYSTEM_EXCLUSIVE, null, 2);
            throw new RuntimeException("Expected exception is not thrown");
        } catch (final NullPointerException ignored) {
            
        }
    }
}
