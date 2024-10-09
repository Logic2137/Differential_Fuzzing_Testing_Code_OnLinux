
package gc.g1.unloading.bytecode;

import java.util.*;

public class BytecodePatcher {

    static private Map<byte[], byte[]> dictionary = new HashMap<>();

    static {
        dictionary.put("bytesToReplace0".getBytes(), "bytesToReplace1".getBytes());
        dictionary.put("bytesToReplace2".getBytes(), "bytesToReplace3".getBytes());
    }

    public static void patch(byte[] bytecode) {
        for (Map.Entry<byte[], byte[]> entry : dictionary.entrySet()) {
            for (int i = 0; i + entry.getKey().length < bytecode.length; i++) {
                boolean match = true;
                for (int j = 0; j < entry.getKey().length; j++) {
                    if (bytecode[i + j] != entry.getKey()[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    for (int j = 0; j < entry.getKey().length; j++) bytecode[i + j] = entry.getValue()[j];
                }
            }
        }
    }
}
