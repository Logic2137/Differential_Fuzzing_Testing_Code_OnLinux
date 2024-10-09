



import java.io.*;

public class BoundsCheck {
    static class DummyFilterStream extends FilterOutputStream {

        public DummyFilterStream(OutputStream o) {
            super(o);
        }

        public void write(int val) throws IOException {
            super.write(val + 1);
        }
    }

    public static void main(String[] args) throws Exception {
        byte data[] = {90, 91, 92, 93, 94, 95, 96, 97, 98, 99};
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DummyFilterStream dfs = new DummyFilterStream(bos);
        boolean caughtException = false;

        
        try {
            dfs.write(data, 0, -5);
        } catch (IndexOutOfBoundsException ie) {
            caughtException = true;
        } finally {
            if (!caughtException)
                throw new RuntimeException("Test failed");
        }

        
        caughtException = false;
        try {
            dfs.write(data, -2, 5);
        } catch (IndexOutOfBoundsException ie) {
            caughtException = true;
        } finally {
            if (!caughtException)
                throw new RuntimeException("Test failed");
        }

        
        caughtException = false;
        try {
            dfs.write(data, 6, 5);
        } catch (IndexOutOfBoundsException ie) {
            caughtException = true;
        } finally {
            if (!caughtException)
                throw new RuntimeException("Test failed");
        }

        
        caughtException = false;
        try {
            dfs.write(null, 0, 5);
        } catch (NullPointerException re) {
            caughtException = true;
        } finally {
            if (!caughtException)
                throw new RuntimeException("Test failed");
        }
    }
}
