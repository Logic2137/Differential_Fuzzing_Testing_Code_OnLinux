



import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class IntersectsLineHang {
    static public void main(String args[]) {
        Rectangle r = new Rectangle(0x70000000, 0x70000000,
                                    0x20000000, 0x0f000000);
        double v = 0x78000000;
        System.out.println("Point alpha");
        boolean result = r.intersectsLine(v, v, v+v, v+v);
        System.out.println(result);
        Rectangle2D rect = new Rectangle2D.Float(29.790712356567383f,
                                                 362.3290710449219f,
                                                 267.40679931640625f,
                                                 267.4068298339844f);
        System.out.println("Point A");
        System.out.println(rect.intersectsLine(431.39777, 551.3534,
                                               26.391, 484.71542));
        System.out.println("Point A2");
        System.out.println(rect.intersectsLine(431.39777, 551.3534,
                                               268.391, 484.71542));
        System.out.println("Point B: Never gets here!");
        if (!result) {
            
            throw new RuntimeException("integer rectangle "+
                                       "failed intersection test");
        }
    }
}
