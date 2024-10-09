



import javax.imageio.stream.FileCacheImageInputStream;

public class FileCacheImageInputStreamNullTest {

    public static void main (String[] args) throws Exception {
        boolean gotIAE = false;
        try {
            FileCacheImageInputStream fciis =
                new FileCacheImageInputStream(null, null);
        } catch (IllegalArgumentException e) {
            gotIAE = true;
        }

        if (!gotIAE) {
            throw new RuntimeException
                ("Failed to get IllegalArgumentException!");
        }
    }
}
