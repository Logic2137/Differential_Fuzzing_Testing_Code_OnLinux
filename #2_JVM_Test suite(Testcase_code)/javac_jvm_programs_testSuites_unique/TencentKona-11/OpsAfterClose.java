



import java.io.*;

public enum OpsAfterClose {

        READ { boolean check(Reader r) {
                    try {
                        r.read();
                    } catch (IOException io) {
                        return true;
                    }
                    return false;
             } },

        READ_BUF { boolean check(Reader r) {
                    try {
                        char buf[] = new char[2];
                        int len = 1;
                        r.read(buf, 0, len);
                    } catch (IOException io) {
                        return true;
                    }
                    return false;
             } },
        READY { boolean check(Reader r) {
                    try {
                         r.ready();
                    } catch (IOException io) {
                        return true;
                    }
                    return false;
             } },
        MARK { boolean check(Reader r) {
                   try {
                         r.mark(1);
                    } catch (IOException io) {
                        return true;
                    }
                    return false;
             } },
        SKIP { boolean check(Reader r) {
                   try {
                         r.skip(1);
                    } catch (IOException io) {
                        return true;
                    }
                    return false;
             } },
        RESET { boolean check(Reader r) {
                   try {
                         r.reset();
                    } catch (IOException io) {
                        return true;
                    }
                    return false;
             } },
        CLOSE { boolean check(Reader r) {
                   try {
                         r.close();
                    } catch (IOException io) {
                        return false;
                    }
                    return true;
             } };

    abstract boolean check(Reader r);

    public static void main(String args[]) throws Exception {

        boolean failed = false;

        BufferedReader br = new BufferedReader(
                            new StringReader("abc def ghi"));
        if (testReader(br)) {
            failed = true;
        }

        CharArrayReader car = new CharArrayReader(new char[2]);
        if (testReader(car)) {
            failed = true;
        }

        PushbackReader pbr = new PushbackReader(
                                new CharArrayReader(new char[2]));
        if (testReader(pbr)) {
            failed = true;
        }
        if (testPushbackReader(pbr)) {
            failed = true;
        }

        StringReader sr = new StringReader("abc def ghi");
        if (testReader(sr)) {
            failed = true;
        }

        InputStreamReader isr = new InputStreamReader(
                                new ByteArrayInputStream("abc".getBytes()));
        if (testReader(isr)) {
            failed = true;
        }

        LineNumberReader lnr = new LineNumberReader(
                            new StringReader("abc def ghi"));
        if (testReader(lnr)) {
            failed = true;
        }

        File f = new File(System.getProperty("test.dir", "."),
                          "NewFile");
        f.createNewFile();
        f.deleteOnExit();

        FileReader fr = new FileReader(f);
        if (testReader(fr)) {
            failed = true;
        }

        PipedWriter pw = new PipedWriter();
        PipedReader pr = new PipedReader(pw);
        if (testReader(pr)) {
            failed = true;
        }
        if (failed) {
            throw new Exception("Test failed for some of the operation{s}" +
                " on some of the reader{s}, check the messages");
        }
    }

    private static boolean testReader(Reader r) throws Exception {
        r.close();
        boolean failed = false;
        boolean result;
        System.out.println("Testing reader:" + r);
        for (OpsAfterClose op : OpsAfterClose.values()) {
            result = op.check(r);
            if (!result) {
                failed = true;
            }
           System.out.println(op + ":" + result);
        }
        if (failed) {
            System.out.println("Test failed for the failed operation{s}" +
                        " above for the Reader:" + r);
        }
        return failed;
    }

    private static boolean testPushbackReader(PushbackReader pr)
                throws Exception {
        boolean failed = false;
        try {
            pr.unread(1);
            System.out.println("Test failed for unread(int):" + pr);
            failed = true;
        } catch (IOException io) {
           System.out.println("UNREAD(int):true");
        }

        char buf[] = new char[2];
        try {
            pr.unread(buf, 0, 2);
            System.out.println("Test failed for unread(buf, offset, len):" +
                                pr);
            failed = true;
        } catch (IOException io) {
           System.out.println("UNREAD(buf, offset, len):true");
        }
        try {
            pr.unread(buf);
            System.out.println("Test failed for unread(char[] buf):" + pr);
            failed = true;
        } catch (IOException io) {
           System.out.println("UNREAD(buf):true");
        }
        return failed;
    }
}
