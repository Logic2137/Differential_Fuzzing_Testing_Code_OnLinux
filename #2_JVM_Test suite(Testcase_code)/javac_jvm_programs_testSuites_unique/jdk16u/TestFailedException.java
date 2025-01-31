





public class TestFailedException extends java.lang.RuntimeException {
    public Throwable detail;

    public TestFailedException() {}

    public TestFailedException(String s) {
        super(s);
    }

    public TestFailedException(String s, Throwable ex) {
        super(s);
        detail = ex;
    }

    public String getMessage() {
        if (detail == null)
            return super.getMessage();
        else
            return super.getMessage() +
                "; nested exception is: \n\t" +
                detail.toString();
    }

    public void printStackTrace(java.io.PrintStream ps)
    {
        if (detail == null) {
            super.printStackTrace(ps);
        } else {
            synchronized(ps) {
                ps.println(this);
                detail.printStackTrace(ps);
            }
        }
    }

    public void printStackTrace()
    {
        printStackTrace(System.err);
    }

    public void printStackTrace(java.io.PrintWriter pw)
    {
        if (detail == null) {
            super.printStackTrace(pw);
        } else {
            synchronized(pw) {
                pw.println(this);
                detail.printStackTrace(pw);
            }
        }
    }
}
