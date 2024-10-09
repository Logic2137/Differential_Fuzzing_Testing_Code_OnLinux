



import java.awt.geom.GeneralPath;

public class ContainsNaN {
    public static void main(String argv[]) {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.quadTo(100, 0, 100, 100);
        gp.contains(Double.NaN, Double.NaN);
    }
}
