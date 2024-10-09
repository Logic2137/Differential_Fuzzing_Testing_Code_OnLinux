

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;


public class BigFont {

   static private class SizedInputStream extends InputStream {

       int size;
       int cnt = 0;

       SizedInputStream(int size) {
           this.size = size;
       }

       public int read() {
           if (cnt < size) {
              cnt++;
              return 0;
           } else {
              return -1;
           }
       }

       public int getCurrentSize() {
           return cnt;
       }
   }

    static String id;
    static String fileName;

    public static void main(final String[] args) {
        id = args[0];
        fileName = args[1];

        System.out.println("Applet " + id + " "+
                           Thread.currentThread().getThreadGroup());
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        int fontSize = 64 * 1000 * 1000;
        SizedInputStream sis = new SizedInputStream(fontSize);
        try {
             Font font = Font.createFont(Font.TRUETYPE_FONT, sis);
        } catch (Throwable t) {
            t.printStackTrace();
            if (t instanceof FontFormatException ||
                fontSize <= sis.getCurrentSize())
            {
                System.out.println(sis.getCurrentSize());
                System.out.println(t);
                throw new RuntimeException("Allowed file to be too large.");
            }
        }
        
        
        
        
        
        System.out.println("Applet " + id + " finished.");
    }

    int getFileSize(String fileName) {
        try {
            String path = Paths.get(System.getProperty("test.src", "."),
                                    fileName).toAbsolutePath().normalize()
                                             .toString();
            URL url = new URL(path);
            InputStream inStream = url.openStream();
            BufferedInputStream fontStream = new BufferedInputStream(inStream);
            int size = 0;
            while (fontStream.read() != -1) {
                size++;
            }
            fontStream.close();
            return size;
        } catch (IOException e) {
            return 0;
        }

    }
    void loadMany(int oneFont, int fontCnt, String fileName) {
        System.out.println("fontcnt= " + fontCnt);
        Font[] fonts = new Font[fontCnt];
        int totalSize = 0;
        boolean gotException = false;
        for (int i=0; i<fontCnt; i++) {
            try {
                String path = Paths.get(System.getProperty("test.src", "."),
                                        fileName).toAbsolutePath().normalize()
                                                 .toString();
                URL url = new URL(path);
                InputStream inStream = url.openStream();
                BufferedInputStream fontStream =
                    new BufferedInputStream(inStream);
                fonts[i] = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                totalSize += oneFont;
                fontStream.close();
            } catch (Throwable t) {
                gotException = true;
                System.out.println("Applet " + id + " " + t);
            }
        }
        if (!gotException) {
          throw new RuntimeException("No expected exception");
        }
    }
}

