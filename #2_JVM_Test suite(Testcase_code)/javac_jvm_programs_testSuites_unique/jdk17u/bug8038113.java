import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTreeUI;

public class bug8038113 extends JApplet {

    @Override
    public void init() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final JTree tree = new JTree();
                final BasicTreeUI treeUI = (BasicTreeUI) tree.getUI();
                final JPanel panel = new JPanel() {

                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke(0.5f));
                        g2.scale(2, 2);
                        int x = 10;
                        int y = 10;
                        Icon collapsedIcon = treeUI.getCollapsedIcon();
                        Icon expandeIcon = treeUI.getExpandedIcon();
                        int w = collapsedIcon.getIconWidth();
                        int h = collapsedIcon.getIconHeight();
                        collapsedIcon.paintIcon(this, g, x, y);
                        g.drawRect(x, y, w, h);
                        y += 10 + h;
                        w = expandeIcon.getIconWidth();
                        h = expandeIcon.getIconHeight();
                        expandeIcon.paintIcon(this, g, x, y);
                        g.drawRect(x, y, w, h);
                    }
                };
                getContentPane().setLayout(new BorderLayout());
                getContentPane().add(panel, BorderLayout.CENTER);
            }
        });
    }
}
