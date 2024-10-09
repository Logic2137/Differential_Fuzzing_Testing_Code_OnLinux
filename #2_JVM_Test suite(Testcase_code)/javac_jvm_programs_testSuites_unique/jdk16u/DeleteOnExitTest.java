

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class DeleteOnExitTest {
    public static void main(String[] args) throws IOException {
        ByteArrayInputStream is =
            new ByteArrayInputStream(new byte[100]);
        ByteArrayOutputStream os =
            new ByteArrayOutputStream();

        String tmp = System.getProperty("java.io.tmpdir", ".");
        System.out.println("tmp: " + tmp);

        
        ImageIO.setUseCache(true);
        ImageIO.setCacheDirectory(new File(tmp));

        File tmpDir = ImageIO.getCacheDirectory();
        System.out.println("tmpDir is " + tmpDir);
        int fnum_before = tmpDir.list().length;
        System.out.println("Files before test: " + fnum_before);

        ImageInputStream iis =
            ImageIO.createImageInputStream(is);
        System.out.println("iis = " + iis);

        ImageInputStream iis2 =
            ImageIO.createImageInputStream(is);

        ImageOutputStream ios =
            ImageIO.createImageOutputStream(os);
        System.out.println("ios = " + ios);

        ImageOutputStream ios2 =
            ImageIO.createImageOutputStream(os);

        iis2.close();
        ios2.close();
        int fnum_after = tmpDir.list().length;
        System.out.println("Files after test: " + fnum_after);

        if (fnum_before == fnum_after) {
            throw new RuntimeException("Test failed: cache was not used.");
        }
    }
}
