
import java.awt.font.OpenType;
import java.io.IOException;



public class OpticalBoundsTagTest {

    public static void main(String[] a) throws Exception {

        int tag_opbd = java.awt.font.OpenType.TAG_OPBD;
        if (tag_opbd == java.awt.font.OpenType.TAG_MORT) {
            System.out.println("Test failed: TAG_OPBD:" + tag_opbd);
            throw new RuntimeException("TAG_OPBD same as TAG_MORT");
        } else {
            System.out.println("Test passed: TAG_OPBD: " + tag_opbd);
        }
    }
}
