
package org.netbeans.jemmy.image;

import java.awt.Point;
import java.awt.image.BufferedImage;


public interface ImageFinder {

    
    public Point findImage(BufferedImage image, int index);
}
