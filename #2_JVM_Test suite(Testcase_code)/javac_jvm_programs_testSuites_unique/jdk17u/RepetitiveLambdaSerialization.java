import java.io.*;

public class RepetitiveLambdaSerialization {

    static final int REPS = 20;

    public static void main(String[] args) throws Exception {
        LSI ls = z -> "[" + z + "]";
        for (int i = 0; i < REPS; ++i) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(baos);
            out.writeObject(ls);
            out.flush();
            out.close();
        }
        System.out.println("Passed.");
    }
}

interface LSI extends Serializable {

    String convert(String x);
}
