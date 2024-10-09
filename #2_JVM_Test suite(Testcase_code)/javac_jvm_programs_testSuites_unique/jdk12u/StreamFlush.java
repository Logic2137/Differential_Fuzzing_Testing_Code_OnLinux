



import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class StreamFlush {

    public static void main(String[] args) throws IOException {
        ImageIO.setUseCache(true);

        
        File temp1 = File.createTempFile("imageio", ".tmp");
        temp1.deleteOnExit();
        ImageOutputStream fios = ImageIO.createImageOutputStream(temp1);

        
        File temp2 = File.createTempFile("imageio", ".tmp");
        temp2.deleteOnExit();
        FileOutputStream fos2 = new FileOutputStream(temp2);
        BufferedOutputStream bos = new BufferedOutputStream(fos2);
        ImageOutputStream fcios1 = ImageIO.createImageOutputStream(bos);

        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream fcios2 = ImageIO.createImageOutputStream(baos);

        BufferedImage bi =
            new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);

        ImageIO.write(bi, "jpg", fios); 
        ImageIO.write(bi, "png", fcios1); 
        ImageIO.write(bi, "jpg", fcios2); 

        
        
        

        
        long file1NoFlushLength = temp1.length();
        fios.flush();
        long file1FlushLength = temp1.length();

        
        long file2NoFlushLength = temp2.length();
        fcios1.flush();
        bos.flush();
        long file2FlushLength = temp2.length();

        byte[] b0 = baos.toByteArray();
        int cacheNoFlushLength = b0.length;
        fcios2.flush();
        byte[] b1 = baos.toByteArray();
        int cacheFlushLength = b1.length;

        if (file1NoFlushLength != file1FlushLength) {
            
            System.out.println
                ("FileImageOutputStream not flushed!");
        }

        if (file2NoFlushLength != file2FlushLength) {
            
            System.out.println
             ("FileCacheImageOutputStream/BufferedOutputStream not flushed!");
        }

        if (cacheNoFlushLength != cacheFlushLength) {
            
            System.out.println
            ("FileCacheImageOutputStream/ByteArrayOutputStream not flushed!");
        }
    }
}
