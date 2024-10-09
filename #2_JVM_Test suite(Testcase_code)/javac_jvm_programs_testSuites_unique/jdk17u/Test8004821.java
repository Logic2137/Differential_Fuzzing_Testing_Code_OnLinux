import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public final class Test8004821 {

    public static void main(final String[] args) {
        final int[] arrEmpty = {};
        final int[] arr1elem = { 150 };
        final BufferedImage bi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = (Graphics2D) bi.getGraphics();
        test(g, arrEmpty);
        test(g, arr1elem);
        g.translate(2.0, 2.0);
        test(g, arrEmpty);
        test(g, arr1elem);
        g.scale(2.0, 2.0);
        test(g, arrEmpty);
        test(g, arr1elem);
        g.dispose();
    }

    private static void test(final Graphics2D g, final int[] arr) {
        g.drawPolygon(arr, arr, arr.length);
        g.drawPolygon(new Polygon(arr, arr, arr.length));
        g.fillPolygon(arr, arr, arr.length);
        g.fillPolygon(new Polygon(arr, arr, arr.length));
        g.drawPolyline(arr, arr, arr.length);
    }
}
