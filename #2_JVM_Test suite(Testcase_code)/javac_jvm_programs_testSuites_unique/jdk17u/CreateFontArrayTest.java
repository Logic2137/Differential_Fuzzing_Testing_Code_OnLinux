import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CreateFontArrayTest {

    public static void main(String[] args) throws Exception {
        test(".ttc", 2, -1, true);
        test(".ttf", 1, 1, true);
        test(".otf", 1, 1, true);
        test(".pfa", 0, 0, false);
        test(".pfb", 0, 0, false);
    }

    static File getPlatformFontFolder(String ext) throws Exception {
        boolean recurse = false;
        String folder = null;
        String os = System.getProperty("os.name");
        if (os.startsWith("Win")) {
            folder = "c:\\windows\\fonts";
        } else if (os.startsWith("Linux")) {
            folder = "/usr/share/fonts";
            recurse = true;
        } else if (os.startsWith("Mac")) {
        }
        if (folder == null) {
            return null;
        }
        File dir = new File(folder);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        if (!recurse) {
            return dir;
        }
        return findSubFolder(dir, ext);
    }

    static File findSubFolder(File folder, String ext) {
        File[] files = folder.listFiles(f -> f.getName().toLowerCase().endsWith(ext));
        if (files != null && files.length > 0) {
            return folder;
        }
        File[] subdirs = folder.listFiles(f -> f.isDirectory());
        for (File f : subdirs) {
            File subfolder = findSubFolder(f, ext);
            if (subfolder != null) {
                return subfolder;
            }
        }
        return null;
    }

    static void test(String ext, int min, int max, boolean expectSuccess) throws Exception {
        File dir = getPlatformFontFolder(ext);
        if (dir == null) {
            System.out.println("No folder to test for " + ext);
            return;
        }
        File[] files = dir.listFiles(f -> f.getName().toLowerCase().endsWith(ext));
        if (files == null || files.length == 0) {
            System.out.println("No files to test for " + ext);
            return;
        }
        System.out.println("Create from file " + files[0]);
        Font[] fonts = null;
        try {
            fonts = Font.createFonts(files[0]);
            System.out.println("createFont from file returned " + fonts);
        } catch (Exception e) {
            if (expectSuccess) {
                throw new RuntimeException("Unexpected exception", e);
            } else {
                System.out.println("Got expected exception " + e);
                return;
            }
        }
        for (Font f : fonts) {
            System.out.println(ext + " component : " + f);
        }
        if (fonts.length < min) {
            throw new RuntimeException("Expected at least " + min + " but got " + fonts.length);
        }
        if (max > 0 && fonts.length > max) {
            throw new RuntimeException("Expected no more than " + max + " but got " + fonts.length);
        }
        FileInputStream fis = null;
        try {
            System.out.println("Create from stream " + files[0]);
            fis = new FileInputStream(files[0]);
            InputStream s = new BufferedInputStream(fis);
            fonts = null;
            try {
                fonts = Font.createFonts(s);
                System.out.println("createFont from stream returned " + fonts);
            } catch (Exception e) {
                if (expectSuccess) {
                    throw new RuntimeException("Unexpected exception", e);
                } else {
                    System.out.println("Got expected exception " + e);
                    return;
                }
            }
            for (Font f : fonts) {
                System.out.println(ext + " component : " + f);
            }
            if (fonts.length < min) {
                throw new RuntimeException("Expected at least " + min + " but got " + fonts.length);
            }
            if (max > 0 && fonts.length > max) {
                throw new RuntimeException("Expected no more than " + max + " but got " + fonts.length);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
