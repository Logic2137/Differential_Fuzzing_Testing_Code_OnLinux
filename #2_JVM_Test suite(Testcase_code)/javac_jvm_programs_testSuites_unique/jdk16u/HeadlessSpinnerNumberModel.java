

import javax.swing.*;



public class HeadlessSpinnerNumberModel {
    public static void main(String args[]) {
        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setValue(5);
        model.getValue();
        model.getPreviousValue();
        model.getNextValue();
    }
}
