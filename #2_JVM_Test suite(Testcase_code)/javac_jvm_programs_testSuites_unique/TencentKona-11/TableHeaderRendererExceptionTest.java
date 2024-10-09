

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.JTableHeader;


public class TableHeaderRendererExceptionTest {

    public static void main(String[] args) throws Throwable {
        
        UIManager.LookAndFeelInfo[] lookAndFeelArray
                = UIManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo lookAndFeelItem : lookAndFeelArray) {
            String lookAndFeelString = lookAndFeelItem.getClassName();
            try{
                UIManager.setLookAndFeel(lookAndFeelString);
            } catch (final UnsupportedLookAndFeelException ignored) {
                continue;
            }

            
            JTableHeader header = new JTableHeader();

            header.getDefaultRenderer().getTableCellRendererComponent(null,
                    " test ", true, true, -1, 0);
        }
    }
}
