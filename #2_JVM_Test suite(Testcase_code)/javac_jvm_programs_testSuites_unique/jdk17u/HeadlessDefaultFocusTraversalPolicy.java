import java.awt.*;

public class HeadlessDefaultFocusTraversalPolicy {

    public static void main(String[] args) {
        Container c = new Container();
        Component cb1;
        Component cb2;
        Component cb3;
        DefaultFocusTraversalPolicy cot = new DefaultFocusTraversalPolicy();
        c.setFocusCycleRoot(true);
        c.setFocusTraversalPolicy(cot);
        c.add(cb1 = new Component() {
        });
        c.add(cb2 = new Component() {
        });
        c.add(cb3 = new Component() {
        });
        cot.getComponentAfter(c, cb1);
        cot.getComponentAfter(c, cb2);
        cot.getComponentAfter(c, cb3);
        cot.getComponentBefore(c, cb1);
        cot.getComponentBefore(c, cb2);
        cot.getComponentBefore(c, cb3);
        cot.getFirstComponent(c);
        cot.getLastComponent(c);
        cot.getDefaultComponent(c);
        cot.setImplicitDownCycleTraversal(true);
        cot.setImplicitDownCycleTraversal(false);
        cot.getImplicitDownCycleTraversal();
    }
}
