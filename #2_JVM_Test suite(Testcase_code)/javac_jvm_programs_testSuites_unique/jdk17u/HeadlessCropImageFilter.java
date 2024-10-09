import java.awt.image.CropImageFilter;

public class HeadlessCropImageFilter {

    public static void main(String[] args) {
        new CropImageFilter(20, 20, 40, 50).clone();
    }
}
