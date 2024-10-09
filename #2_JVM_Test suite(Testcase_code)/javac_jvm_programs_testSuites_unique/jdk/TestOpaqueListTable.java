

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



public class TestOpaqueListTable {

    public static void main(String[] args) throws Exception {
        UIManager.LookAndFeelInfo[] installedLookAndFeels;
        installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo LF : installedLookAndFeels) {
            try {
                UIManager.setLookAndFeel(LF.getClassName());
                SwingUtilities.invokeAndWait(() -> {
                    JList list = new JList();
                    JTable table = new JTable();
                    JTree tree = new JTree();
                    JToolTip toolTip = new JToolTip();
                    JViewport viewport = new JViewport();
                    String opaqueValue =  new String(" ");

                    if (!list.isOpaque()) {
                        opaqueValue += "JList, ";
                    }
                    if (!table.isOpaque()) {
                        opaqueValue += "JTable, ";
                    }
                    if (!tree.isOpaque()) {
                        opaqueValue += "JTree, ";
                    }
                    if (!toolTip.isOpaque()) {
                        opaqueValue += "JToolTip, ";

                    }
                    if (!viewport.isOpaque()) {
                        opaqueValue += "JViewport, ";
                    }

                    if(!opaqueValue.equals(" ")) {
                        throw new RuntimeException("Default value of " +
                                "\"opaque\" property for " + opaqueValue
                                + " is changed ");
                    }

                    LookAndFeel.installProperty(list, "opaque", false);
                    LookAndFeel.installProperty(table, "opaque", false);
                    LookAndFeel.installProperty(tree, "opaque", false);
                    LookAndFeel.installProperty(toolTip,"opaque",false);
                    LookAndFeel.installProperty(viewport,"opaque",false);

                    opaqueValue = " ";
                    if (list.isOpaque()) {
                        opaqueValue += "JList, ";
                    }
                    if (table.isOpaque()) {
                        opaqueValue += "JTable, ";
                    }
                    if (tree.isOpaque()) {
                        opaqueValue += "JTree, ";
                    }
                    if (toolTip.isOpaque()) {
                        opaqueValue += "JToolTip, ";
                    }
                    if (viewport.isOpaque()) {
                        opaqueValue += "JViewport, ";
                    }
                    if (!opaqueValue.equals(" ")) {
                        throw new RuntimeException(
                                "setUIProperty failed to clear " +
                                        opaqueValue +" opaque" +
                                        " when opaque is not set by client");
                    }


                    list.setOpaque(true);
                    table.setOpaque(true);
                    tree.setOpaque(true);
                    toolTip.setOpaque(true);
                    viewport.setOpaque(true);

                    LookAndFeel.installProperty(list,"opaque",false);
                    LookAndFeel.installProperty(table, "opaque", false);
                    LookAndFeel.installProperty(tree, "opaque", false);
                    LookAndFeel.installProperty(toolTip, "opaque", false);
                    LookAndFeel.installProperty(viewport, "opaque", false);

                    opaqueValue = " ";

                    if (!list.isOpaque()) {
                        opaqueValue += "JList";
                    }
                    if (!table.isOpaque()) {
                        opaqueValue += "JTable";
                    }
                    if (!tree.isOpaque()) {
                        opaqueValue += "JTree";
                    }
                    if (!toolTip.isOpaque()) {
                        opaqueValue += "JToolTip";
                    }
                    if (!viewport.isOpaque()) {
                        opaqueValue += "JViewport";
                    }

                    if (!opaqueValue.equals(" ")) {
                        throw new RuntimeException("" +
                                "setUIProperty cleared the " +opaqueValue +
                                " Opaque when opaque is set by client");
                    }

                });
            } catch (UnsupportedLookAndFeelException e) {
                System.out.println("Note: LookAndFeel " + LF.getClassName()
                        + " is not supported on this configuration");
            }
        }
    }
}
