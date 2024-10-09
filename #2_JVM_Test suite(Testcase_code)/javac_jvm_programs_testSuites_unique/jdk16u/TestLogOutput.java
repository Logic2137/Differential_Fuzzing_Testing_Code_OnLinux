

package jdk.jfr.jvm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class TestLogOutput {
    public static void main(String[] args) throws Exception {
        final String fileName = "jfr_trace.txt";
        final List<String>findWhat = new ArrayList<>();
        findWhat.add("Starting a recording");
        findWhat.add("Flight Recorder initialized");
        boolean passed = false;
        List<String> matches = new ArrayList<String>(findWhat);

        do {
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(fileName));
            } catch (IOException e) {
                throw new Error(e);
            }
            for (String l : lines) {
                for (String m : matches) {
                    if (l.toString().contains(m)) {
                        matches.remove(m);
                        break;
                    }
                }
            }
            if (matches.size() < 1) {
                passed = true;
                break;
            }
            if (lines.size() > 100) {
                break; 
            }
        } while(!passed);

        if (!passed) {
            throw new Error("Not found " + findWhat  + " in stream");
        }

       System.out.println("PASSED");
    }
}
