

package nsk.share;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

public class FileUtils {
        private FileUtils() {
        }

        
        public static byte[] readFile(File file) throws IOException {
                InputStream in = new FileInputStream(file);
                long countl = file.length();
                if (countl > Integer.MAX_VALUE)
                        throw new IOException("File is too huge");
                int count = (int) countl;
                byte[] buffer = new byte[count];
                int n = 0;
                try {
                        while (n < count) {
                                int k = in.read(buffer, n, count - n);
                                if (k < 0)
                                        throw new IOException("Unexpected EOF");
                                n += k;
                        }
                } finally {
                        in.close();
                }
                return buffer;
        }

        
        public static byte[] readFile(String name) throws IOException {
                return readFile(new File(name));
        }
}
