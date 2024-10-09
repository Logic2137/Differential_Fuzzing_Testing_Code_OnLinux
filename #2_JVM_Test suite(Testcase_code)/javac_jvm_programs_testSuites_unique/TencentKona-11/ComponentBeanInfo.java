

package infos;

import java.awt.Component;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class ComponentBeanInfo extends SimpleBeanInfo {
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            return new PropertyDescriptor[] {
                    new PropertyDescriptor("name", Component.class)
            };
        } catch (IntrospectionException exception) {
            throw new Error("unexpected exception", exception);
        }
    }
}
