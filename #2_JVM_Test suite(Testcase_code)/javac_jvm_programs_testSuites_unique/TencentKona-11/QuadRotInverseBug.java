



import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;


public class QuadRotInverseBug {
    public static void main(String[] args) {
        
        
        System.out.println("Using 90 degree rotation:");
        AffineTransform xform = AffineTransform.getRotateInstance(Math.PI/2);
        boolean test1failed = test(xform);
        
        System.out.println("Using 90 degree rotation with translation:");
        xform.translate(2,2);
        boolean test2failed = test(xform);
        if (test1failed || test2failed) {
            throw new RuntimeException("test failed, see printout");
        }
    }

    public static boolean test(AffineTransform xform) {
        
        double[] originalPoint = new double[2];
        double[] transformedPoint = new double[2];
        double[] inverseFromOriginalXForm = new double[2];

        Point2D originalPoint2D = new Point2D.Double();
        Point2D transformedPoint2D = new Point2D.Double();
        Point2D inverseFromOriginalPoint2D = new Point2D.Double();

        
        originalPoint[0] = 1.;
        originalPoint[1] = 1.;

        try {

            originalPoint2D.setLocation(originalPoint[0], originalPoint[1]);

            
            xform.transform(originalPoint,0,transformedPoint,0,1);
            xform.transform(originalPoint2D, transformedPoint2D);

            
            xform.inverseTransform(transformedPoint,0,
                                   inverseFromOriginalXForm,0,1);
            xform.inverseTransform(transformedPoint2D,
                                   inverseFromOriginalPoint2D);
        } catch (NoninvertibleTransformException e) {
            throw new InternalError("transform wasn't invertible!");
        }

        System.out.println("Both points should be identical:");
        System.out.println("Original Point: "+
                           originalPoint[0]+" "+
                           originalPoint[1]);
        System.out.println("inverseTransform method used: "+
                           inverseFromOriginalXForm[0]+" "+
                           inverseFromOriginalXForm[1]);
        System.out.println("Original Point2D: "+ originalPoint2D);
        System.out.println("inverseTransform method used: "+
                           inverseFromOriginalPoint2D);
        return (originalPoint[0] != inverseFromOriginalXForm[0] ||
                originalPoint[1] != inverseFromOriginalXForm[1] ||
                !originalPoint2D.equals(inverseFromOriginalPoint2D));
    }
}
