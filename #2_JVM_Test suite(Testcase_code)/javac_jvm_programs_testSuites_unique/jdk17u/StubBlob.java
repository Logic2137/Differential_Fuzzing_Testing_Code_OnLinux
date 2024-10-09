
package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StubBlob implements Blob {

    private byte[] bytes;

    public StubBlob() {
        bytes = new byte[] { 2, 4, 6, 8 };
    }

    public long length() throws SQLException {
        return bytes.length;
    }

    public byte[] getBytes(long pos, int length) throws SQLException {
        return Arrays.copyOfRange(bytes, (int) pos - 1, length);
    }

    public InputStream getBinaryStream() throws SQLException {
        return null;
    }

    public long position(byte[] pattern, long start) throws SQLException {
        return 0;
    }

    public long position(Blob pattern, long start) throws SQLException {
        return 0;
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException {
        return 0;
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        return 0;
    }

    public OutputStream setBinaryStream(long pos) throws SQLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException ex) {
            Logger.getLogger(StubBlob.class.getName()).log(Level.SEVERE, null, ex);
        }
        return oos;
    }

    public void truncate(long len) throws SQLException {
    }

    public void free() throws SQLException {
    }

    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        return null;
    }
}
