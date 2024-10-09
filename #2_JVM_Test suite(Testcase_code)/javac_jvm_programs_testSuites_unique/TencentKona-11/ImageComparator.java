
package org.netbeans.jemmy.image;

import java.awt.image.BufferedImage;


public interface ImageComparator {

    
    public boolean compare(BufferedImage image1, BufferedImage image2);
}
