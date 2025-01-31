import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.IvParameterSpec;

public class ChaCha20Poly1305ParametersUnitTest {

    private static final String ALGORITHM = "ChaCha20-Poly1305";

    private static final byte[] NONCE = { 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8 };

    private static final byte[] PARAM = { 4, 12, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8 };

    private static final byte[] BAD_NONCE = { 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    private static final byte[] BAD_PARAM = { 4, 13, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    public static void main(String[] args) throws Exception {
        testInit();
        testGetParameterSpec();
        testGetEncoded();
    }

    private static void testInit() throws Exception {
        System.out.println("== init ==");
        AlgorithmParameters ap = AlgorithmParameters.getInstance(ALGORITHM);
        ap.init(new IvParameterSpec(NONCE));
        System.out.println("AlgorithmParameters: " + ap);
        ap = AlgorithmParameters.getInstance(ALGORITHM);
        ap.init(PARAM);
        ap = AlgorithmParameters.getInstance(ALGORITHM);
        try {
            ap.init(new ChaCha20ParameterSpec(NONCE, 0));
            throw new RuntimeException("IvParameterSpec is needed");
        } catch (InvalidParameterSpecException e) {
            System.out.println("Expected " + e);
        }
        ap = AlgorithmParameters.getInstance(ALGORITHM);
        try {
            ap.init(new IvParameterSpec(BAD_NONCE));
            throw new RuntimeException("Nonce must be 96 bits in length");
        } catch (InvalidParameterSpecException e) {
            System.out.println("Expected " + e);
        }
        ap = AlgorithmParameters.getInstance(ALGORITHM);
        try {
            ap.init(BAD_PARAM);
            throw new RuntimeException("Nonce must be 96 bits in length");
        } catch (IOException e) {
            System.out.println("Expected " + e);
        }
    }

    private static void testGetParameterSpec() throws Exception {
        System.out.println("== getParameterSpec ==");
        AlgorithmParameters ap = AlgorithmParameters.getInstance(ALGORITHM);
        ap.init(PARAM);
        IvParameterSpec paramSpec = ap.getParameterSpec(IvParameterSpec.class);
        byte[] nonce = paramSpec.getIV();
        System.out.println("Nonce: " + Arrays.toString(nonce));
        Arrays.equals(nonce, NONCE);
        try {
            ap.getParameterSpec(ChaCha20ParameterSpec.class);
            throw new RuntimeException("IvParameterSpec is needed");
        } catch (InvalidParameterSpecException e) {
            System.out.println("Expected " + e);
        }
    }

    private static void testGetEncoded() throws Exception {
        System.out.println("== getEncoded ==");
        AlgorithmParameters ap = AlgorithmParameters.getInstance(ALGORITHM);
        ap.init(PARAM);
        byte[] defaultFormatEncoded = ap.getEncoded();
        System.out.println("Default format encoding: " + Arrays.toString(defaultFormatEncoded));
        if (!Arrays.equals(defaultFormatEncoded, PARAM)) {
            throw new RuntimeException("Default format encoding failed");
        }
        byte[] asn1FormatEncoded = ap.getEncoded("ASN.1");
        System.out.println("ASN.1 format encoding: " + Arrays.toString(asn1FormatEncoded));
        if (!Arrays.equals(asn1FormatEncoded, PARAM)) {
            throw new RuntimeException("ASN.1 format encoding failed");
        }
        byte[] nullFormatEncoded = ap.getEncoded(null);
        System.out.println("Null format encoding: " + Arrays.toString(nullFormatEncoded));
        if (!Arrays.equals(nullFormatEncoded, PARAM)) {
            throw new RuntimeException("Null format encoding failed");
        }
        try {
            ap.getEncoded("BAD");
            throw new RuntimeException("Format must be ASN.1");
        } catch (IOException e) {
            System.out.println("Expected " + e);
        }
    }
}
