



import javax.imageio.ImageIO;

public class ImageIOGetImageReaders {

    public static void main(String[] args) {
        boolean gotIAE = false;
        try {
            ImageIO.getImageReaders(null);
        } catch (IllegalArgumentException e) {
            gotIAE = true;
        }

        if (!gotIAE) {
            throw new RuntimeException("Failed to get IAE!");
        }
    }
}
