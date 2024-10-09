



import java.awt.geom.GeneralPath;

public class IntersectsRect {
    public static void main(String args[]) {
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        gp.moveTo(-12.820351600646973f, 22.158836364746094f);
        gp.quadTo(-26.008909225463867f, 83.72308349609375f,
                  84.20527648925781f, 13.218562126159668f);
        gp.quadTo(107.0041275024414f, 38.3076171875f,
                  -55.382022857666016f, -113.43235778808594f);
        gp.lineTo(-43.795501708984375f, 52.847373962402344f);
        gp.curveTo(37.72114944458008f, 70.46839141845703f,
                   -26.205299377441406f, -103.99849700927734f,
                   108.40007781982422f, 101.23545837402344f);
        gp.closePath();

        if (gp.intersects(34.614093600596874, 22.15252370704289,
                          0.5, 0.5) != false)
        {
            throw new RuntimeException("intersects rect clearly "+
                                       "outside of path");
        }
    }
}
