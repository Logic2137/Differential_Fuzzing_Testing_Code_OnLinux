import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import javax.swing.JButton;

public final class LoadingStandardIcons {

    public static void main(final String[] args) {
        final Object bi;
        try {
            bi = Introspector.getBeanInfo(JButton.class);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        final Image m16 = ((BeanInfo) bi).getIcon(BeanInfo.ICON_MONO_16x16);
        final Image m32 = ((BeanInfo) bi).getIcon(BeanInfo.ICON_MONO_32x32);
        final Image c16 = ((BeanInfo) bi).getIcon(BeanInfo.ICON_COLOR_16x16);
        final Image c32 = ((BeanInfo) bi).getIcon(BeanInfo.ICON_COLOR_32x32);
        if (m16 == null || m32 == null || c16 == null || c32 == null) {
            throw new RuntimeException("Image should not be null");
        }
    }
}
