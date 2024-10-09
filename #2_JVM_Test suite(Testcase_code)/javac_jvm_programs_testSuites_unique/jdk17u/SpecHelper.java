
package jdk.internal.net.http.hpack;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class SpecHelper {

    private SpecHelper() {
        throw new AssertionError();
    }

    public static ByteBuffer toBytes(String hexdump) {
        Pattern hexByte = Pattern.compile("[0-9a-fA-F]{2}");
        List<String> bytes = new ArrayList<>();
        Matcher matcher = hexByte.matcher(hexdump);
        while (matcher.find()) {
            bytes.add(matcher.group(0));
        }
        ByteBuffer result = ByteBuffer.allocate(bytes.size());
        for (String f : bytes) {
            result.put((byte) Integer.parseInt(f, 16));
        }
        result.flip();
        return result;
    }

    public static String toHexdump(ByteBuffer bb) {
        List<String> words = new ArrayList<>();
        int i = 0;
        while (bb.hasRemaining()) {
            if (i % 2 == 0) {
                words.add("");
            }
            byte b = bb.get();
            String hex = Integer.toHexString(256 + Byte.toUnsignedInt(b)).substring(1);
            words.set(i / 2, words.get(i / 2) + hex);
            i++;
        }
        return words.stream().collect(Collectors.joining(" "));
    }
}
