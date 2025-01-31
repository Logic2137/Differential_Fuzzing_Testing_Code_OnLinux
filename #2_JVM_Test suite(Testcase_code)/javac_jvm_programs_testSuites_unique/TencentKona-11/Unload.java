import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.sound.sampled.*;
import com.sun.media.sound.*;

public class Unload {

    static float[] testarray;

    static byte[] test_byte_array;

    static File test_file;

    static AudioFormat format = new AudioFormat(44100, 16, 1, true, false);

    static void setUp() throws Exception {
        testarray = new float[1024];
        for (int i = 0; i < 1024; i++) {
            double ii = i / 1024.0;
            ii = ii * ii;
            testarray[i] = (float) Math.sin(10 * ii * 2 * Math.PI);
            testarray[i] += (float) Math.sin(1.731 + 2 * ii * 2 * Math.PI);
            testarray[i] += (float) Math.sin(0.231 + 6.3 * ii * 2 * Math.PI);
            testarray[i] *= 0.3;
        }
        test_byte_array = new byte[testarray.length * 2];
        AudioFloatConverter.getConverter(format).toByteArray(testarray, test_byte_array);
        test_file = File.createTempFile("test", ".raw");
        try (FileOutputStream fos = new FileOutputStream(test_file)) {
            fos.write(test_byte_array);
        }
    }

    static void tearDown() throws Exception {
        Files.delete(Paths.get(test_file.getAbsolutePath()));
    }

    public static void main(String[] args) throws Exception {
        try {
            setUp();
            ModelByteBuffer buff = new ModelByteBuffer(test_file);
            buff.load();
            buff.unload();
            if (buff.array() != null)
                throw new RuntimeException("buff.array() not null!");
        } finally {
            tearDown();
        }
    }
}
