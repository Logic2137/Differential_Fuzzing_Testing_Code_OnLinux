
package org.openjdk.bench.java.util.stream.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class DataProviders {

    public static Stream<String> dictionary() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(DataProviders.class.getResourceAsStream("cmudict-0.7b.txt")));
        return r.lines().filter(w -> w.charAt(0) >= 'A' && w.charAt(0) <= 'Z').map(w -> w.substring(0, w.indexOf(" "))).onClose(() -> {
            try {
                r.close();
            } catch (IOException e) {
            }
        });
    }
}
