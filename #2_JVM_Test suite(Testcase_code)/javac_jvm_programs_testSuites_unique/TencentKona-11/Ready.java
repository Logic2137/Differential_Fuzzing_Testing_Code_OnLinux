



import java.io.*;

public class Ready {
    public static void main(String[] args) throws Exception {
        PipedWriter pw = new PipedWriter();
        PipedReader pr = new PipedReader(pw);

        pw.write("input characters");
        if (!pr.ready()) {
            throw new Exception("ready() should return true");
        }
        pr.close();
        try {
            pr.ready();
            throw new Exception("ready() should throw an exception");
        } catch (IOException e) {
        }
    }
}
