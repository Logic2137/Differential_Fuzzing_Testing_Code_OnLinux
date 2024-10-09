import java.awt.*;

public class HeadlessBorderLayout {

    public static void main(String[] args) {
        BorderLayout bl;
        bl = new BorderLayout();
        bl = new BorderLayout(10, 10);
        bl.getHgap();
        bl.setHgap(10);
        bl.getVgap();
        bl.setVgap(10);
        bl = new BorderLayout();
        bl.setVgap(10);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.AFTER_LAST_LINE);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.AFTER_LINE_ENDS);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.BEFORE_FIRST_LINE);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.BEFORE_LINE_BEGINS);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.PAGE_START);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.PAGE_END);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.LINE_START);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.LINE_END);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.CENTER);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.EAST);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.NORTH);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.SOUTH);
        bl.addLayoutComponent(new Component() {
        }, BorderLayout.WEST);
        Component cb = new Component() {
        };
        bl.addLayoutComponent(cb, BorderLayout.WEST);
        bl.removeLayoutComponent(cb);
    }
}
