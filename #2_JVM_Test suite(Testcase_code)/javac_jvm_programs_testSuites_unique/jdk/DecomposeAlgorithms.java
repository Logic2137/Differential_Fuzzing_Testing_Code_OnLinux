



import sun.security.util.AlgorithmDecomposer;
import java.util.Set;

public class DecomposeAlgorithms {

    public static void main(String[] args) throws Exception {
        AlgorithmDecomposer decomposer = new AlgorithmDecomposer();

        check(decomposer, "AES/CBC/NoPadding", new String[] {
                "AES", "CBC", "NoPadding"});
        check(decomposer, "DES/CBC/PKCS5Padding", new String[] {
                "DES", "CBC", "PKCS5Padding"});
        check(decomposer, "RSA/ECB/OAEPWithSHA-1AndMGF1Padding", new String[] {
                "RSA", "ECB", "OAEP", "SHA1", "SHA-1", "MGF1Padding"});
        check(decomposer, "OAEPWithSHA-512AndMGF1Padding", new String[] {
                "OAEP", "SHA512", "SHA-512", "MGF1Padding"});
        check(decomposer, "OAEPWithSHA-512AndMGF1Padding", new String[] {
                "OAEP", "SHA512", "SHA-512", "MGF1Padding"});
        check(decomposer, "PBEWithSHA1AndRC2_40", new String[] {
                "PBE", "SHA1", "SHA-1", "RC2_40"});
        check(decomposer, "PBEWithHmacSHA224AndAES_128", new String[] {
                "PBE", "HmacSHA224", "AES_128"});
    }

    private static void check(AlgorithmDecomposer parser,
            String fullAlgName, String[] components) throws Exception {

        Set<String> parsed = parser.decompose(fullAlgName);
        if (parsed.size() != components.length) {
            throw new Exception("Not expected components number: " + parsed);
        }

        for (String component : components) {
            if (!parsed.contains(component)) {
                throw new Exception("Not a expected component: " + component);
            }
        }

        System.out.println("OK: " + fullAlgName);
    }
}
