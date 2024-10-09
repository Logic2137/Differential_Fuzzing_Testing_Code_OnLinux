

import java.awt.print.Paper;



public class HeadlessPaper {
    public static void main(String args[]) {
        Paper p;
        p = (Paper) new Paper().clone();
        p.getHeight();
        p.setSize(200.0, 300.0);
        p.getWidth();
        p.setImageableArea(1.0, 1.0, 300.0, 400.0);
        p.getImageableX();
        p.getImageableY();
        p.getImageableWidth();
        p.getImageableHeight();
    }
}
