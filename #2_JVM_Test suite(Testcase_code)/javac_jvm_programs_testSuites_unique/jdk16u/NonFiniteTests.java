



import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

public class NonFiniteTests {
    public static final double DBL_NaN = Double.NaN;
    public static final double DBL_POS_INF = Double.POSITIVE_INFINITY;
    public static final double DBL_NEG_INF = Double.NEGATIVE_INFINITY;
    public static final double DBL_MAX = Double.MAX_VALUE;
    public static final double DBL_MIN = -Double.MAX_VALUE;
    public static final double FLT_MAX = Float.MAX_VALUE;
    public static final double FLT_MIN = -Float.MAX_VALUE;

    public static final int SEG_MOVETO = PathIterator.SEG_MOVETO;
    public static final int SEG_LINETO = PathIterator.SEG_LINETO;
    public static final int SEG_QUADTO = PathIterator.SEG_QUADTO;
    public static final int SEG_CUBICTO = PathIterator.SEG_CUBICTO;
    public static final int SEG_CLOSE = PathIterator.SEG_CLOSE;

    public static int types[] = {
        SEG_MOVETO,
        SEG_LINETO,
        SEG_QUADTO,
        SEG_CUBICTO,
        SEG_CLOSE,
    };

    public static double coords[] = {
        
          0.0,    0.0,

        
         50.0,   10.0,

        
        100.0,   20.0,
        100.0,  100.0,

        
         50.0,  150.0,
          0.0,  100.0,
        -50.0,   50.0,

        
    };

    public static double testpoints[] = {
               -100,        -100,
                  0,           0,
                 50,          50,
            DBL_NaN,     DBL_NaN,
        DBL_POS_INF, DBL_POS_INF,
        DBL_NEG_INF, DBL_NEG_INF,
        DBL_POS_INF, DBL_NEG_INF,
        DBL_NEG_INF, DBL_POS_INF,
    };

    public static double testrects[] = {
               -100,        -100,          10,          10,
                  0,           0,          10,          10,
                 50,          50,          10,          10,
            DBL_NaN,     DBL_NaN,          10,          10,
                 10,          10,     DBL_NaN,     DBL_NaN,
            DBL_NaN,     DBL_NaN,     DBL_NaN,     DBL_NaN,
                 10,          10, DBL_POS_INF, DBL_POS_INF,
                 10,          10, DBL_NEG_INF, DBL_NEG_INF,
                 10,          10, DBL_POS_INF, DBL_NEG_INF,
                 10,          10, DBL_NEG_INF, DBL_POS_INF,
        DBL_NEG_INF, DBL_NEG_INF, DBL_POS_INF, DBL_POS_INF,
        DBL_POS_INF, DBL_POS_INF,          10,          10,
        DBL_NEG_INF, DBL_NEG_INF,          10,          10,
        DBL_POS_INF, DBL_NEG_INF,          10,          10,
        DBL_NEG_INF, DBL_POS_INF,          10,          10,
    };

    public static double replacecoords[] = {
        DBL_NEG_INF,
        DBL_MIN,
        FLT_MIN,
        DBL_NaN,
        FLT_MAX,
        DBL_MAX,
        DBL_POS_INF,
    };

    public static void main(String argv[]) {
        test(types, coords);
        testNonFinites(types, coords, 2);
    }

    public static void testNonFinites(int types[], double coords[],
                                      int numvalues)
    {
        if (numvalues == 0) {
            test(types, coords);
            return;
        }
        numvalues--;
        for (int i = 0; i < coords.length; i++) {
            double savedval = coords[i];

            
            for (int j = 0; j < replacecoords.length; j++) {
                coords[i] = replacecoords[j];
                testNonFinites(types, coords, numvalues);
            }

            coords[i] = savedval;
        }
    }

    public static void test(int types[], double coords[]) {
        testGP(new GeneralPath(), types, coords);
        try {
            P2DTest.test(types, coords);
        } catch (NoClassDefFoundError e) {
            
        }
    }

    public static void testGP(GeneralPath gp, int types[], double coords[]) {
        int ci = 0;
        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
            case SEG_MOVETO:
                gp.moveTo((float) coords[ci++], (float) coords[ci++]);
                break;
            case SEG_LINETO:
                gp.lineTo((float) coords[ci++], (float) coords[ci++]);
                break;
            case SEG_QUADTO:
                gp.quadTo((float) coords[ci++], (float) coords[ci++],
                          (float) coords[ci++], (float) coords[ci++]);
                break;
            case SEG_CUBICTO:
                gp.curveTo((float) coords[ci++], (float) coords[ci++],
                           (float) coords[ci++], (float) coords[ci++],
                           (float) coords[ci++], (float) coords[ci++]);
                break;
            case SEG_CLOSE:
                gp.closePath();
                break;
            }
        }
        testGP(gp);
    }

    public static void testGP(GeneralPath gp) {
        for (int i = 0; i < testpoints.length; i += 2) {
            gp.contains(testpoints[i+0], testpoints[i+1]);
        }

        for (int i = 0; i < testrects.length; i += 4) {
            gp.contains(testrects[i+0], testrects[i+1],
                        testrects[i+2], testrects[i+3]);
            gp.intersects(testrects[i+0], testrects[i+1],
                          testrects[i+2], testrects[i+3]);
        }
    }

    public static class P2DTest {
        public static void test(int types[], double coords[]) {
            testPath(new Path2D.Float(), types, coords);
            testPath(new Path2D.Double(), types, coords);
        }

        public static void testPath(Path2D p2d, int types[], double coords[]) {
            int ci = 0;
            for (int i = 0; i < types.length; i++) {
                switch (types[i]) {
                case SEG_MOVETO:
                    p2d.moveTo(coords[ci++], coords[ci++]);
                    break;
                case SEG_LINETO:
                    p2d.lineTo(coords[ci++], coords[ci++]);
                    break;
                case SEG_QUADTO:
                    p2d.quadTo(coords[ci++], coords[ci++],
                               coords[ci++], coords[ci++]);
                    break;
                case SEG_CUBICTO:
                    p2d.curveTo(coords[ci++], coords[ci++],
                                coords[ci++], coords[ci++],
                                coords[ci++], coords[ci++]);
                    break;
                case SEG_CLOSE:
                    p2d.closePath();
                    break;
                }
            }
            testPath(p2d);
        }

        public static void testPath(Path2D p2d) {
            
            for (int i = 0; i < testpoints.length; i += 2) {
                p2d.contains(testpoints[i+0], testpoints[i+1]);
                contains(p2d, testpoints[i+0], testpoints[i+1]);
            }

            for (int i = 0; i < testrects.length; i += 4) {
                p2d.contains(testrects[i+0], testrects[i+1],
                             testrects[i+2], testrects[i+3]);
                contains(p2d,
                         testrects[i+0], testrects[i+1],
                         testrects[i+2], testrects[i+3]);
                p2d.intersects(testrects[i+0], testrects[i+1],
                               testrects[i+2], testrects[i+3]);
                intersects(p2d,
                           testrects[i+0], testrects[i+1],
                           testrects[i+2], testrects[i+3]);
            }
        }

        public static boolean contains(Path2D p2d, double x, double y) {
            return Path2D.contains(p2d.getPathIterator(null), x, y);
        }

        public static boolean contains(Path2D p2d,
                                       double x, double y, double w, double h)
        {
            return Path2D.contains(p2d.getPathIterator(null), x, y, w, h);
        }

        public static boolean intersects(Path2D p2d,
                                         double x, double y, double w, double h)
        {
            return Path2D.intersects(p2d.getPathIterator(null), x, y, w, h);
        }
    }
}
