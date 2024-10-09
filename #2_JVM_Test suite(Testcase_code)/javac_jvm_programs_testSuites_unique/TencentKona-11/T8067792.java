

import java.util.stream.*;
import java.util.*;

class T8067792 {
    void test(Stream<List<?>> sl) {
        Runnable r = new Runnable() {
            public void run() {
                Stream<List<?>> constructor = sl.filter(c -> true);
            }
        };
    }
}
