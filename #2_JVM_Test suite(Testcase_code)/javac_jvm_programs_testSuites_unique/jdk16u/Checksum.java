

import java.util.zip.CRC32;


public class Checksum extends CRC32
{
    
    public void update(int val) {
        byte[] b = new byte[4];
        b[0] = (byte)((val >>> 24) & 0xff);
        b[1] = (byte)((val >>> 16) & 0xff);
        b[2] = (byte)((val >>> 8) & 0xff);
        b[3] = (byte)(val & 0xff);
        update(b);
    }

    
    void update(long val) {
        byte[] b = new byte[8];
        b[0] = (byte)((val >>> 56) & 0xff);
        b[1] = (byte)((val >>> 48) & 0xff);
        b[2] = (byte)((val >>> 40) & 0xff);
        b[3] = (byte)((val >>> 32) & 0xff);
        b[4] = (byte)((val >>> 24) & 0xff);
        b[5] = (byte)((val >>> 16) & 0xff);
        b[6] = (byte)((val >>> 8) & 0xff);
        b[7] = (byte)(val & 0xff);
        update(b);
    }
}
