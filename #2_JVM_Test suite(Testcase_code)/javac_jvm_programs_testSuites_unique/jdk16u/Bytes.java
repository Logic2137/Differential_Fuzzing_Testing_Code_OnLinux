

package jdk.jfr.event.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public final class Bytes {
    public final static byte[] WORLD = Bytes.asBytes("world");
    public final static byte[] EARTH = Bytes.asBytes("earth");

    public static byte[] asBytes(String string) {
        byte[] result = new byte[string.length()];
        for (int i = 0; i < string.length(); i++) {
            result[i] = (byte)string.charAt(i);
        }
        return result;
    }

    public static byte[] classBytes(ClassLoader classLoader, String className) throws IOException {
        String classFileName = className.replace(".", "/") + ".class";
        try (InputStream is = classLoader.getResourceAsStream(classFileName)) {
            if (is == null) {
                throw new IOException("Could not find class file " + classFileName);
            }
            return is.readAllBytes();
        }
    }

    public static byte[] classBytes(Class<?> clazz) throws IOException {
        return classBytes(clazz.getClassLoader(), clazz.getName());
    }

    public static byte[] replaceAll(byte[] input, byte[] target, byte[] replacement) {
        List<Byte> result = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            if (hasTarget(input, i, target)) {
                for (int j = 0; j < replacement.length; j++) {
                    result.add(replacement[j]);
                }
                i += target.length - 1;
            } else {
                result.add(input[i]);
            }
        }
        byte[] resultArray = new byte[result.size()];
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = result.get(i);
        }
        return resultArray;
    }

    private static boolean hasTarget(byte[] input, int start, byte[] target) {
        for (int i = 0; i < target.length; i++) {
            if (start + i == input.length) {
                return false;
            }
            if (input[start + i] != target[i]) {
                return false;
            }
        }
        return true;
    }
}
