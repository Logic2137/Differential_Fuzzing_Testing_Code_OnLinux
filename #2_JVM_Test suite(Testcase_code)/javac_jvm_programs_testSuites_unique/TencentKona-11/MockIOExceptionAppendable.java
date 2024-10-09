


package test.java.time.format;

import java.io.IOException;


public class MockIOExceptionAppendable implements Appendable {

    public Appendable append(CharSequence csq) throws IOException {
        throw new IOException();
    }

    public Appendable append(char c) throws IOException {
        throw new IOException();
    }

    public Appendable append(CharSequence csq, int start, int end)
            throws IOException {
        throw new IOException();
    }

}
