



import java.awt.geom.CubicCurve2D;

public class ContainsHang {
    public static void main(String args[]) {
        CubicCurve2D curve =
            new CubicCurve2D.Double(83.0, 101.0,
                                    -5.3919918959078075, 94.23530547506019,
                                    71.0, 39.0,
                                    122.0, 44.0);

        System.out.println("Checking contains() >>>");
        curve.contains(71.0, 44.0);
        System.out.println("Check complete!!");
    }
}
