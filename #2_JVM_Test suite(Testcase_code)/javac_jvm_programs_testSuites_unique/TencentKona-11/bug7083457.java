

import javax.swing.text.DefaultCaret;


public class bug7083457 {

    public static void main(String[] args) {
        DefaultCaret caret = new DefaultCaret();

        for (int i = 0; i < 10; i++) {
            boolean active = (i % 2 == 0);
            caret.setVisible(active);
            if (caret.isActive() != active) {
                throw new RuntimeException("caret.isActive() does not equal: " + active);
            }
        }
    }
}
