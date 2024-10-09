



import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LargeCopyWithMark {

    static final int BUFF_SIZE = 8192;
    static final int BIS_BUFF_SIZE = Integer.MAX_VALUE / 2 + 100;
    static final long BYTES_TO_COPY = 2L * Integer.MAX_VALUE;

    static {
        assert BIS_BUFF_SIZE * 2 < 0 : "doubling must overflow";
    }

    public static void main(String[] args) throws Exception {
        byte[] buff = new byte[BUFF_SIZE];

        try (InputStream myis = new MyInputStream(BYTES_TO_COPY);
             InputStream bis = new BufferedInputStream(myis, BIS_BUFF_SIZE);
             OutputStream myos = new MyOutputStream()) {

            
            bis.mark(BIS_BUFF_SIZE + 100);

            for (;;) {
                int count = bis.read(buff, 0, BUFF_SIZE);
                if (count == -1)
                    break;
                myos.write(buff, 0, count);
            }
        }
    }
}

class MyInputStream extends InputStream {
    private long bytesLeft;
    public MyInputStream(long bytesLeft) {
        this.bytesLeft = bytesLeft;
    }
    @Override public int read() throws IOException {
        return 0;
    }
    @Override public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }
    @Override public int read(byte[] b, int off, int len) throws IOException {
        if (bytesLeft <= 0)
            return -1;
        long result = Math.min(bytesLeft, (long)len);
        bytesLeft -= result;
        return (int)result;
    }
    @Override public int available() throws IOException {
        return (bytesLeft > 0) ? 1 : 0;
    }
}

class MyOutputStream extends OutputStream {
    @Override public void write(int b) throws IOException {}
    @Override public void write(byte[] b) throws IOException {}
    @Override public void write(byte[] b, int off, int len) throws IOException {}
}
