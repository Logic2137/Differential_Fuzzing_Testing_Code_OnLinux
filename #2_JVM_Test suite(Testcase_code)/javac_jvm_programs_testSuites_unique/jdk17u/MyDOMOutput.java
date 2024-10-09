
package test.auctionportal;

import org.w3c.dom.ls.LSOutput;
import java.io.OutputStream;
import java.io.Writer;

public class MyDOMOutput implements LSOutput {

    private OutputStream bytestream;

    private String encoding;

    private String sysId;

    private Writer writer;

    @Override
    public OutputStream getByteStream() {
        return bytestream;
    }

    @Override
    public Writer getCharacterStream() {
        return writer;
    }

    @Override
    public String getEncoding() {
        return encoding;
    }

    @Override
    public String getSystemId() {
        return sysId;
    }

    @Override
    public void setByteStream(OutputStream bs) {
        bytestream = bs;
    }

    @Override
    public void setCharacterStream(Writer cs) {
        writer = cs;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void setSystemId(String sysId) {
        this.sysId = sysId;
    }
}
