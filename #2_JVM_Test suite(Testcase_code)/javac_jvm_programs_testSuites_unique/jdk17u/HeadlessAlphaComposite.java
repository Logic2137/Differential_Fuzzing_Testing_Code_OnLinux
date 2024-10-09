import java.awt.*;
import java.awt.image.ColorModel;

public class HeadlessAlphaComposite {

    public static void main(String[] args) {
        AlphaComposite ac;
        ac = AlphaComposite.getInstance(AlphaComposite.CLEAR);
        ac = AlphaComposite.getInstance(AlphaComposite.DST_IN);
        ac = AlphaComposite.getInstance(AlphaComposite.DST_OUT);
        ac = AlphaComposite.getInstance(AlphaComposite.DST_OVER);
        ac = AlphaComposite.getInstance(AlphaComposite.SRC);
        ac = AlphaComposite.getInstance(AlphaComposite.SRC_IN);
        ac = AlphaComposite.getInstance(AlphaComposite.SRC_OUT);
        ac = AlphaComposite.getInstance(AlphaComposite.DST_IN);
        ac = AlphaComposite.getInstance(AlphaComposite.SRC, (float) 0.5);
        ac = AlphaComposite.getInstance(AlphaComposite.SRC, (float) 0.5);
        CompositeContext cc = ac.createContext(ColorModel.getRGBdefault(), ColorModel.getRGBdefault(), new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        ac = AlphaComposite.getInstance(AlphaComposite.SRC, (float) 0.5);
        float alpha = ac.getAlpha();
        ac = AlphaComposite.getInstance(AlphaComposite.SRC, (float) 0.5);
        int rule = ac.getRule();
        ac = AlphaComposite.getInstance(AlphaComposite.SRC, (float) 0.5);
        int hc = ac.hashCode();
    }
}
