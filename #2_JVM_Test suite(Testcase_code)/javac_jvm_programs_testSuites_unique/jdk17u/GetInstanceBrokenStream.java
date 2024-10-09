import java.awt.color.ICC_Profile;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class GetInstanceBrokenStream {

    public static void main(String[] args) throws IOException {
        testHeader(new byte[] {});
        testHeader(new byte[] { -12, 3, 45 });
        testHeader(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0x61, 0x63, 0x73, 0x70 });
    }

    private static void testHeader(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try {
            ICC_Profile.getInstance(bais);
        } catch (IllegalArgumentException e) {
        }
    }
}
