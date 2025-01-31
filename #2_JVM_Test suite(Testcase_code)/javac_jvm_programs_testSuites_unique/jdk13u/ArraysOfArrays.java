import java.io.*;

public class ArraysOfArrays {

    public static void main(String[] argv) throws IOException {
        System.err.println("\nRegression test for testing of " + "serialization/deserialization of objects as " + "arrays of arrays \n");
        FileInputStream istream = null;
        FileOutputStream ostream = null;
        try {
            ostream = new FileOutputStream("piotest5.tmp");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            byte[][] b = { { 0, 1 }, { 2, 3 } };
            p.writeObject((Object) b);
            short[][] s = { { 0, 1, 2 }, { 3, 4, 5 } };
            p.writeObject((Object) s);
            char[][] c = { { 0, 1, 2, 3 }, { 4, 5, 6, 7 } };
            p.writeObject((Object) c);
            int[][] i = { { 0, 1, 2, 3, 4 }, { 5, 6, 7, 8, 9 } };
            p.writeObject((Object) i);
            long[][] l = { { 0, 1, 2, 3, 4, 5 }, { 6, 7, 8, 9, 10, 11 } };
            p.writeObject((Object) l);
            boolean[][] z = new boolean[2][2];
            z[0][0] = true;
            z[0][1] = false;
            z[1] = z[0];
            p.writeObject((Object) z);
            float[][] f = { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f }, { 1.1f, 2.1f, 3.1f, 4.1f, 5.1f, 6.1f } };
            p.writeObject((Object) f);
            double[][] d = { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0d }, { 1.1f, 2.1f, 3.1f, 4.1f, 5.1f, 6.1f, 7.1d } };
            p.writeObject((Object) d);
            Integer[][] Int = { { new Integer(3), new Integer(2) }, { new Integer(1), new Integer(0) } };
            p.writeObject((Object) Int);
            p.flush();
            istream = new FileInputStream("piotest5.tmp");
            ObjectInputStream q = new ObjectInputStream(istream);
            byte[][] b_u = (byte[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\nByte array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            short[][] s_u = (short[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\nshort array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            char[][] c_u = (char[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\nchar array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            int[][] i_u = (int[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\nint array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            long[][] l_u = (long[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\nlong array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            boolean[][] z_u = (boolean[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\nboolean array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            float[][] f_u = (float[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\nfloat array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            double[][] d_u = (double[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\ndouble array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            Integer[][] Int_u = (Integer[][]) (q.readObject());
            for (int ix = 0; ix < b_u.length; ix++) {
                for (int iy = 0; iy < b_u[ix].length; iy++) {
                    if (b[ix][iy] != b_u[ix][iy]) {
                        System.err.println("\nInteger array mismatch [" + ix + "][" + iy + " expected " + b[ix][iy] + " actual = " + b_u[ix][iy]);
                        throw new Error();
                    }
                }
            }
            System.err.println("\nTEST PASSED");
        } catch (Exception e) {
            System.err.print("TEST FAILED: ");
            e.printStackTrace();
            System.err.println("\nInput remaining");
            int ch;
            try {
                while ((ch = istream.read()) != -1) {
                    System.err.print("\n " + Integer.toString(ch, 16) + " ");
                }
                System.err.println("\n ");
            } catch (Exception f) {
                throw new Error();
            }
            throw new Error();
        } finally {
            if (istream != null)
                istream.close();
            if (ostream != null)
                ostream.close();
        }
    }
}
