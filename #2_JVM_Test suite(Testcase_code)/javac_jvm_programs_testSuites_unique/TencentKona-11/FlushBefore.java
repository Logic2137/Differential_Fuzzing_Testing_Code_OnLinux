



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.stream.FileCacheImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class FlushBefore {

    public static void main(String[] args) throws IOException {
        OutputStream ostream = new ByteArrayOutputStream();

        FileCacheImageOutputStream fcios =
            new FileCacheImageOutputStream(ostream, null);
        test(fcios);

        MemoryCacheImageOutputStream mcios =
            new MemoryCacheImageOutputStream(ostream);
        test(mcios);
    }

    private static void test(ImageOutputStream ios) throws IOException {
        try {
            ios.write(new byte[10], 0, 10);
            ios.flushBefore(5);
            ios.flushBefore(4);

            throw new RuntimeException
                ("Failed to get IndexOutOfBoundsException!");
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
