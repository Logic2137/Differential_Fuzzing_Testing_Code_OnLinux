



import com.sun.imageio.plugins.png.PNGImageWriterSpi;

public class PNGSuffixes {

    public static void main(String[] args) {
        String[] suffixes = new PNGImageWriterSpi().getFileSuffixes();
        for (int i = 0; i < suffixes.length; i++) {
            if (suffixes[i].startsWith(".")) {
                throw new RuntimeException("Found a \".\" in a suffix!");
            }
        }
    }
}
