import java.awt.image.RGBImageFilter;

public class HeadlessRGBImageFilter {

    public static void main(String[] args) {
        new RGBImageFilter() {

            public int filterRGB(int x, int y, int rgb) {
                return 0;
            }
        }.clone();
    }
}
