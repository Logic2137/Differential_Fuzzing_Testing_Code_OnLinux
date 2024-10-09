

import javax.swing.UIManager;
import javax.swing.table.JTableHeader;


public class TableHeaderRendererExceptionTest {

    public static void main(String[] args) throws Throwable {
        
        UIManager.LookAndFeelInfo[] lookAndFeelArray
                = UIManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo lookAndFeelItem : lookAndFeelArray) {
            String lookAndFeelString = lookAndFeelItem.getClassName();

            UIManager.setLookAndFeel(lookAndFeelString);

            
            JTableHeader header = new JTableHeader();

            header.getDefaultRenderer().getTableCellRendererComponent(null,
                    " test ", true, true, -1, 0);
        }
    }
}
