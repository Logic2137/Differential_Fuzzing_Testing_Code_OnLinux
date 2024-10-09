



import java.awt.geom.Area;

public class EqualsNull {
    public static void main(String argv[]) {
        if (new Area().equals(null)) {
            throw new RuntimeException("Area object equaled null");
        }
    }
}
