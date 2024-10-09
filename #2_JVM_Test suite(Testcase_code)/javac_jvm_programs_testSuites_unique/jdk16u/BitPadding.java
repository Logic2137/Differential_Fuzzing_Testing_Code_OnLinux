



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.stream.FileCacheImageOutputStream;

public class BitPadding {

    public static void main(String[] args) throws IOException {
        OutputStream ostream = new ByteArrayOutputStream();
        File f = null;
        FileCacheImageOutputStream fcios =
            new FileCacheImageOutputStream(ostream, f);
        fcios.writeBit(1);
        fcios.write(96);

        fcios.seek(0);
        int r1 = fcios.read();
        if (r1 != 128 ) {
            throw new RuntimeException("Failed, first byte is " + r1);
        }

        int r2 = fcios.read();
        if (r2 != 96) {
            throw new RuntimeException("Failed, second byte is " + r2);
        }
    }
}
