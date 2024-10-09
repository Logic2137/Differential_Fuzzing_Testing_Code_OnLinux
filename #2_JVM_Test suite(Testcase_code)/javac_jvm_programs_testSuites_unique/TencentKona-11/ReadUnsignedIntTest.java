



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

public class ReadUnsignedIntTest {

    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(1);
        dos.writeInt(0x7fffffff);
        dos.writeInt(0x8fffffff);
        dos.writeInt(0xffffffff);

        dos.close();

        ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        ImageInputStream iis = ImageIO.createImageInputStream(bais);
        for (int i=0; i<4; i++) {
            long res = iis.readUnsignedInt();
            if (res <= 0) {
                throw new RuntimeException("Negative number was read: "+
                                           Long.toString(res, 16));
            }
        }
    }
}
