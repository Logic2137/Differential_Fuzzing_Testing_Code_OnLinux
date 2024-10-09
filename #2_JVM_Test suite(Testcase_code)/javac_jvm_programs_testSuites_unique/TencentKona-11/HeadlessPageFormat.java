

import java.awt.print.PageFormat;
import java.awt.print.Paper;



public class HeadlessPageFormat {
    public static void main(String args[]) {
        PageFormat pf;
        pf = (PageFormat) new PageFormat().clone();
        pf.getWidth();
        pf.getHeight();
        pf.getImageableX();
        pf.getImageableY();
        pf.getImageableWidth();
        pf.getImageableHeight();
        pf.getPaper();
        pf.setPaper(new Paper());
        pf.setOrientation(PageFormat.PORTRAIT);
        if (pf.getOrientation() != PageFormat.PORTRAIT)
            throw new RuntimeException("Changing Orientation did not result in a change: PageFormat.PORTRAIT");

        pf.setOrientation(PageFormat.LANDSCAPE);
        if (pf.getOrientation() != PageFormat.LANDSCAPE)
            throw new RuntimeException("Changing Orientation did not result in a change: PageFormat.LANDSCAPE");

        pf.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        if (pf.getOrientation() != PageFormat.REVERSE_LANDSCAPE)
            throw new RuntimeException("Changing Orientation did not result in a change: PageFormat.REVERSE_LANDSCAPE");

        pf.getOrientation();
        pf.getMatrix();
    }
}
