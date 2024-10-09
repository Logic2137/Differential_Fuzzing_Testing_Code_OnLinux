import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.zip.*;
import static java.nio.charset.StandardCharsets.US_ASCII;

public class ReadZip {

    private static void unreached(Object o) throws Exception {
        throw new Exception("Expected exception was not thrown");
    }

    public static void main(String[] args) throws Exception {
        try (ZipFile zf = new ZipFile(new File(System.getProperty("test.src", "."), "input.zip"))) {
            try {
                unreached(zf.getEntry(null));
            } catch (NullPointerException e) {
            }
            try {
                unreached(zf.getInputStream(null));
            } catch (NullPointerException e) {
            }
            ZipEntry ze = zf.getEntry("ReadZip.java");
            if (ze == null) {
                throw new Exception("cannot read from zip file");
            }
        }
        File newZip = new File(System.getProperty("test.dir", "."), "input2.zip");
        Files.copy(Paths.get(System.getProperty("test.src", ""), "input.zip"), newZip.toPath(), StandardCopyOption.REPLACE_EXISTING);
        newZip.setWritable(true);
        try (OutputStream os = Files.newOutputStream(newZip.toPath(), StandardOpenOption.APPEND)) {
            os.write(1);
            os.write(3);
            os.write(5);
            os.write(7);
        }
        try (ZipFile zf = new ZipFile(newZip)) {
            ZipEntry ze = zf.getEntry("ReadZip.java");
            if (ze == null) {
                throw new Exception("cannot read from zip file");
            }
        } finally {
            newZip.delete();
        }
        try {
            try (FileOutputStream fos = new FileOutputStream(newZip);
                ZipOutputStream zos = new ZipOutputStream(fos)) {
                ZipEntry ze = new ZipEntry("ZipEntry");
                zos.putNextEntry(ze);
                zos.write(1);
                zos.write(2);
                zos.write(3);
                zos.write(4);
                zos.closeEntry();
                zos.setComment("This is the comment for testing");
            }
            try (ZipFile zf = new ZipFile(newZip)) {
                ZipEntry ze = zf.getEntry("ZipEntry");
                if (ze == null)
                    throw new Exception("cannot read entry from zip file");
                if (!"This is the comment for testing".equals(zf.getComment()))
                    throw new Exception("cannot read comment from zip file");
            }
        } finally {
            newZip.delete();
        }
        try {
            try (FileOutputStream fos = new FileOutputStream(newZip);
                ZipOutputStream zos = new ZipOutputStream(fos)) {
                ZipEntry ze = new ZipEntry("directory/");
                zos.putNextEntry(ze);
                zos.closeEntry();
            }
            try (ZipFile zf = new ZipFile(newZip)) {
                ZipEntry ze = zf.getEntry("directory/");
                if (ze == null || !ze.isDirectory())
                    throw new RuntimeException("read entry \"directory/\" failed");
                try (InputStream is = zf.getInputStream(ze)) {
                    is.available();
                } catch (Exception x) {
                    x.printStackTrace();
                }
                ze = zf.getEntry("directory");
                if (ze == null || !ze.isDirectory())
                    throw new RuntimeException("read entry \"directory\" failed");
                try (InputStream is = zf.getInputStream(ze)) {
                    is.available();
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        } finally {
            newZip.delete();
        }
        try {
            unreached(new ZipFile(new File(System.getProperty("test.src", "."), "input" + String.valueOf(new java.util.Random().nextInt()) + ".zip")));
        } catch (NoSuchFileException nsfe) {
        }
        Path path = Paths.get(System.getProperty("test.dir", ""), "end64.zip");
        try {
            URI uri = URI.create("jar:" + path.toUri());
            Map<String, Object> env = Map.of("create", "true", "forceZIP64End", "true");
            try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
                Files.write(fs.getPath("hello"), "hello".getBytes());
            }
            try (ZipFile zf = new ZipFile(path.toFile())) {
                if (!"hello".equals(new String(zf.getInputStream(new ZipEntry("hello")).readAllBytes(), US_ASCII)))
                    throw new RuntimeException("zipfile: read entry failed");
            } catch (IOException x) {
                throw new RuntimeException("zipfile: zip64 end failed");
            }
            try (FileSystem fs = FileSystems.newFileSystem(uri, Map.of())) {
                if (!"hello".equals(new String(Files.readAllBytes(fs.getPath("hello")))))
                    throw new RuntimeException("zipfs: read entry failed");
            } catch (IOException x) {
                throw new RuntimeException("zipfile: zip64 end failed");
            }
        } finally {
            Files.deleteIfExists(path);
        }
        if (Files.notExists(Paths.get("/usr/bin/zip")))
            return;
        try {
            Process zip = new ProcessBuilder("zip", path.toString().toString(), "-").start();
            OutputStream os = zip.getOutputStream();
            os.write("hello".getBytes(US_ASCII));
            os.close();
            zip.waitFor();
            if (zip.exitValue() == 0 && Files.exists(path)) {
                try (ZipFile zf = new ZipFile(path.toFile())) {
                    if (!"hello".equals(new String(zf.getInputStream(new ZipEntry("-")).readAllBytes())))
                        throw new RuntimeException("zipfile: read entry failed");
                } catch (IOException x) {
                    throw new RuntimeException("zipfile: zip64 end failed");
                }
            }
        } finally {
            Files.deleteIfExists(path);
        }
    }
}
