

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;


public final class PaintContextScaleValidation extends AbstractRegionPainter {

    public static void main(final String[] args) {
        final PaintContextScaleValidation t = new PaintContextScaleValidation();
        t.test(0, 0);
        t.test(0, 1);
        t.test(1, 0);
    }

    private void test(final double maxH, final double maxV) {
        try {
            new PaintContext(new Insets(1, 1, 1, 1), new Dimension(1, 1), false,
                             null, maxH, maxV);
        } catch (final IllegalArgumentException ignored) {
            return; 
        }
        throw new RuntimeException("IllegalArgumentException was not thrown");
    }

    @Override
    protected PaintContext getPaintContext() {
        return null;
    }

    @Override
    protected void doPaint(final Graphics2D g, final JComponent c,
                           final int width, final int height,
                           final Object[] extendedCacheKeys) {
    }
}
