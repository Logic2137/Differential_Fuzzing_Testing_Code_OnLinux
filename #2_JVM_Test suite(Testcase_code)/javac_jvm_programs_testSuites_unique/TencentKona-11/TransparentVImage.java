


import java.awt.GraphicsConfiguration;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;


public class TransparentVImage
    extends JComponent {

    BufferedImage cImgTransparent, cImgTranslucent;
    VolatileImage vImgTransparent, vImgTranslucent;
    Image sprite;
    static final int IMAGE_SIZE = 250;
    static final int WINDOW_SIZE = 600;
    static boolean doneComparing = false;
    static JFrame testFrame = null;

    @Override
    public void paint(Graphics g) {
        if (cImgTransparent == null) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            
            cImgTransparent = (BufferedImage)
                gc.createCompatibleImage(IMAGE_SIZE, IMAGE_SIZE,
                                         Transparency.BITMASK);
            cImgTranslucent = (BufferedImage)
                gc.createCompatibleImage(IMAGE_SIZE, IMAGE_SIZE,
                                         Transparency.TRANSLUCENT);
            vImgTransparent = gc.createCompatibleVolatileImage(IMAGE_SIZE,
                IMAGE_SIZE, Transparency.BITMASK);
            vImgTranslucent = gc.createCompatibleVolatileImage(IMAGE_SIZE,
                IMAGE_SIZE, Transparency.TRANSLUCENT);

            String fileName = "duke.gif";
            String separator = System.getProperty("file.separator");
            String dirPath = System.getProperty("test.src", ".");
            String filePath = dirPath + separator + fileName;
            sprite = new ImageIcon(filePath).getImage();

            
            Graphics gImg[] = new Graphics[4];
            gImg[0] = cImgTransparent.getGraphics();
            gImg[1] = cImgTranslucent.getGraphics();
            gImg[2] = vImgTransparent.getGraphics();
            gImg[3] = vImgTranslucent.getGraphics();

            for (int i = 0; i < gImg.length; ++i) {
                
                
                gImg[i].setColor(Color.blue);
                gImg[i].fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);

                
                int spriteW = sprite.getWidth(null);
                gImg[i].drawImage(sprite, 0, 0, null);
                gImg[i].drawImage(sprite, spriteW, 0, null);
                gImg[i].drawImage(sprite, 2 * spriteW, 0, null);

                
                gImg[i].setColor(Color.red);
                gImg[i].drawLine(0, 0,
                                 IMAGE_SIZE - 1, IMAGE_SIZE - 1);
                gImg[i].drawLine(IMAGE_SIZE / 2, 0,
                                 IMAGE_SIZE / 2, IMAGE_SIZE - 1);
                
                
                gImg[i].drawLine(0, IMAGE_SIZE / 2,
                                 IMAGE_SIZE - 1, IMAGE_SIZE / 2);

                
                
                
                
            }

            
            int cRgbTransparent[] = new int[IMAGE_SIZE * IMAGE_SIZE];
            int cRgbTranslucent[] = new int[IMAGE_SIZE * IMAGE_SIZE];
            int vRgbTransparent[] = new int[IMAGE_SIZE * IMAGE_SIZE];
            int vRgbTranslucent[] = new int[IMAGE_SIZE * IMAGE_SIZE];
            cImgTransparent.getRGB(0, 0, IMAGE_SIZE, IMAGE_SIZE,
                                   cRgbTransparent, 0, IMAGE_SIZE);
            cImgTranslucent.getRGB(0, 0, IMAGE_SIZE, IMAGE_SIZE,
                                   cRgbTranslucent, 0, IMAGE_SIZE);
            BufferedImage bImgTransparent = vImgTransparent.getSnapshot();
            bImgTransparent.getRGB(0, 0, IMAGE_SIZE, IMAGE_SIZE,
                                   vRgbTransparent, 0, IMAGE_SIZE);
            BufferedImage bImgTranslucent = vImgTranslucent.getSnapshot();
            bImgTranslucent.getRGB(0, 0, IMAGE_SIZE, IMAGE_SIZE,
                                   vRgbTranslucent, 0, IMAGE_SIZE);

            boolean failed = false;
            for (int pixel = 0; pixel < cRgbTransparent.length; ++pixel) {
                if (cRgbTransparent[pixel] != vRgbTransparent[pixel]) {
                    failed = true;
                    System.out.println("Error in transparent image: " +
                        "BI[" + pixel + "] = " +
                        Integer.toHexString(cRgbTransparent[pixel]) +
                        "VI[" + pixel + "] = " +
                        Integer.toHexString(vRgbTransparent[pixel]));
                    break;
                }
                if (cRgbTranslucent[pixel] != vRgbTranslucent[pixel]) {
                    failed = true;
                    System.out.println("Error in translucent image: " +
                        "BI[" + pixel + "] = " +
                        Integer.toHexString(cRgbTranslucent[pixel]) +
                        "VI[" + pixel + "] = " +
                        Integer.toHexString(vRgbTranslucent[pixel]));
                    break;
                }
            }
            if (failed) {
                throw new RuntimeException("Failed: Pixel mis-match observed");
            }
            else {
                System.out.println("Passed");
            }
            doneComparing = true;
        }

        g.drawImage(cImgTransparent, 0, 0, null);
        g.drawImage(cImgTranslucent, getWidth() - IMAGE_SIZE, 0, null);
        g.drawImage(vImgTransparent, 0, getHeight() - IMAGE_SIZE, null);
        g.drawImage(vImgTranslucent, getWidth() - IMAGE_SIZE,
                    getHeight() - IMAGE_SIZE, null);
    }

    private static void constructTestUI() {
        testFrame = new JFrame();
        testFrame.setSize(600, 600);
        testFrame.setResizable(false);
        testFrame.getContentPane().add(new TransparentVImage());

        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);
    }

    private static void destroyTestUI() {
        testFrame.dispose();
    }

    public static void main(String args[]) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    constructTestUI();
                } catch (Exception ex) {
                    
                    throw new RuntimeException("Test Failed: Error while" +
                        " creating the test interface.");
                }
            }
        });

        
        while (!doneComparing) {
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {}
        }

        
        try {
            Thread.sleep(5000);
        }
        catch (Exception e) {}

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    destroyTestUI();
                } catch (Exception ex) {
                    
                    throw new RuntimeException("Test Failed: Error while" +
                        " deleting the test interface.");
                }
            }
        });
    }
}
