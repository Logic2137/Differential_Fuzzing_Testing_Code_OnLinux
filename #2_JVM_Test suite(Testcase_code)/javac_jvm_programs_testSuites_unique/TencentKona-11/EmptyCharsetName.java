



import java.io.*;
import java.nio.*;
import java.nio.charset.*;


public class EmptyCharsetName {

    static abstract class Test {

        public abstract void go() throws Exception;

        Test() throws Exception {
            try {
                go();
            } catch (Exception x) {
                if (x instanceof IllegalCharsetNameException) {
                    System.err.println("Thrown as expected: " + x);
                    return;
                }
                throw new Exception("Incorrect exception: "
                                    + x.getClass().getName(),
                                    x);
            }
            throw new Exception("No exception thrown");
        }

    }

    public static void main(String[] args) throws Exception {

        new Test() {
                public void go() throws Exception {
                    Charset.forName("");
                }};
        new Test() {
                public void go() throws Exception {
                    Charset.isSupported("");
                }};
        new Test() {
                public void go() throws Exception {
                    new Charset("", new String[] { }) {
                            public CharsetDecoder newDecoder() {
                                return null;
                            }
                            public CharsetEncoder newEncoder() {
                                return null;
                            }
                            public boolean contains(Charset cs) {
                                return false;
                            }
                        };
                }};
    }

}
