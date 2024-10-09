

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;



public class FileHandlerAccessTest {
    public static void main(String[] args) {
        var count = Integer.parseInt(args[0]);
        System.out.println("Testing with " + count + " threads");
        for (var i = 0; i < count; i++) {
            new Thread(FileHandlerAccessTest::access).start();
        }
    }

    private static void access() {
        try {
            var handler = new FileHandler("sample%g.log", 1048576, 2, true);
            handler.publish(new LogRecord(Level.SEVERE, "TEST"));
            handler.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
