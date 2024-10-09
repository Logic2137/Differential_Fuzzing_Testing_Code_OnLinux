import java.util.*;
import java.util.zip.*;
import java.util.jar.*;
import java.io.*;

public class Assortment {

    static int passed = 0, failed = 0;

    static void fail(String msg) {
        failed++;
        new Exception(msg).printStackTrace();
    }

    static void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    static void check(boolean condition, String msg) {
        if (!condition)
            fail(msg);
    }

    static void check(boolean condition) {
        check(condition, "Something's wrong");
    }

    static final int get16(byte[] b, int off) {
        return Byte.toUnsignedInt(b[off]) | (Byte.toUnsignedInt(b[off + 1]) << 8);
    }

    static boolean equalsExtraData(byte[] expected, byte[] extra) {
        if (expected == null)
            return true;
        int off = 0;
        int len = expected.length;
        while (off + 4 < len) {
            int tag = get16(expected, off);
            int sz = get16(expected, off + 2);
            int off0 = 0;
            int len0 = extra.length;
            boolean matched = false;
            while (off0 + 4 < len0) {
                int tag0 = get16(extra, off0);
                int sz0 = get16(extra, off0 + 2);
                if (tag == tag0 && sz == sz0) {
                    matched = true;
                    for (int i = 0; i < sz; i++) {
                        if (expected[off + i] != extra[off0 + i])
                            matched = false;
                    }
                    break;
                }
                off0 += (4 + sz0);
            }
            if (!matched)
                return false;
            off += (4 + sz);
        }
        return true;
    }

    private static class Entry {

        private String name;

        private int method;

        private byte[] data;

        private byte[] extra;

        private String comment;

        Entry(String name, int method, byte[] data, byte[] extra, String comment) {
            this.name = name;
            this.method = method;
            this.data = data;
            this.extra = extra;
            this.comment = comment;
        }

        void write(ZipOutputStream s) throws Exception {
            ZipEntry e = new ZipEntry(name);
            CRC32 crc32 = new CRC32();
            e.setMethod(method);
            if (method == ZipEntry.STORED) {
                e.setSize(data == null ? 0 : data.length);
                crc32.reset();
                if (data != null)
                    crc32.update(data);
                e.setCrc(crc32.getValue());
            } else {
                e.setSize(0);
                e.setCrc(0);
            }
            if (comment != null)
                e.setComment(comment);
            if (extra != null)
                e.setExtra(extra);
            s.putNextEntry(e);
            if (data != null)
                s.write(data);
        }

        byte[] getData(ZipFile f, ZipEntry e) throws Exception {
            byte[] fdata = new byte[(int) e.getSize()];
            InputStream is = f.getInputStream(e);
            is.read(fdata);
            return fdata;
        }

        void verify(ZipFile f, ZipEntry e) throws Exception {
            verify(e, getData(f, e));
        }

        void verify(ZipEntry e, byte[] eData) throws Exception {
            byte[] data = (this.data == null) ? new byte[] {} : this.data;
            byte[] extra = (this.extra != null && this.extra.length == 0) ? null : this.extra;
            check(name.equals(e.getName()));
            check(method == e.getMethod());
            check((((comment == null) || comment.equals("")) && (e.getComment() == null)) || comment.equals(e.getComment()));
            check(equalsExtraData(extra, e.getExtra()));
            check(Arrays.equals(data, eData));
            check(e.getSize() == data.length);
            check((method == ZipEntry.DEFLATED) || (e.getCompressedSize() == data.length));
        }

        void verify(ZipFile f) throws Exception {
            ZipEntry e = f.getEntry(name);
            verify(e, getData(f, e));
        }

        void verifyZipInputStream(ZipInputStream s) throws Exception {
            ZipEntry e = s.getNextEntry();
            byte[] data = (this.data == null) ? new byte[] {} : this.data;
            byte[] otherData = new byte[data.length];
            s.read(otherData);
            check(Arrays.equals(data, otherData));
            byte[] extra = (this.extra != null && this.extra.length == 0) ? null : this.extra;
            check(equalsExtraData(extra, e.getExtra()));
            check(name.equals(e.getName()));
            check(method == e.getMethod());
            check(e.getSize() == -1 || e.getSize() == data.length);
            check((method == ZipEntry.DEFLATED) || (e.getCompressedSize() == data.length));
        }

        void verifyJarInputStream(JarInputStream s) throws Exception {
            if (name.equals("meta-iNf/ManIfEst.Mf"))
                return;
            verifyZipInputStream(s);
        }
    }

    private static int uniquifier = 86;

    private static String uniquify(String name) {
        return name + (uniquifier++);
    }

    private static byte[] toBytes(String s) throws Exception {
        return s.getBytes("UTF-8");
    }

    private static byte[] toExtra(byte[] bytes) throws Exception {
        if (bytes == null)
            return null;
        byte[] v = new byte[bytes.length + 4];
        v[0] = (byte) 0x47;
        v[1] = (byte) 0xff;
        v[2] = (byte) bytes.length;
        v[3] = (byte) (bytes.length << 8);
        System.arraycopy(bytes, 0, v, 4, bytes.length);
        return v;
    }

    private static Random random = new Random();

    private static String makeName(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append((char) (random.nextInt(10000) + 1));
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        File zipName = new File("x.zip");
        int[] methods = { ZipEntry.STORED, ZipEntry.DEFLATED };
        String[] names = { makeName(1), makeName(160), makeName(9000) };
        byte[][] datas = { null, new byte[] {}, new byte[] { 'd' } };
        byte[][] extras = { null, new byte[] {}, new byte[] { 'e' } };
        String[] comments = { null, "", "c" };
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry("meta-iNf/ManIfEst.Mf", ZipEntry.STORED, toBytes("maNiFest-VeRsIon: 1.0\n"), toExtra(toBytes("Can manifests have extra??")), "Can manifests have comments??"));
        entries.add(new Entry("", ZipEntry.STORED, null, null, ""));
        for (String name : names) for (int method : methods) for (
        byte[] data : datas) for (byte[] extra : extras) for (String comment : comments) entries.add(new Entry(uniquify(name), method, data, toExtra(extra), comment));
        try (FileOutputStream fos = new FileOutputStream(zipName);
            ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (Entry e : entries) e.write(zos);
        }
        try (ZipFile f = new ZipFile(zipName)) {
            for (Entry e : entries) e.verify(f);
        }
        try (JarFile f = new JarFile(zipName)) {
            check(f.getManifest() != null);
            for (Entry e : entries) e.verify(f);
        }
        try (ZipFile f = new ZipFile(zipName)) {
            Enumeration<? extends ZipEntry> en = f.entries();
            for (Entry e : entries) e.verify(f, en.nextElement());
            check(!en.hasMoreElements());
        }
        try (JarFile f = new JarFile(zipName)) {
            Enumeration<? extends ZipEntry> en = f.entries();
            for (Entry e : entries) e.verify(f, en.nextElement());
            check(!en.hasMoreElements());
        }
        try (FileInputStream fis = new FileInputStream(zipName);
            ZipInputStream s = new ZipInputStream(fis)) {
            for (Entry e : entries) e.verifyZipInputStream(s);
        }
        try (FileInputStream fis = new FileInputStream(zipName);
            JarInputStream s = new JarInputStream(fis)) {
            check(s.getManifest() != null);
            for (Entry e : entries) e.verifyJarInputStream(s);
        }
        zipName.deleteOnExit();
        System.out.printf("passed = %d, failed = %d%n", passed, failed);
        if (failed > 0)
            throw new Exception("Some tests failed");
    }
}
