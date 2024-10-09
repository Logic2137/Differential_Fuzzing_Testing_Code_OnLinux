

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;


public final class TextBeanProperty {

    public static void main(String[] args) throws Exception {
        test(JTextComponent.class);
        test(JTextField.class);
        test(JTextArea.class);
        test(JPasswordField.class);
    }

    private static void test(Class<?> beanClass) throws Exception {
        BeanInfo info = Introspector.getBeanInfo(beanClass);
        PropertyDescriptor[] pd = info.getPropertyDescriptors();
        int i;
        for (i = 0; i < pd.length; i++) {
            if (pd[i].getName().equals("text")) {
                break;
            }
        }
        if (pd[i].isBound()) {
            System.err.println("Property: " + pd[i]);
            throw new RuntimeException("text property is flagged as bound");
        }
    }
}
