



import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;



public class AreaTransformTest {

    public static void main(String[] args) {
        AffineTransform t = AffineTransform.getRotateInstance(Math.PI/8);
        GeneralPath path = new GeneralPath();

        path.moveTo(-4516.23223633003f,10983.71557514126f);
        path.lineTo(-1451.4908513919768f, 13100.559659959084f);
        path.quadTo(-54.38163118565376f, 13679.261247085042f,
                    1470.6331984752403f, 13679.261247085042f);
        path.closePath();

        Area area = new Area(path);

        for (int i = 0; i < 8; i++) {
            area.transform(t);
        }
    }

}
