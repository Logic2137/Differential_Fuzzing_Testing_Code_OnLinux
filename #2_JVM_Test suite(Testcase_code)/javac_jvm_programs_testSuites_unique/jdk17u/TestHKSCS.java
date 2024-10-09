import java.util.Arrays;

public class TestHKSCS {

    public static void main(String[] args) throws Exception {
        String[] charsets = { "x-MS950-HKSCS-XP", "x-MS950-HKSCS", "Big5-HKSCS", "x-Big5-HKSCS-2001" };
        String s = "\ufffd\ud87f\udffd";
        byte[] bytes = new byte[] { 0x3f, 0x3f };
        for (String cs : charsets) {
            if (!Arrays.equals(bytes, s.getBytes(cs))) {
                throw new RuntimeException(cs + " failed to decode u+fffd");
            }
        }
    }
}
