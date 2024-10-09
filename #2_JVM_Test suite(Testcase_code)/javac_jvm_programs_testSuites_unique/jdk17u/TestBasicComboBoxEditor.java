import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class TestBasicComboBoxEditor {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TestBasicComboBoxEditor::testBasicComboBoxEditor);
        System.setSecurityManager(new SecurityManager());
        SwingUtilities.invokeAndWait(TestBasicComboBoxEditor::testBasicComboBoxEditor);
    }

    private static void testBasicComboBoxEditor() {
        BasicComboBoxEditor comboBoxEditor = new BasicComboBoxEditor();
        comboBoxEditor.setItem(new UserComboBoxEditorType("100"));
        JTextField editor = (JTextField) comboBoxEditor.getEditorComponent();
        editor.setText("200");
        UserComboBoxEditorType item = (UserComboBoxEditorType) comboBoxEditor.getItem();
        if (!item.str.equals("200")) {
            throw new RuntimeException("Wrong itme value!");
        }
    }

    public static class UserComboBoxEditorType {

        String str;

        public UserComboBoxEditorType(String str) {
            this.str = str;
        }

        public static UserComboBoxEditorType valueOf(String str) {
            return new UserComboBoxEditorType(str);
        }

        @Override
        public String toString() {
            return "UserComboBoxEditorType: " + str;
        }
    }
}
