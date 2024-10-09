

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class TreeClipTest {

    static boolean passed = true;

    static boolean checkImage(BufferedImage img, int clipY) {
        for (int y = clipY; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if ((img.getRGB(x,y) & 0xFFFFFF) != 0xFFFFFF) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                DefaultMutableTreeNode      root = new DefaultMutableTreeNode("JTree");
                DefaultMutableTreeNode      parent;

                parent = new DefaultMutableTreeNode("colors");
                root.add(parent);
                parent.add(new DefaultMutableTreeNode("blue"));
                DefaultTreeModel model = new DefaultTreeModel(root);
                JTree tree = new JTree(model);

                BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
                for (int clipY = 1; clipY < 50; clipY++) {
                    Graphics2D ig = img.createGraphics();
                    ig.setColor(Color.WHITE);
                    ig.fillRect(0,0,1000, 1000);
                    tree.setSize(200,200);
                    ig.setClip(0,0,1000,clipY);
                    tree.paint(ig);
                    ig.dispose();

                    if (!checkImage(img, clipY)) {
                        System.err.println("Failed with clipY=" + clipY);
                        passed = false;
                        try {
                            ImageIO.write(img, "PNG", new File("failedResult.png"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        });

        if (!passed) {
            throw new RuntimeException("Test failed.");
        } else {
            System.out.println("Passed.");
        }
    }
}
