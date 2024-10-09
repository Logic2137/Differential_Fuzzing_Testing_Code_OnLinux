
package org.netbeans.jemmy;

import java.io.PrintStream;
import java.io.PrintWriter;

public class JemmyException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    private Throwable innerException = null;

    private Object object = null;

    public JemmyException(String description) {
        super(description);
    }

    public JemmyException(String description, Throwable innerException) {
        this(description);
        this.innerException = innerException;
    }

    public JemmyException(String description, Object object) {
        this(description);
        this.object = object;
    }

    public JemmyException(String description, Throwable innerException, Object object) {
        this(description, innerException);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Deprecated
    public Exception getInnerException() {
        if (innerException instanceof Exception) {
            return (Exception) innerException;
        } else {
            return null;
        }
    }

    public Throwable getInnerThrowable() {
        return innerException;
    }

    @Override
    public void printStackTrace() {
        printStackTrace(System.out);
    }

    @Override
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (innerException != null) {
            ps.println("Inner exception:");
            innerException.printStackTrace(ps);
        }
        if (object != null) {
            ps.println("Object:");
            ps.println(object.toString());
        }
    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (innerException != null) {
            pw.println("Inner exception:");
            innerException.printStackTrace(pw);
        }
        if (object != null) {
            pw.println("Object:");
            pw.println(object.toString());
        }
    }
}
