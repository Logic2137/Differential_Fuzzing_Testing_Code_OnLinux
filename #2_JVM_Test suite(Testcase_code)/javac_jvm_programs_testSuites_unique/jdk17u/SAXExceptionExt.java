
package sax;

import org.xml.sax.SAXException;

public class SAXExceptionExt extends SAXException {

    private Exception exception;

    public SAXExceptionExt(Exception e) {
        super(e);
        exception = e;
    }

    public Throwable getCause() {
        return exception;
    }
}
