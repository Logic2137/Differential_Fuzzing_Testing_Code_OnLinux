

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.List;


public class TestSHAOids {

    private static final String PROVIDER_NAME = "SUN";
    private static final byte[] INPUT = "1234567890".getBytes();

    private static final List<DataTuple> DATA = Arrays.asList(
            new DataTuple("2.16.840.1.101.3.4.2.1", "SHA-256"),
            new DataTuple("2.16.840.1.101.3.4.2.2", "SHA-384"),
            new DataTuple("2.16.840.1.101.3.4.2.3", "SHA-512"),
            new DataTuple("2.16.840.1.101.3.4.2.4", "SHA-224"));

    public static void main(String[] args) throws Exception {
        for (DataTuple dataTuple : DATA) {
            runTest(dataTuple);
            System.out.println("passed");
        }
        System.out.println("All tests passed");
    }

    private static void runTest(DataTuple dataTuple)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        MessageDigest mdAlgorithm = MessageDigest.getInstance(
                dataTuple.algorithm, PROVIDER_NAME);
        MessageDigest mdOid = MessageDigest.getInstance(dataTuple.oid,
                PROVIDER_NAME);

        if (mdAlgorithm == null) {
            throw new RuntimeException(String.format(
                    "Test failed: algorithm string %s getInstance failed.%n",
                    dataTuple.algorithm));
        }

        if (mdOid == null) {
            throw new RuntimeException(
                    String.format("Test failed: OID %s getInstance failed.%n",
                            dataTuple.oid));
        }

        if (!mdAlgorithm.getAlgorithm().equals(dataTuple.algorithm)) {
            throw new RuntimeException(String.format(
                    "Test failed: algorithm string %s getInstance doesn't "
                            + "generate expected algorithm.%n",
                    dataTuple.algorithm));
        }

        mdAlgorithm.update(INPUT);
        mdOid.update(INPUT);

        
        if (!Arrays.equals(mdAlgorithm.digest(), mdOid.digest())) {
            throw new RuntimeException("Digest comparison failed: "
                    + "the two digests are not the same");
        }
    }

    private static class DataTuple {

        private final String oid;
        private final String algorithm;

        private DataTuple(String oid, String algorithm) {
            this.oid = oid;
            this.algorithm = algorithm;
        }
    }
}
