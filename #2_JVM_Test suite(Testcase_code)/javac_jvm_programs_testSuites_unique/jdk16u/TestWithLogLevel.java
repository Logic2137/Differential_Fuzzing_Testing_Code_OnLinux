

 

import java.util.*;

public class TestWithLogLevel {
    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        long count = 300 * 1024 * 1024 / 16; 
        for (long index = 0; index < count; index++) {
            Object sink = new Object();
            list.add(sink);
        }
    }
}
