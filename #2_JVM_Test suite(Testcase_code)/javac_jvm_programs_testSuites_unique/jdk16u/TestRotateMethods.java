



import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TestRotateMethods {
    
    public static final double MAX_ULPS = 3.0;
    public static final double MAX_ANCHOR_TX_ULPS = 1024.0;
    public static double MAX_TX_ULPS = MAX_ULPS;

    
    public static final double quadxvec[] = {  1.0,  0.0, -1.0,  0.0 };
    public static final double quadyvec[] = {  0.0,  1.0,  0.0, -1.0 };

    
    
    
    
    public static enum Mode { GET, SET, MOD };

    
    public static double maxulps = 0.0;
    public static double maxtxulps = 0.0;

    
    public static Point2D zeropt = new Point2D.Double(0, 0);
    public static Point2D testtxpts[] = {
        new Point2D.Double(       5,      5),
        new Point2D.Double(      20,    -10),
        new Point2D.Double(-Math.PI, Math.E),
    };

    public static void main(String argv[]) {
        test(Mode.GET);
        test(Mode.SET);
        test(Mode.MOD);

        System.out.println("Max scale and shear difference: "+maxulps+" ulps");
        System.out.println("Max translate difference: "+maxtxulps+" ulps");
    }

    public static void test(Mode mode) {
        MAX_TX_ULPS = MAX_ULPS; 
        test(mode, 0.5, null);
        test(mode, 1.0, null);
        test(mode, 3.0, null);

        
        MAX_TX_ULPS = MAX_ANCHOR_TX_ULPS;
        for (int i = 0; i < testtxpts.length; i++) {
            test(mode, 1.0, testtxpts[i]);
        }
        MAX_TX_ULPS = MAX_ULPS; 
    }

    public static void verify(AffineTransform at1, AffineTransform at2,
                              Mode mode, double vectorscale, Point2D txpt,
                              String message, double num, String units)
    {
        if (!compare(at1, at2)) {
            System.out.println("mode == "+mode);
            System.out.println("vectorscale == "+vectorscale);
            System.out.println("txpt == "+txpt);
            System.out.println(at1+", type = "+at1.getType());
            System.out.println(at2+", type = "+at2.getType());
            System.out.println("ScaleX values differ by "+
                               ulps(at1.getScaleX(), at2.getScaleX())+" ulps");
            System.out.println("ScaleY values differ by "+
                               ulps(at1.getScaleY(), at2.getScaleY())+" ulps");
            System.out.println("ShearX values differ by "+
                               ulps(at1.getShearX(), at2.getShearX())+" ulps");
            System.out.println("ShearY values differ by "+
                               ulps(at1.getShearY(), at2.getShearY())+" ulps");
            System.out.println("TranslateX values differ by "+
                               ulps(at1.getTranslateX(),
                                    at2.getTranslateX())+" ulps");
            System.out.println("TranslateY values differ by "+
                               ulps(at1.getTranslateY(),
                                    at2.getTranslateY())+" ulps");
            throw new RuntimeException(message + num + units);
        }
    }

    public static void test(Mode mode, double vectorscale, Point2D txpt) {
        AffineTransform at1, at2, at3;

        for (int deg = -720; deg <= 720; deg++) {
            if ((deg % 90) == 0) continue;
            double radians = Math.toRadians(deg);
            double vecy = Math.sin(radians) * vectorscale;
            double vecx = Math.cos(radians) * vectorscale;

            at1 = makeAT(mode, txpt, radians);
            at2 = makeAT(mode, txpt, vecx, vecy);
            verify(at1, at2, mode, vectorscale, txpt,
                   "vector and radians do not match for ", deg, " degrees");

            if (txpt == null) {
                
                if (vectorscale == 1.0) {
                    
                    at3 = makeAT(mode, zeropt, radians);
                    verify(at1, at3, mode, vectorscale, zeropt,
                           "radians not invariant with 0,0 translate at ",
                           deg, " degrees");
                }
                
                at3 = makeAT(mode, zeropt, vecx, vecy);
                verify(at2, at3, mode, vectorscale, zeropt,
                       "vector not invariant with 0,0 translate at ",
                       deg, " degrees");
            }
        }

        for (int quad = -8; quad <= 8; quad++) {
            double degrees = quad * 90.0;
            double radians = Math.toRadians(degrees);
            double vecx = quadxvec[quad & 3] * vectorscale;
            double vecy = quadyvec[quad & 3] * vectorscale;

            at1 = makeAT(mode, txpt, radians);
            at2 = makeAT(mode, txpt, vecx, vecy);
            verify(at1, at2, mode, vectorscale, txpt,
                   "quadrant vector and radians do not match for ",
                   degrees, " degrees");
            at2 = makeQuadAT(mode, txpt, quad);
            verify(at1, at2, mode, vectorscale, txpt,
                   "quadrant and radians do not match for ",
                   quad, " quadrants");
            if (txpt == null) {
                at3 = makeQuadAT(mode, zeropt, quad);
                verify(at2, at3, mode, vectorscale, zeropt,
                       "quadrant not invariant with 0,0 translate at ",
                       quad, " quadrants");
            }
        }
    }

    public static AffineTransform makeRandomAT() {
        AffineTransform at = new AffineTransform();
        at.scale(Math.random() * -10.0, Math.random() * 100.0);
        at.rotate(Math.random() * Math.PI);
        at.shear(Math.random(), Math.random());
        at.translate(Math.random() * 300.0, Math.random() * -20.0);
        return at;
    }

    public static AffineTransform makeAT(Mode mode, Point2D txpt,
                                         double radians)
    {
        AffineTransform at;
        double tx = (txpt == null) ? 0.0 : txpt.getX();
        double ty = (txpt == null) ? 0.0 : txpt.getY();
        switch (mode) {
        case GET:
            if (txpt != null) {
                at = AffineTransform.getRotateInstance(radians, tx, ty);
            } else {
                at = AffineTransform.getRotateInstance(radians);
            }
            break;
        case SET:
            at = makeRandomAT();
            if (txpt != null) {
                at.setToRotation(radians, tx, ty);
            } else {
                at.setToRotation(radians);
            }
            break;
        case MOD:
            at = makeRandomAT();
            at.setToIdentity();
            if (txpt != null) {
                at.rotate(radians, tx, ty);
            } else {
                at.rotate(radians);
            }
            break;
        default:
            throw new InternalError("unrecognized mode: "+mode);
        }

        return at;
    }

    public static AffineTransform makeAT(Mode mode, Point2D txpt,
                                         double vx, double vy)
    {
        AffineTransform at;
        double tx = (txpt == null) ? 0.0 : txpt.getX();
        double ty = (txpt == null) ? 0.0 : txpt.getY();
        switch (mode) {
        case GET:
            if (txpt != null) {
                at = AffineTransform.getRotateInstance(vx, vy, tx, ty);
            } else {
                at = AffineTransform.getRotateInstance(vx, vy);
            }
            break;
        case SET:
            at = makeRandomAT();
            if (txpt != null) {
                at.setToRotation(vx, vy, tx, ty);
            } else {
                at.setToRotation(vx, vy);
            }
            break;
        case MOD:
            at = makeRandomAT();
            at.setToIdentity();
            if (txpt != null) {
                at.rotate(vx, vy, tx, ty);
            } else {
                at.rotate(vx, vy);
            }
            break;
        default:
            throw new InternalError("unrecognized mode: "+mode);
        }

        return at;
    }

    public static AffineTransform makeQuadAT(Mode mode, Point2D txpt,
                                             int quads)
    {
        AffineTransform at;
        double tx = (txpt == null) ? 0.0 : txpt.getX();
        double ty = (txpt == null) ? 0.0 : txpt.getY();
        switch (mode) {
        case GET:
            if (txpt != null) {
                at = AffineTransform.getQuadrantRotateInstance(quads, tx, ty);
            } else {
                at = AffineTransform.getQuadrantRotateInstance(quads);
            }
            break;
        case SET:
            at = makeRandomAT();
            if (txpt != null) {
                at.setToQuadrantRotation(quads, tx, ty);
            } else {
                at.setToQuadrantRotation(quads);
            }
            break;
        case MOD:
            at = makeRandomAT();
            at.setToIdentity();
            if (txpt != null) {
                at.quadrantRotate(quads, tx, ty);
            } else {
                at.quadrantRotate(quads);
            }
            break;
        default:
            throw new InternalError("unrecognized mode: "+mode);
        }

        return at;
    }

    public static boolean compare(AffineTransform at1, AffineTransform at2) {
        maxulps = Math.max(maxulps, ulps(at1.getScaleX(), at2.getScaleX()));
        maxulps = Math.max(maxulps, ulps(at1.getScaleY(), at2.getScaleY()));
        maxulps = Math.max(maxulps, ulps(at1.getShearX(), at2.getShearX()));
        maxulps = Math.max(maxulps, ulps(at1.getShearY(), at2.getShearY()));
        maxtxulps = Math.max(maxtxulps,
                             ulps(at1.getTranslateX(), at2.getTranslateX()));
        maxtxulps = Math.max(maxtxulps,
                             ulps(at1.getTranslateY(), at2.getTranslateY()));
        return (getModifiedType(at1) == getModifiedType(at2) &&
                (compare(at1.getScaleX(), at2.getScaleX(), MAX_ULPS)) &&
                (compare(at1.getScaleY(), at2.getScaleY(), MAX_ULPS)) &&
                (compare(at1.getShearX(), at2.getShearX(), MAX_ULPS)) &&
                (compare(at1.getShearY(), at2.getShearY(), MAX_ULPS)) &&
                (compare(at1.getTranslateX(),
                         at2.getTranslateX(), MAX_TX_ULPS)) &&
                (compare(at1.getTranslateY(),
                         at2.getTranslateY(), MAX_TX_ULPS)));
    }

    public static int getModifiedType(AffineTransform at) {
        int type = at.getType();
        
        
        if ((type & AffineTransform.TYPE_UNIFORM_SCALE) != 0) {
            maxulps = Math.max(maxulps, ulps(at.getDeterminant(), 1.0));
            if (ulps(at.getDeterminant(), 1.0) <= MAX_ULPS) {
                
                type &= (~AffineTransform.TYPE_UNIFORM_SCALE);
            }
        }
        return type;
    }

    public static boolean compare(double val1, double val2, double maxulps) {
        return (ulps(val1, val2) <= maxulps);
    }

    public static double ulps(double val1, double val2) {
        double diff = Math.abs(val1 - val2);
        double ulpmax = Math.min(Math.ulp(val1), Math.ulp(val2));
        return (diff / ulpmax);
    }
}
