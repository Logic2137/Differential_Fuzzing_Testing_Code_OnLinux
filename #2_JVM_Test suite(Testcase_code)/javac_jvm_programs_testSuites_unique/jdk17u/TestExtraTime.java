import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class TestExtraTime {

    public static void main(String[] args) throws Throwable {
        File src = new File(System.getProperty("test.src", "."), "TestExtraTime.java");
        if (!src.exists()) {
            return;
        }
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        for (byte[] extra : new byte[][] { null, new byte[] { 1, 2, 3 } }) {
            test0(FileTime.from(10, TimeUnit.MILLISECONDS), null, null, null, extra);
            test0(FileTime.from(-100, TimeUnit.DAYS), null, null, null, extra);
            long time = src.lastModified();
            test(FileTime.from(time, TimeUnit.MILLISECONDS), FileTime.from(time + 300000, TimeUnit.MILLISECONDS), FileTime.from(time - 300000, TimeUnit.MILLISECONDS), tz, extra);
            time = Instant.now().toEpochMilli();
            test(FileTime.from(time, TimeUnit.MILLISECONDS), FileTime.from(time + 300000, TimeUnit.MILLISECONDS), FileTime.from(time - 300000, TimeUnit.MILLISECONDS), tz, extra);
            time = 0x80000000L;
            test(FileTime.from(time, TimeUnit.SECONDS), FileTime.from(time, TimeUnit.SECONDS), FileTime.from(time, TimeUnit.SECONDS), tz, extra);
            time = 0x7fffffffL;
            test(FileTime.from(time, TimeUnit.SECONDS), FileTime.from(time + 30000, TimeUnit.SECONDS), FileTime.from(time + 30000, TimeUnit.SECONDS), tz, extra);
        }
        testNullHandling();
        testTagOnlyHandling();
        testTimeConversions();
        testNullMtime();
    }

    static void test(FileTime mtime, FileTime atime, FileTime ctime, TimeZone tz, byte[] extra) throws Throwable {
        test0(mtime, null, null, null, extra);
        test0(mtime, null, null, tz, extra);
        test0(mtime, atime, null, null, extra);
        test0(mtime, null, ctime, null, extra);
        test0(mtime, atime, ctime, null, extra);
        test0(mtime, atime, null, tz, extra);
        test0(mtime, null, ctime, tz, extra);
        test0(mtime, atime, ctime, tz, extra);
    }

    static void test0(FileTime mtime, FileTime atime, FileTime ctime, TimeZone tz, byte[] extra) throws Throwable {
        System.out.printf("--------------------%nTesting: [%s]/[%s]/[%s]%n", mtime, atime, ctime);
        TimeZone tz0 = TimeZone.getDefault();
        if (tz != null) {
            TimeZone.setDefault(tz);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        ZipEntry ze = new ZipEntry("TestExtraTime.java");
        ze.setExtra(extra);
        ze.setLastModifiedTime(mtime);
        if (atime != null)
            ze.setLastAccessTime(atime);
        if (ctime != null)
            ze.setCreationTime(ctime);
        zos.putNextEntry(ze);
        zos.write(new byte[] { 1, 2, 3, 4 });
        if (extra != null) {
            ze = new ZipEntry("TestExtraEntry");
            zos.putNextEntry(ze);
        }
        zos.close();
        if (tz != null) {
            TimeZone.setDefault(tz0);
        }
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ze = zis.getNextEntry();
        zis.close();
        check(mtime, atime, ctime, ze, extra);
        Path zpath = Paths.get(System.getProperty("test.dir", "."), "TestExtraTime.zip");
        Path zparent = zpath.getParent();
        if (zparent != null && !Files.isWritable(zparent)) {
            System.err.format("zpath %s parent %s is not writable%n", zpath, zparent);
        }
        if (Files.exists(zpath)) {
            System.err.format("zpath %s already exists%n", zpath);
            if (Files.isDirectory(zpath)) {
                System.err.format("%n%s contents:%n", zpath);
                Files.list(zpath).forEach(System.err::println);
            }
            FileOwnerAttributeView foav = Files.getFileAttributeView(zpath, FileOwnerAttributeView.class);
            System.err.format("zpath %s owner: %s%n", zpath, foav.getOwner());
            System.err.format("zpath %s permissions:%n", zpath);
            Set<PosixFilePermission> perms = Files.getPosixFilePermissions(zpath);
            perms.stream().forEach(System.err::println);
        }
        if (Files.isSymbolicLink(zpath)) {
            System.err.format("zpath %s is a symbolic link%n", zpath);
        }
        Files.copy(new ByteArrayInputStream(baos.toByteArray()), zpath);
        try (ZipFile zf = new ZipFile(zpath.toFile())) {
            ze = zf.getEntry("TestExtraTime.java");
            check(mtime, null, null, ze, extra);
        } finally {
            Files.delete(zpath);
        }
    }

    static void check(FileTime mtime, FileTime atime, FileTime ctime, ZipEntry ze, byte[] extra) {
        if (mtime != null && mtime.to(TimeUnit.SECONDS) != ze.getLastModifiedTime().to(TimeUnit.SECONDS)) {
            throw new RuntimeException("Timestamp: storing mtime failed!");
        }
        if (atime != null && atime.to(TimeUnit.SECONDS) != ze.getLastAccessTime().to(TimeUnit.SECONDS))
            throw new RuntimeException("Timestamp: storing atime failed!");
        if (ctime != null && ctime.to(TimeUnit.SECONDS) != ze.getCreationTime().to(TimeUnit.SECONDS))
            throw new RuntimeException("Timestamp: storing ctime failed!");
        if (extra != null) {
            byte[] extra1 = ze.getExtra();
            if (extra1 == null || extra1.length < extra.length || !Arrays.equals(Arrays.copyOfRange(extra1, extra1.length - extra.length, extra1.length), extra)) {
                throw new RuntimeException("Timestamp: storing extra field failed!");
            }
        }
    }

    static void testNullHandling() {
        ZipEntry ze = new ZipEntry("TestExtraTime.java");
        try {
            ze.setLastAccessTime(null);
            throw new RuntimeException("setLastAccessTime(null) should throw NPE");
        } catch (NullPointerException ignored) {
        }
        try {
            ze.setCreationTime(null);
            throw new RuntimeException("setCreationTime(null) should throw NPE");
        } catch (NullPointerException ignored) {
        }
        try {
            ze.setLastModifiedTime(null);
            throw new RuntimeException("setLastModifiedTime(null) should throw NPE");
        } catch (NullPointerException ignored) {
        }
    }

    static void testTimeConversions() {
        long step = Long.MAX_VALUE / 100L;
        testTimeConversions(Long.MIN_VALUE, Long.MAX_VALUE - step, step);
        long currentTime = System.currentTimeMillis();
        testTimeConversions(currentTime, currentTime + 1_000_000, 10_000);
    }

    static void testTimeConversions(long from, long to, long step) {
        ZipEntry ze = new ZipEntry("TestExtraTime.java");
        for (long time = from; time <= to; time += step) {
            ze.setTime(time);
            FileTime lastModifiedTime = ze.getLastModifiedTime();
            if (lastModifiedTime.toMillis() != time) {
                throw new RuntimeException("setTime should make getLastModifiedTime " + "return the specified instant: " + time + " got: " + lastModifiedTime.toMillis());
            }
            if (ze.getTime() != time) {
                throw new RuntimeException("getTime after setTime, expected: " + time + " got: " + ze.getTime());
            }
        }
    }

    static void check(ZipEntry ze, byte[] extra) {
        if (extra != null) {
            byte[] extra1 = ze.getExtra();
            if (extra1 == null || extra1.length < extra.length || !Arrays.equals(Arrays.copyOfRange(extra1, extra1.length - extra.length, extra1.length), extra)) {
                throw new RuntimeException("Timestamp: storing extra field failed!");
            }
        }
    }

    static void testTagOnlyHandling() throws Throwable {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] extra = new byte[] { 0x0a, 0, 4, 0, 0, 0, 0, 0 };
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry ze = new ZipEntry("TestExtraTime.java");
            ze.setExtra(extra);
            zos.putNextEntry(ze);
            zos.write(new byte[] { 1, 2, 3, 4 });
        }
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            ZipEntry ze = zis.getNextEntry();
            check(ze, extra);
        }
        Path zpath = Paths.get(System.getProperty("test.dir", "."), "TestExtraTime.zip");
        Files.copy(new ByteArrayInputStream(baos.toByteArray()), zpath);
        try (ZipFile zf = new ZipFile(zpath.toFile())) {
            ZipEntry ze = zf.getEntry("TestExtraTime.java");
            check(ze, extra);
        } finally {
            Files.delete(zpath);
        }
    }

    static void checkLastModifiedTimeDOS(FileTime mtime, ZipEntry ze) {
        FileTime lmt = ze.getLastModifiedTime();
        if ((lmt.to(TimeUnit.SECONDS) >>> 1) != (mtime.to(TimeUnit.SECONDS) >>> 1) || lmt.to(TimeUnit.MILLISECONDS) != ze.getTime() || lmt.to(TimeUnit.MILLISECONDS) % 1000 != 0) {
            throw new RuntimeException("Timestamp: storing mtime in dos format failed!");
        }
    }

    static void testNullMtime() throws Throwable {
        Instant now = Instant.now();
        FileTime ctime = FileTime.from(now);
        FileTime atime = FileTime.from(now.plusSeconds(7));
        FileTime mtime = FileTime.from(now.plusSeconds(13));
        System.out.printf("--------------------%nTesting: [%s]/[%s]/[%s]%n", mtime, atime, ctime);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry ze = new ZipEntry("TestExtraTime.java");
            ze.setCreationTime(ctime);
            ze.setLastAccessTime(atime);
            ze.setTime(mtime.toMillis());
            zos.putNextEntry(ze);
            zos.write(new byte[] { 1, 2, 3, 4 });
        }
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            ZipEntry ze = zis.getNextEntry();
            check(null, atime, ctime, ze, null);
            checkLastModifiedTimeDOS(mtime, ze);
        }
        Path zpath = Paths.get(System.getProperty("test.dir", "."), "TestExtraTime.zip");
        Files.copy(new ByteArrayInputStream(baos.toByteArray()), zpath);
        try (ZipFile zf = new ZipFile(zpath.toFile())) {
            ZipEntry ze = zf.getEntry("TestExtraTime.java");
            checkLastModifiedTimeDOS(mtime, ze);
        } finally {
            Files.delete(zpath);
        }
    }
}
