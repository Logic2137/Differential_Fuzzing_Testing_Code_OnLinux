import java.io.*;

public class CheckingEquality {

    public static void main(String[] argv) {
        System.err.println("\nRegression test of " + "serialization/deserialization of " + "complex objects\n");
        FileInputStream istream = null;
        try {
            Thirdpsio objout = new Thirdpsio();
            objout.init();
            FileOutputStream ostream = new FileOutputStream("psiotest1.tmp");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(objout);
            p.flush();
            ostream.close();
            istream = new FileInputStream("psiotest1.tmp");
            ObjectInputStream q = new ObjectInputStream(istream);
            Thirdpsio objin = (Thirdpsio) q.readObject();
            if (!objout.equals(objin)) {
                System.err.println("\nTEST FAILED: Original and read objects not equal.");
                throw new Error();
            }
            istream.close();
            System.err.println("\nTEST PASSED");
        } catch (Exception e) {
            System.err.print("TEST FAILED: ");
            e.printStackTrace();
            System.err.println("\nInput remaining");
            int ch;
            try {
                while ((ch = istream.read()) != -1) {
                    System.out.print(Integer.toString(ch, 16) + " ");
                }
                System.err.println("\n");
            } catch (Exception f) {
                throw new Error();
            }
            throw new Error();
        }
    }
}

class Firstpsio implements java.io.Serializable {

    String one;

    int two;

    float[] three;

    void init() {
        one = "one";
        two = 2;
        three = new float[5];
        float f = 3.0f;
        for (int i = 0; i < 5; i++) {
            f += 0.11;
            three[i] = f;
        }
    }

    boolean equals(Firstpsio other) {
        boolean ret = true;
        if (!one.equals(other.one)) {
            System.err.println("\nfirstpsio: expected " + one + " actual " + other.one);
            ret = false;
        }
        if (two != other.two) {
            System.err.println("\nfirstpsio: expected " + two + " actual " + other.two);
            ret = false;
        }
        for (int i = 0; i < three.length; i++) {
            if (three[i] != other.three[i]) {
                System.err.println("\nfirstpsio: three[" + i + "] expected " + three[i] + " actual " + other.three[i]);
                ret = false;
            }
        }
        return ret;
    }
}

class Secondpsio extends Firstpsio {

    String quatre;

    int cinq;

    private void writeObject(ObjectOutputStream pw) throws IOException {
        pw.writeObject(quatre);
        pw.writeInt(cinq);
    }

    private void readObject(ObjectInputStream pr) throws StreamCorruptedException, IOException, ClassNotFoundException {
        if (one == null) {
            System.err.println("\nTEST FAILED: Superclass not serialized when " + "it should have been");
            throw new StreamCorruptedException("Superclass not serialized " + "when it should have been");
        }
        quatre = (String) pr.readObject();
        cinq = pr.readInt();
    }

    boolean equals(Secondpsio other) {
        boolean ret = super.equals(other);
        if (!quatre.equals(other.quatre)) {
            System.err.println("\nsecondpsio: quatre expected " + quatre + " actual " + other.quatre);
            ret = false;
        }
        if (cinq != other.cinq) {
            System.err.println("\nsecondpsio: cinq expected " + cinq + " actual " + other.cinq);
            ret = false;
        }
        return ret;
    }

    void init() {
        quatre = "4444";
        cinq = 5;
        super.init();
    }
}

class Thirdpsio extends Secondpsio {

    static String ign = "ignored";

    transient Object oh;

    int six;

    private static int[] seven;

    protected byte eight = (byte) 9;

    static final byte dcare = (byte) 128;

    private short nine = 8888;

    long ten;

    java.util.Enumeration zero;

    boolean equals(Thirdpsio other) {
        boolean ret = super.equals(other);
        if (six != other.six) {
            System.err.println("\nthirdpsio six " + six + " actual " + other.six);
            ret = false;
        }
        if (eight != other.eight) {
            System.err.println("\nthirdpsio eight - expected " + eight + " actual " + other.eight);
            ret = false;
        }
        if (nine != other.nine) {
            System.err.println("\nthirdpsio nine - expected " + nine + " actual " + other.nine);
            ret = false;
        }
        if (ten != other.ten) {
            System.err.println("\nthirdpsio ten - expected " + ten + " actual " + other.ten);
            ret = false;
        }
        if (zero != other.zero) {
            System.err.println("\nthirdpsio zero - expected " + zero + " actual " + other.zero);
            ret = false;
        }
        return ret;
    }

    void init() {
        six = 666;
        int[] s7 = { 7, 7, 7 };
        seven = s7;
        eight = (byte) 8;
        nine = (short) 9;
        ten = (long) 100000;
        java.util.Enumeration em = null;
        super.init();
    }
}
