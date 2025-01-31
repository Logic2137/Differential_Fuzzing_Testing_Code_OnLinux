



import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class MemoryCacheImageOutputStreamTest {

    public static void main(String[] args) throws IOException {
        try {
            MemoryCacheImageOutputStream stream =
                new MemoryCacheImageOutputStream(new ByteArrayOutputStream());
            stream.write(0);  
            stream.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error flushing stream: " + e);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageOutputStream ios = new MemoryCacheImageOutputStream(os);

        byte[] b = new byte[30*256];
        byte byteVal = (byte)0;
        for (int i = 0; i < b.length; i++) {
            b[i] = byteVal++;
        }

        
        for (int i = 0; i < 34; i++) {
            ios.write(b);
        }
        
        
        byte[] buf = new byte[1];
        for (int i = 0; i < 256; i += 2) {
            ios.seek(1000*i);
            ios.write(i);

            ios.seek(1000*(i + 1));
            buf[0] = (byte)(i + 1);
            ios.write(buf);
        }

        
        for (int i = 0; i < 256; i++) {
            ios.seek(1000*i);
            int val = ios.read();
            if (val != i) {
                System.out.println("Got bad value (1) at pos = " + (1000*i));
            }
        }

        
        ios.flushBefore(2*8192);

        for (int i = 0; i < 256; i++) {
            long pos = 1000*i;
            if (pos >= 2*8192) {
                ios.seek(pos);
                int val = ios.read();
                if (val != i) {
                    System.out.println("Got bad value (2) at pos = " + (1000*i));
                }
            }
        }
        ios.close();

        byte[] data = os.toByteArray();
        for (int i = 0; i < data.length; i++) {
            byte val = data[i];
            if ((i < 256000) && (i % 1000) == 0) {
                if (val != (byte)(i/1000)) {
                    System.out.println("Got bad value (3) at pos = " + i);
                }
            } else {
                byte gval = (byte)((i % (30*256)) % 256);
                if (val != gval) {
                    System.out.println("Got bad value (4) at pos = " + i +
                                       "(got " + (val & 0xff) +
                                       " wanted " + (gval & 0xff) +")");
                }
            }
        }
    }
}
