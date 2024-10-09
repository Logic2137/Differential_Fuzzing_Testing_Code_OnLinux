
package jdk.jfr.api.consumer.security;

import java.nio.file.Paths;
import jdk.jfr.consumer.RecordingFile;

public class TestRecordingFile {

    public static void main(String... args) throws Exception {
        try {
            RecordingFile.readAllEvents(Paths.get(args[0]));
            throw new AssertionError("Expected SecurityException");
        } catch (SecurityException se) {
            return;
        }
    }
}
