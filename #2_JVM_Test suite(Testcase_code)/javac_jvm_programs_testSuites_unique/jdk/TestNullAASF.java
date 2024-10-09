

import java.awt.image.AreaAveragingScaleFilter;

public class TestNullAASF {
    public static void main(String[] args) {
        AreaAveragingScaleFilter filter = null;
        try {
            filter = new AreaAveragingScaleFilter(0, Integer.MAX_VALUE);
            System.out.println("result: false");
            throw new RuntimeException("IAE expected for width=0");
        } catch (IllegalArgumentException e) {
            System.out.println("result (iae): true");
        }
        try {
            filter = new AreaAveragingScaleFilter(Integer.MAX_VALUE, 0);
            System.out.println("result: false");
            throw new RuntimeException("IAE expected for height=0");
        } catch (IllegalArgumentException e) {
            System.out.println("result (iae): true");
        }
    }
}
