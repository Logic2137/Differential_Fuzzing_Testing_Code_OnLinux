



import java.util.Properties;
import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;


public class SaveClose {


    static class O extends FilterOutputStream {

        boolean closed = false;

        O(OutputStream o) {
            super(o);
        }

        public void close() throws IOException {
            this.closed = true;
        }

    }


    public static void main(String argv[]) throws Exception {
        Properties p = new Properties();
        p.put("Foo", "Bar");
        O o = new O(System.err);
        p.store(o, "Test");
        if (o.closed)
            throw new Exception("Properties.save closed its output stream");
    }

}
