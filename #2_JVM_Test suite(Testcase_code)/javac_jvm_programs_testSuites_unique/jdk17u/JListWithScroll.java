import javax.swing.*;
import java.awt.*;
import java.awt.dnd.Autoscroll;

public class JListWithScroll<E> extends JList<E> implements Autoscroll {

    private Insets scrollInsets;

    public JListWithScroll(E[] listData) {
        super(listData);
        scrollInsets = new Insets(50, 50, 50, 50);
    }

    @Override
    public Insets getAutoscrollInsets() {
        return scrollInsets;
    }

    @Override
    public void autoscroll(Point cursorLoc) {
        JViewport viewport = getViewport();
        if (viewport == null) {
            return;
        }
        Point viewPos = viewport.getViewPosition();
        int viewHeight = viewport.getExtentSize().height;
        int viewWidth = viewport.getExtentSize().width;
        if ((cursorLoc.y - viewPos.y) < scrollInsets.top) {
            viewport.setViewPosition(new Point(viewPos.x, Math.max(viewPos.y - scrollInsets.top, 0)));
        } else if (((viewPos.y + viewHeight) - cursorLoc.y) < scrollInsets.bottom) {
            viewport.setViewPosition(new Point(viewPos.x, Math.min(viewPos.y + scrollInsets.bottom, this.getHeight() - viewHeight)));
        } else if ((cursorLoc.x - viewPos.x) < scrollInsets.left) {
            viewport.setViewPosition(new Point(Math.max(viewPos.x - scrollInsets.left, 0), viewPos.y));
        } else if (((viewPos.x + viewWidth) - cursorLoc.x) < scrollInsets.right) {
            viewport.setViewPosition(new Point(Math.min(viewPos.x + scrollInsets.right, this.getWidth() - viewWidth), viewPos.y));
        }
    }

    public JViewport getViewport() {
        Component curComp = this;
        while (!(curComp instanceof JViewport) && (curComp != null)) {
            curComp = curComp.getParent();
        }
        if (curComp instanceof JViewport) {
            return (JViewport) curComp;
        } else {
            return null;
        }
    }
}
