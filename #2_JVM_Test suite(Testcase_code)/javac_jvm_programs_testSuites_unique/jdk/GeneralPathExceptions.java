

import java.awt.geom.GeneralPath;

import static java.awt.geom.Path2D.WIND_EVEN_ODD;
import static java.awt.geom.Path2D.WIND_NON_ZERO;


public final class GeneralPathExceptions {

    public static void main(String[] args) {
        try {
            new GeneralPath(null);
            throw new RuntimeException("NullPointerException is expected");
        } catch (NullPointerException ignore) {
            
        }

        try {
            new GeneralPath(-1);
            throw new RuntimeException("IllegalArgumentException is expected");
        } catch (IllegalArgumentException ignore) {
            
        }
        try {
            new GeneralPath(-1, 0);
            throw new RuntimeException("IllegalArgumentException is expected");
        } catch (IllegalArgumentException ignore) {
            
        }

        try {
            new GeneralPath(WIND_EVEN_ODD, -1);
            throw new RuntimeException("NegativeArraySizeException is expected");
        } catch (NegativeArraySizeException ignore) {
            
        }
        try {
            new GeneralPath(WIND_NON_ZERO, -1);
            throw new RuntimeException("NegativeArraySizeException is expected");
        } catch (NegativeArraySizeException ignore) {
            
        }
    }
}
