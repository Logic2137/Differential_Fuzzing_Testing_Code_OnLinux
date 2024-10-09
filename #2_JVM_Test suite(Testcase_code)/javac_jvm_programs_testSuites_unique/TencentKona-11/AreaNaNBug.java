



import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class AreaNaNBug {
    public static void main(String argv[]) {
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        gp.moveTo(-38.05311f, 75.25694f);
        gp.curveTo(-48.18971f, 71.23722f,
                   -46.495422f, -113.844574f,
                   124.16219f, 87.604744f);
        gp.quadTo(105.644554f, 114.52495f,
                  72.98282f, -78.52084f);
        gp.lineTo(107.358795f, 29.33548f);
        gp.quadTo(-16.562698f, -43.91586f,
                  51.50203f, 38.51295f);
        gp.lineTo(20.715876f, 44.4093f);
        gp.closePath();
        Area a = new Area(gp);
        Rectangle2D r2d = a.getBounds2D();
        if (Double.isNaN(r2d.getX()) ||
            Double.isNaN(r2d.getY()) ||
            Double.isNaN(r2d.getWidth()) ||
            Double.isNaN(r2d.getHeight()))
        {
            throw new RuntimeException("Area bounds have NaN");
        }
    }
}
