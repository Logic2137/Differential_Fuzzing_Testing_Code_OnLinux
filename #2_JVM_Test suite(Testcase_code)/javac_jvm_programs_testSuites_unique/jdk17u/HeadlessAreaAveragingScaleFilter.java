import java.awt.image.AreaAveragingScaleFilter;

public class HeadlessAreaAveragingScaleFilter {

    public static void main(String[] args) {
        new AreaAveragingScaleFilter(100, 100).clone();
    }
}
