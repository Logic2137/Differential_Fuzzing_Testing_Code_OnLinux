import java.awt.geom.Path2D;

public final class EmptyCapacity {

    public static void main(final String[] args) {
        final Path2D path1 = new Path2D.Double(Path2D.WIND_EVEN_ODD, 0);
        path1.moveTo(10, 10);
        path1.lineTo(20, 20);
        final Path2D path2 = new Path2D.Float(Path2D.WIND_EVEN_ODD, 0);
        path2.moveTo(10, 10);
        path2.lineTo(20, 20);
    }
}
