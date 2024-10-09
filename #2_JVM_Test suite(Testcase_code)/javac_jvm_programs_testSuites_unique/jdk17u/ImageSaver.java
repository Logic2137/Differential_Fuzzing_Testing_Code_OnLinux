
package org.netbeans.jemmy.image;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageSaver {

    public void save(BufferedImage image, String fileName) throws IOException;
}
