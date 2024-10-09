
package jdk.jfr.api.consumer.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogAnalyzer {

    private final Path path;

    public LogAnalyzer(String filename) throws IOException {
        this.path = Path.of(filename);
    }

    public void shouldNotContain(String text) throws Exception {
        System.out.println("Should not contain: '" + text + "'");
        while (true) {
            try {
                for (String line : Files.readAllLines(path)) {
                    if (line.contains(text)) {
                        throw new Exception("Found unexpected log message: " + line);
                    }
                }
                return;
            } catch (IOException e) {
                System.out.println("Could not read log file " + path.toAbsolutePath());
                e.printStackTrace();
            }
            Thread.sleep(100);
        }
    }

    public void await(String text) throws InterruptedException {
        System.out.println("Awaiting... '" + text + "' ");
        while (true) {
            try {
                for (String line : Files.readAllLines(path)) {
                    if (line.contains(text)) {
                        System.out.println("Found!");
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Could not read log file " + path.toAbsolutePath());
                e.printStackTrace();
            }
            Thread.sleep(100);
        }
    }
}
