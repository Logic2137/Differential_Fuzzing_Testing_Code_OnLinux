



import javax.imageio.spi.IIOServiceProvider;

import com.sun.imageio.spi.FileImageInputStreamSpi;
import com.sun.imageio.spi.FileImageOutputStreamSpi;
import com.sun.imageio.spi.InputStreamImageInputStreamSpi;
import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;
import com.sun.imageio.spi.RAFImageInputStreamSpi;
import com.sun.imageio.spi.RAFImageOutputStreamSpi;

public class SpiVersionNumbers {

    private static void check(IIOServiceProvider spi) {
        String version = spi.getVersion();
        if (!version.equals("1.0")) {
            throw new RuntimeException("Provider " +
                                       spi.getClass().getName() +
                                       " has version " + version + "!");
        }
    }

    public static void main(String[] args) {
        check(new FileImageInputStreamSpi());
        check(new InputStreamImageInputStreamSpi());
        check(new RAFImageInputStreamSpi());

        check(new FileImageOutputStreamSpi());
        check(new OutputStreamImageOutputStreamSpi());
        check(new RAFImageOutputStreamSpi());
    }

}
