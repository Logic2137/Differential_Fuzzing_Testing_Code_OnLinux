

import static java.util.zip.ZipFile.CENOFF;
import static java.util.zip.ZipFile.CENTIM;
import static java.util.zip.ZipFile.ENDHDR;
import static java.util.zip.ZipFile.ENDOFF;
import static java.util.zip.ZipFile.LOCTIM;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZeroDate {

    public static void main(String[] args) throws Exception {
        
        Path path = Files.createTempFile("bad", ".zip");
        try (OutputStream os = Files.newOutputStream(path);
                ZipOutputStream zos = new ZipOutputStream(os)) {
            ZipEntry e = new ZipEntry("x");
            zos.putNextEntry(e);
            zos.write((int) 'x');
        }
        int len = (int) Files.size(path);
        byte[] data = new byte[len];
        try (InputStream is = Files.newInputStream(path)) {
            is.read(data);
        }
        Files.delete(path);

        
        testDate(data.clone(), 0, LocalDate.of(1979, 11, 30).atStartOfDay());
        
        testDate(data.clone(), 0 << 25 | 4 << 21 | 5 << 16, LocalDate.of(1980, 4, 5).atStartOfDay());
        
        testDate(data.clone(), 0 << 25 | 13 << 21 | 1 << 16, LocalDate.of(1981, 1, 1).atStartOfDay());
        
        testDate(data.clone(), 0 << 25 | 2 << 21 | 30 << 16, LocalDate.of(1980, 3, 1).atStartOfDay());
        
        testDate(data.clone(), 0 << 25 | 2 << 21 | 30 << 16 | 24 << 11 | 60 << 5 | 60 >> 1,
                LocalDateTime.of(1980, 3, 2, 1, 1, 0));
    }

    private static void testDate(byte[] data, int date, LocalDateTime expected) throws IOException {
        
        int endpos = data.length - ENDHDR;
        int cenpos = u16(data, endpos + ENDOFF);
        int locpos = u16(data, cenpos + CENOFF);
        writeU32(data, cenpos + CENTIM, date);
        writeU32(data, locpos + LOCTIM, date);

        
        Path path = Files.createTempFile("out", ".zip");
        try (OutputStream os = Files.newOutputStream(path)) {
            os.write(data);
        }
        URI uri = URI.create("jar:" + path.toUri());
        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            Path entry = fs.getPath("x");
            Instant actualInstant =
                    Files.readAttributes(entry, BasicFileAttributes.class)
                            .lastModifiedTime()
                            .toInstant();
            Instant expectedInstant =
                    expected.atZone(ZoneId.systemDefault()).toInstant();
            if (!actualInstant.equals(expectedInstant)) {
                throw new AssertionError(
                        String.format("actual: %s, expected: %s", actualInstant, expectedInstant));
            }
        } finally {
            Files.delete(path);
        }
    }

    static int u8(byte[] data, int offset) {
        return data[offset] & 0xff;
    }

    static int u16(byte[] data, int offset) {
        return u8(data, offset) + (u8(data, offset + 1) << 8);
    }

    private static void writeU32(byte[] data, int pos, int value) {
        data[pos] = (byte) (value & 0xff);
        data[pos + 1] = (byte) ((value >> 8) & 0xff);
        data[pos + 2] = (byte) ((value >> 16) & 0xff);
        data[pos + 3] = (byte) ((value >> 24) & 0xff);
    }
}
