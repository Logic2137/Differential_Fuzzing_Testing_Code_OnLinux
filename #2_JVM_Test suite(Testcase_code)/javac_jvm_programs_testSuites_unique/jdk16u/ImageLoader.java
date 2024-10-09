
package org.netbeans.jemmy.image;

import java.awt.image.BufferedImage;
import java.io.IOException;


public interface ImageLoader {

    
    public BufferedImage load(String fileName) throws IOException;
}
