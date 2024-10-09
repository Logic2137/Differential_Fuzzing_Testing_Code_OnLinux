



import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.SimpleBeanInfo;

public class Test7195106 {

    public static void main(String[] arg) throws Exception {
        BeanInfo info = Introspector.getBeanInfo(My.class);
        if (null == info.getIcon(BeanInfo.ICON_COLOR_16x16)) {
            throw new Error("Unexpected behavior");
        }
        try {
            int[] array = new int[1024];
            while (true) {
                array = new int[array.length << 1];
            }
        }
        catch (OutOfMemoryError error) {
            System.gc();
        }
        if (null == info.getIcon(BeanInfo.ICON_COLOR_16x16)) {
            throw new Error("Explicit BeanInfo is collected");
        }
    }

    public static class My {
    }

    public static class MyBeanInfo extends SimpleBeanInfo {
        @Override
        public Image getIcon(int type) {
            return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        }
    }
}
