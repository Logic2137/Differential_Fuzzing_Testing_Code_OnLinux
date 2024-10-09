



import java.io.*;
import java.util.zip.DeflaterOutputStream;

public class WriteParams {

    
    public static void doTest(OutputStream out) throws Exception {

        int off[] = {-1, -1,  0, 0, 33, 33, 0, 32, 32, 4, 1, 0,  -1,
                     Integer.MAX_VALUE, 1, Integer.MIN_VALUE,
                     Integer.MIN_VALUE, 1};
        int len[] = {-1,  0, -1, 33, 0, 4, 32, 0, 4, 16, 31, 0,
                     Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                     1, -1, Integer.MIN_VALUE};
        boolean results[] = { false,  false,  false, false, false, false,
                              true, true, false, true, true, true,  false,
                              false, false, false, false, false};
        int numCases = off.length;
        byte b[] = new byte[32];
        int numBad = 0;

        for(int i = 0; i < numCases; i++) {
            try {
                out.write(b , off[i] , len[i]);
            } catch (IndexOutOfBoundsException aiobe) {
                if (results[i]) {
                    System.err.println("Error:IndexOutOfBoundsException thrown"+
                                       " for write(b, " + off[i] + " " + len[i] +
                                       " ) on " + out + "\nb.length = 32");
                    numBad++;
                } else {
                    
                }
                continue;
            } catch (OutOfMemoryError ome) {
                System.err.println("Error: OutOfMemoryError in write(b, " +
                                   off[i] + " " + len[i] + " ) on " + out +
                                   "\nb.length = 32");
                numBad++;
                continue;
            }
            if (!results[i]) {
                System.err.println("Error:No IndexOutOfBoundsException thrown"+
                                   " for write(b, " + off[i] + " " + len[i] +
                                   " ) on " + out + "\nb.length = 32");
                numBad++;
            } else {
                
            }
        }

        if (numBad > 0) {
            throw new RuntimeException(out + " Failed " + numBad + " cases");
        } else {
            System.err.println("Successfully completed bounds tests on " + out);
        }
    }

    
    public static void doTest1(OutputStream out) throws Exception {
        byte b[] = null;
        try {
            out.write(b, 0, 32);
        } catch (NullPointerException npe) {
            System.err.println("SuccessFully completed null b test on " + out);
            return;
        }
        throw new RuntimeException(out + " Failed null b test");
    }

    public static void main(String args[]) throws Exception{
        
        File fn = new File("x.WriteBounds");
        FileOutputStream fout = new FileOutputStream(fn);
        for (int i = 0; i < 32; i++) {
            fout.write(i);
        }
        fout.close();

        byte b[] = new byte[64];
        for(int i = 0; i < 64; i++) {
            b[i] = 1;
        }

        
        FileOutputStream fos = new FileOutputStream(fn);
        doTest(fos);
        doTest1(fos);
        fos.close();

        ObjectOutputStream oos = new ObjectOutputStream(new MyOutputStream());
        doTest(oos);
        doTest1(oos);
        oos.close();

        BufferedOutputStream bos =
            new BufferedOutputStream(new MyOutputStream());
        doTest(bos);
        doTest1(bos);
        bos.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doTest(baos);
        doTest1(baos);
        baos.close();

        DataOutputStream dos = new DataOutputStream(new MyOutputStream());
        doTest(dos);
        doTest1(dos);
        dos.close();

        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream();
        pos.connect(pis);
        doTest(pos);
        doTest1(pos);
        pos.close();

        DeflaterOutputStream dfos = new DeflaterOutputStream(new MyOutputStream());
        doTest(dfos);
        doTest1(dfos);
        dfos.close();

        OutputStream nos = OutputStream.nullOutputStream();
        doTest(nos);
        doTest1(nos);
        nos.close();

        
        fn.delete();

    }
}


class MyOutputStream extends OutputStream {

    public MyOutputStream() {
    }

    public void write(int b) throws IOException {
    }
}
