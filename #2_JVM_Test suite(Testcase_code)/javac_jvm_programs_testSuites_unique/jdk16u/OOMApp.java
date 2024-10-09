

package jdk.jfr.event.gc.detailed;

import java.util.List;
import java.util.LinkedList;


public class OOMApp {

    public static List<DummyObject> dummyList;

    public static void main(String[] args) {
        int bytesToAllocate;

        if (args.length > 0) {
            bytesToAllocate = Integer.parseInt(args[0]);
        } else {
            bytesToAllocate = 1024;
        }
        System.gc();
        dummyList = new LinkedList<DummyObject>();
        System.out.println("## Initiate OOM ##");
        try {
            while (true) {
                dummyList.add(new DummyObject(bytesToAllocate));
            }
        } catch (OutOfMemoryError e) {
            System.out.println("## Got OOM ##");
            dummyList = null;
        }
        System.gc();
    }

    public static class DummyObject {
        public byte[] payload;

        DummyObject(int bytesToAllocate) {
            payload = new byte[bytesToAllocate];
        }
    }
}
