



import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CopyZipFile {
    private static final String ZIP_FILE = "first.zip";
    private static final String TEST_STRING = "TestTestTest";

    private static void createZip(String zipFile) throws Exception {
        File f = new File(zipFile);
        f.deleteOnExit();
        try (OutputStream os = new FileOutputStream(f);
             ZipOutputStream zos = new ZipOutputStream(os)) {
            
            zos.putNextEntry(new ZipEntry("test1.txt"));
            zos.write(TEST_STRING.getBytes());
            zos.closeEntry();
            
            zos.setMethod(ZipOutputStream.STORED);
            ZipEntry ze = new ZipEntry("test2.txt");
            int length = TEST_STRING.length();
            ze.setSize(length);
            ze.setCompressedSize(length);
            CRC32 crc = new CRC32();
            crc.update(TEST_STRING.getBytes("utf8"), 0, length);
            ze.setCrc(crc.getValue());
            zos.putNextEntry(ze);
            zos.write(TEST_STRING.getBytes());
            
            zos.setMethod(ZipOutputStream.DEFLATED);
            zos.setLevel(Deflater.NO_COMPRESSION);
            zos.putNextEntry(new ZipEntry("test3.txt"));
            zos.write(TEST_STRING.getBytes());
            
            zos.setLevel(Deflater.BEST_SPEED);
            zos.putNextEntry(new ZipEntry("test4.txt"));
            zos.write(TEST_STRING.getBytes());
            
            zos.setLevel(Deflater.BEST_COMPRESSION);
            zos.putNextEntry(new ZipEntry("test5.txt"));
            zos.write(TEST_STRING.getBytes());
        }
    }

    public static void main(String args[]) throws Exception {
        
        
        
        
        
        
        createZip(ZIP_FILE);

        
        
        
        
        
        
        ZipEntry entry;
        byte[] buf = new byte[512];
        try (InputStream is = new FileInputStream(ZIP_FILE);
             ZipInputStream zis = new ZipInputStream(is);
             OutputStream os = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(os)) {
            while((entry = zis.getNextEntry())!=null) {
                
                
                
                System.out.println(
                    String.format("name=%s, clen=%d, len=%d, crc=%d",
                                  entry.getName(), entry.getCompressedSize(), entry.getSize(), entry.getCrc()));
                if (entry.getMethod() == ZipEntry.DEFLATED &&
                    (entry.getCompressedSize() != -1 || entry.getSize() != -1 || entry.getCrc() != -1)) {
                    throw new Exception("'size', 'compressedSize' and 'crc' shouldn't be initialized at this point.");
                }
                zos.putNextEntry(entry);
                zis.transferTo(zos);
                
                
                
                
                System.out.println(
                    String.format("name=%s, clen=%d, len=%d, crc=%d\n",
                                  entry.getName(), entry.getCompressedSize(), entry.getSize(), entry.getCrc()));
                if (entry.getCompressedSize() == -1 || entry.getSize() == -1) {
                    throw new Exception("'size' and 'compressedSize' must be initialized at this point.");
                }
            }
        }

        
        
        
        
        
        
        
        
        
        
        
        
        try (OutputStream os = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(os);
             ZipFile zf = new ZipFile(ZIP_FILE)) {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                System.out.println(
                    String.format("name=%s, clen=%d, len=%d, crc=%d\n",
                                  entry.getName(), entry.getCompressedSize(),
                                  entry.getSize(), entry.getCrc()));
                if (entry.getCompressedSize() == -1 || entry.getSize() == -1) {
                    throw new Exception("'size' and 'compressedSize' must be initialized at this point.");
                }
                InputStream is = zf.getInputStream(entry);
                zos.putNextEntry(entry);
                is.transferTo(zos);
                zos.closeEntry();
            }
        }

        
        
        try (OutputStream os = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(os);
             ZipFile zf = new ZipFile(ZIP_FILE)) {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                try {
                    entry = entries.nextElement();
                    entry.setCompressedSize(entry.getCompressedSize());
                    InputStream is = zf.getInputStream(entry);
                    zos.putNextEntry(entry);
                    is.transferTo(zos);
                    zos.closeEntry();
                    if ("test3.txt".equals(entry.getName())) {
                        throw new Exception(
                            "Should throw a ZipException if ZipEntry.setCpompressedSize() was called.");
                    }
                } catch (ZipException ze) {
                    if ("test1.txt".equals(entry.getName()) || "test2.txt".equals(entry.getName())) {
                        throw new Exception(
                            "Shouldn't throw a ZipExcpetion for STORED files or files compressed with DEFAULT_COMPRESSION");
                    }
                    
                    
                    
                    
                    Pattern cSize = Pattern.compile("\\d+");
                    Matcher m = cSize.matcher(ze.getMessage());
                    m.find();
                    m.find();
                    entry.setCompressedSize(Integer.parseInt(m.group()));
                }
            }
        }
    }
}
