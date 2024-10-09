

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;


public final class GetUI {

    public static void main(final String[] args) {
        CustomJComponent component = new CustomJComponent();
        ComponentUI ui = new ComponentUI() {
        };
        component.setUI(ui);
        ComponentUI actual = component.getUI();
        if (actual != ui) {
            System.err.println("Expected: " + ui);
            System.err.println("Actual: " + actual);
            throw new RuntimeException("Test failed");
        }
    }

    private static class CustomJComponent extends JComponent {

        @Override
        public ComponentUI getUI() {
            return super.getUI();
        }

        @Override
        public void setUI(ComponentUI ui) {
            super.setUI(ui);
        }
    }
}
