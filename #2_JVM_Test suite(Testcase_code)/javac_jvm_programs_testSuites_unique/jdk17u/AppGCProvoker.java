
package jdk.jfr.event.gc.collection;

import java.util.ArrayList;
import java.util.List;

public class AppGCProvoker {

    private static List<byte[]> garbage = new ArrayList<>();

    public static Object trash;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            trash = new byte[100_000];
        }
        System.gc();
        try {
            while (true) {
                garbage.add(new byte[150_000]);
            }
        } catch (OutOfMemoryError e) {
            garbage = null;
        }
    }
}
