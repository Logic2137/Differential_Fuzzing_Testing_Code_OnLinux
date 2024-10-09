


import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JdpTestUtil {

    static final int HEADER_SIZE = 4 + 2;   

    
    static int decode2ByteInt(byte[] data, int pos) {
        return (((data[pos] & 0xFF) << 8) | (data[pos + 1] & 0xFF));
    }

    
    static int decode4ByteInt(byte[] data, int pos) {
        int result = data[pos + 3] & 0xFF;
        result = result | ((data[pos + 2] & 0xFF) << 8);
        result = result | ((data[pos + 1] & 0xFF) << 16);
        result = result | ((data[pos] & 0xFF) << 24);
        return result;
    }

    
    static String decodeEntry(byte[] data, int pos)
            throws UnsupportedEncodingException {

        int size = JdpTestUtil.decode2ByteInt(data, pos);
        pos = pos + 2;
        byte[] raw = Arrays.copyOfRange(data, pos, pos + size);
        return new String(raw, "UTF-8");
    }

    
    static Map<String, String> readPayload(byte[] rawData)
            throws UnsupportedEncodingException {

        int totalSize = rawData.length;
        int payloadSize = totalSize - HEADER_SIZE;
        byte[] rawPayload = Arrays.copyOfRange(rawData, HEADER_SIZE, HEADER_SIZE + payloadSize);
        Map<String, String> payload = readRawPayload(rawPayload, payloadSize);
        return payload;
    }

    
    static Map<String, String> readRawPayload(byte[] rawPayload, int size)
            throws UnsupportedEncodingException {

        String key, value;
        Map<String, String> payload = new HashMap<String, String>();

        for (int pos = 0; pos < size; ) {
            key = decodeEntry(rawPayload, pos);
            pos = pos + 2 + key.length();
            value = decodeEntry(rawPayload, pos);
            pos = pos + 2 + value.length();

            payload.put(key, value);
        }
        return payload;
    }

    static void enableConsoleLogging(Logger log, Level level) throws SecurityException {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        log.addHandler(handler);
        log.setLevel(level);
    }

}
