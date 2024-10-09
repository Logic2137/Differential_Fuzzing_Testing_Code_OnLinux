import javax.swing.*;

public class HeadlessAbstractSpinnerModel {

    public static void main(String[] args) {
        AbstractSpinnerModel model = new AbstractSpinnerModel() {

            public Object getValue() {
                return null;
            }

            public void setValue(Object value) {
            }

            public Object getNextValue() {
                return null;
            }

            public Object getPreviousValue() {
                return null;
            }
        };
        model.getPreviousValue();
        model.getNextValue();
        model.setValue("next");
        model.getValue();
    }
}
