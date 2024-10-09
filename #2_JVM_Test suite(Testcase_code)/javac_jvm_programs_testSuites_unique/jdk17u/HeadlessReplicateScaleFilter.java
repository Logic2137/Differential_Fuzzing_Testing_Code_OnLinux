import java.awt.image.ReplicateScaleFilter;

public class HeadlessReplicateScaleFilter {

    public static void main(String[] args) {
        new ReplicateScaleFilter(100, 100).clone();
    }
}
