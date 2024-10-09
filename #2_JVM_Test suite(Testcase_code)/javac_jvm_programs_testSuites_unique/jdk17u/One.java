import java.io.*;

public class One {

    private abstract static class Test {

        InputStreamReader isr;

        StringBuffer sb;

        String expect;

        Test(byte[] in, String expect) throws Exception {
            isr = new InputStreamReader(new ByteArrayInputStream(in), "UTF-8");
            sb = new StringBuffer(expect.length());
            this.expect = expect;
            go();
        }

        void go() throws Exception {
            read();
            if (!expect.equals(sb.toString()))
                throw new Exception("Expected " + expect + ", got " + sb.toString());
        }

        abstract void read() throws IOException;
    }

    private static void test(String expect) throws Exception {
        byte[] in = expect.getBytes("UTF-8");
        new Test(in, expect) {

            public void read() throws IOException {
                for (; ; ) {
                    int c;
                    if ((c = isr.read()) == -1)
                        break;
                    sb.append((char) c);
                }
            }
        };
        new Test(in, expect) {

            public void read() throws IOException {
                for (; ; ) {
                    char[] cb = new char[1];
                    if (isr.read(cb) == -1)
                        break;
                    sb.append(cb[0]);
                }
            }
        };
        new Test(in, expect) {

            public void read() throws IOException {
                for (; ; ) {
                    char[] cb = new char[2];
                    int n;
                    if ((n = isr.read(cb)) == -1)
                        break;
                    sb.append(cb[0]);
                    if (n == 2)
                        sb.append(cb[1]);
                }
            }
        };
    }

    public static void main(String[] args) throws Exception {
        test("x");
        test("xy");
        test("xyz");
        test("\ud800\udc00");
        test("x\ud800\udc00");
    }
}
