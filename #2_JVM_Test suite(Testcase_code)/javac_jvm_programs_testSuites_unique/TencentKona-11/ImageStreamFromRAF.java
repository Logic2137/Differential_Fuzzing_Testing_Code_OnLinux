



import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class ImageStreamFromRAF {

    public static void main(String[] args) {
        try {
            File f = new File("ImageInputStreamFromRAF.tmp");
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            ImageInputStream istream = ImageIO.createImageInputStream(raf);
            ImageOutputStream ostream = ImageIO.createImageOutputStream(raf);
            f.delete();
            if (istream == null) {
                throw new RuntimeException("ImageIO.createImageInputStream(RandomAccessFile) returned null!");
            }
            if (ostream == null) {
                throw new RuntimeException("ImageIO.createImageOutputStream(RandomAccessFile) returned null!");
            }
            if (!(istream instanceof FileImageInputStream)) {
                throw new RuntimeException("ImageIO.createImageInputStream(RandomAccessFile) did not return a FileImageInputStream!");
            }
            if (!(ostream instanceof FileImageOutputStream)) {
                throw new RuntimeException("ImageIO.createImageOutputStream(RandomAccessFile) did not return a FileImageOutputStream!");
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Unexpected IOException: " + ioe);
        }
    }
}
