

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


public abstract class JdwpReply {

    protected final static int HEADER_LEN = 11;
    private byte[] errCode = new byte[2];
    private byte[] data;

    public final void initFromStream(InputStream is) throws IOException {
        DataInputStream ds = new DataInputStream(is);

        int length = ds.readInt();
        int id = ds.readInt();
        byte flags = (byte) ds.read();

        ds.read(errCode, 0, 2);

        int dataLength = length - HEADER_LEN;
        if (dataLength > 0) {
            data = new byte[dataLength];
            int bytesRead = ds.read(data, 0, dataLength);
            
            
            
            if (bytesRead > 0 && bytesRead < dataLength) {
                System.out.println("[" + getClass().getName() + "] Only " +
                        bytesRead + " bytes of " + dataLength + " were " +
                        "read in the first packet. Reading the rest...");
                ds.read(data, bytesRead, dataLength - bytesRead);
            }

            parseData(new DataInputStream(new ByteArrayInputStream(data)));
        }
    }

    protected void parseData(DataInputStream ds) throws IOException {
    }

    protected byte[] readJdwpString(DataInputStream ds) throws IOException {
        byte[] str = null;
        int len = ds.readInt();
        if (len > 0) {
            str = new byte[len];
            ds.read(str, 0, len);
        }
        return str;
    }

    protected long readRefId(DataInputStream ds) throws IOException {
        return ds.readLong();
    }

    public int getErrorCode() {
        return (((errCode[0] & 0xFF) << 8) | (errCode[1] & 0xFF));
    }
}
