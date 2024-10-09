import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;

public final class WriteProfileToFile {

    public static void main(String[] args) throws Exception {
        byte[] gold = ICC_Profile.getInstance(ColorSpace.CS_sRGB).getData();
        testViaDataArray(gold);
        testViaFile(gold);
        testViaStream(gold);
    }

    private static void testViaDataArray(byte[] gold) {
        ICC_Profile profile = ICC_Profile.getInstance(gold);
        compare(gold, profile.getData());
    }

    private static void testViaFile(byte[] gold) throws Exception {
        ICC_Profile profile = ICC_Profile.getInstance(gold);
        profile.write("fileName.icc");
        try {
            profile = ICC_Profile.getInstance("fileName.icc");
            compare(gold, profile.getData());
        } finally {
            Files.delete(new File("fileName.icc").toPath());
        }
    }

    private static void testViaStream(byte[] gold) throws Exception {
        ICC_Profile profile = ICC_Profile.getInstance(gold);
        File file = new File("fileName.icc");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            profile.write(outputStream);
            profile = ICC_Profile.getInstance("fileName.icc");
            compare(gold, profile.getData());
        } finally {
            Files.delete(file.toPath());
        }
    }

    private static void compare(byte[] data1, byte[] data2) {
        if (!Arrays.equals(data1, data2)) {
            throw new RuntimeException("Data mismatch");
        }
    }
}
