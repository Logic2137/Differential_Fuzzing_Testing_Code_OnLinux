import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class NPETests {

    static boolean verbose;

    static int numfailures = 0;

    public static void main(String[] argv) {
        verbose = (argv.length > 0);
        test(new Runnable() {

            public void run() {
                new Area(null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().add(null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().subtract(null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().intersect(null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().exclusiveOr(null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().transform(null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().createTransformedArea(null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().contains((Point2D) null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().contains((Rectangle2D) null);
            }
        });
        test(new Runnable() {

            public void run() {
                new Area().intersects((Rectangle2D) null);
            }
        });
        if (numfailures > 0) {
            throw new RuntimeException(numfailures + " methods failed to throw NPE");
        }
    }

    public static void test(Runnable r) {
        try {
            r.run();
            numfailures++;
            if (verbose) {
                new RuntimeException(r + " failed to throw NPE").printStackTrace();
            }
        } catch (NullPointerException e) {
        }
    }
}
