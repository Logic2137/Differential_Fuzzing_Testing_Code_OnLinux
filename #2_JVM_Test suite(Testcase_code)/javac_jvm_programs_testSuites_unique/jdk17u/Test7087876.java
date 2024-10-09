import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;

public class Test7087876 {

    public static void main(String[] args) throws IntrospectionException {
        PropertyDescriptor pd = new PropertyDescriptor("value", Bean.class);
        pd.setPropertyEditorClass(Editor.class);
        pd.createPropertyEditor(new Bean());
    }

    public static class Bean {

        private String value;

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Editor extends PropertyEditorSupport {

        private Editor() {
        }
    }
}
