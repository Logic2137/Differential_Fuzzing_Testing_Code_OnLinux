import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.sound.sampled.*;
import com.sun.media.sound.*;

public class MarkReset {

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
            for (int i = 0; i < 8; i++) {
                ModelByteBuffer buff;
                if (i % 2 == 0)
                    buff = new ModelByteBuffer(test_file);
                else
                    buff = new ModelByteBuffer(test_byte_array);
                if ((i / 2) == 1)
                    buff.subbuffer(5);
                if ((i / 2) == 2)
                    buff.subbuffer(5, 500);
                if ((i / 2) == 3)
                    buff.subbuffer(5, 600, true);
                long capacity = buff.capacity();
                InputStream is = buff.getInputStream();
                try {
                    is.mark(1000);
                    int ret = is.available();
                    int a = is.read();
                    is.skip(75);
                    is.reset();
                    if (is.available() != ret)
                        throw new RuntimeException("is.available() returns incorrect value (" + is.available() + "!=" + (ret) + ") !");
                    int b = is.read();
                    if (a != b)
                        throw new RuntimeException("is doesn't return same value after reset (" + a + "!=" + b + ") !");
                    is.skip(15);
                    ret = is.available();
                    is.mark(1000);
                    is.reset();
                    if (is.available() != ret)
                        throw new RuntimeException("is.available() returns incorrect value (" + is.available() + "!=" + (ret) + ") !");
                } finally {
                    is.close();
                }
                if (buff.capacity() != capacity)
                    throw new RuntimeException("Capacity variable should not change!");
            }
        } finally {
            tearDown();
        }
    }
}
