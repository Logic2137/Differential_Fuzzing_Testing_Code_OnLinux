import java.util.List;
import java.util.ArrayList;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.tiff.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TIFFFieldTest {

    private final static String NAME = "tag";

    private final static int NUM = 12345;

    private final static int MIN_TYPE = TIFFTag.MIN_DATATYPE;

    private final static int MAX_TYPE = TIFFTag.MAX_DATATYPE;

    private final static String CONSTRUCT = "can construct TIFFField with ";

    private void check(boolean ok, String msg) {
        if (!ok) {
            throw new RuntimeException(msg);
        }
    }

    private void testConstructors() {
        TIFFTag tag = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_SHORT | 1 << TIFFTag.TIFF_LONG);
        TIFFField f;
        boolean ok = false;
        try {
            new TIFFField(null, 0);
        } catch (NullPointerException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "null tag");
        ok = false;
        try {
            new TIFFField(tag, -1);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "negative value");
        ok = false;
        try {
            new TIFFField(tag, 1L << 32);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "value > 0xffffffff");
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_SHORT);
            new TIFFField(t, 0x10000);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "value 0x10000 incompatible with TIFF_SHORT");
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_LONG);
            new TIFFField(t, 0xffff);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "value 0xffff incompatible with TIFF_LONG");
        int v = 1 << 16;
        f = new TIFFField(tag, v - 1);
        check(f.getType() == TIFFTag.TIFF_SHORT, "must be treated as short");
        check(f.isIntegral(), "must be integral");
        f = new TIFFField(tag, v);
        check(f.getType() == TIFFTag.TIFF_LONG, "must be treated as long");
        check(f.getAsLongs().length == 1, "invalid long[] size");
        check(f.isIntegral(), "must be integral");
        check((f.getDirectory() == null) && !f.hasDirectory(), "must not have directory");
        check(f.getValueAsString(0).equals(String.valueOf(v)), "invalid string representation of value");
        check(f.getTag().getNumber() == f.getTagNumber(), "invalid tag number");
        check(f.getCount() == 1, "invalid count");
        check(f.getTagNumber() == NUM, "invalid tag number");
        int type = TIFFTag.TIFF_SHORT;
        ok = false;
        try {
            new TIFFField(null, type, 1);
        } catch (NullPointerException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "null tag");
        ok = false;
        try {
            new TIFFField(tag, MAX_TYPE + 1, 1);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "invalid type tag");
        ok = false;
        try {
            new TIFFField(tag, TIFFTag.TIFF_IFD_POINTER, 0);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "only count = 1 should be allowed for IFDPointer");
        ok = false;
        try {
            new TIFFField(tag, TIFFTag.TIFF_IFD_POINTER, 2);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "only count = 1 should be allowed for IFDPointer");
        ok = false;
        try {
            new TIFFField(tag, TIFFTag.TIFF_RATIONAL, 0);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "count = 0 should not be allowed for Rational");
        ok = false;
        try {
            new TIFFField(tag, TIFFTag.TIFF_SRATIONAL, 0);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "count = 0 should not be allowed for SRational");
        ok = false;
        try {
            new TIFFField(tag, type, -1);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "with invalid data count");
        f = new TIFFField(tag, type, 0);
        check(f.getCount() == 0, "invalid count");
        check(!f.hasDirectory(), "must not have directory");
        double[] a = { 0.1, 0.2, 0.3 };
        ok = false;
        try {
            new TIFFField(null, TIFFTag.TIFF_DOUBLE, a.length, a);
        } catch (NullPointerException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "null tag");
        ok = false;
        try {
            new TIFFField(tag, type, a.length - 1, a);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "invalid data count");
        String[] a2 = { "one", "two" };
        ok = false;
        try {
            new TIFFField(tag, type, 2, a2);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "invalid data type");
        check((f.getDirectory() == null) && !f.hasDirectory(), "must not have directory");
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_RATIONAL);
            long[][] tiffRationals = new long[6][3];
            new TIFFField(t, TIFFTag.TIFF_RATIONAL, tiffRationals.length, tiffRationals);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_SRATIONAL);
            int[][] tiffSRationals = new int[6][3];
            new TIFFField(t, TIFFTag.TIFF_SRATIONAL, tiffSRationals.length, tiffSRationals);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_LONG);
            long[] tiffLongs = new long[] { 0, -7, 10 };
            new TIFFField(t, TIFFTag.TIFF_LONG, tiffLongs.length, tiffLongs);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_LONG);
            long[] tiffLongs = new long[] { 0, 7, 0x100000000L };
            new TIFFField(t, TIFFTag.TIFF_LONG, tiffLongs.length, tiffLongs);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_IFD_POINTER);
            long[] tiffLongs = new long[] { -7 };
            new TIFFField(t, TIFFTag.TIFF_IFD_POINTER, tiffLongs.length, tiffLongs);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_IFD_POINTER);
            long[] tiffLongs = new long[] { 0x100000000L };
            new TIFFField(t, TIFFTag.TIFF_IFD_POINTER, tiffLongs.length, tiffLongs);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_RATIONAL);
            long[][] tiffRationals = new long[][] { { 10, 2 }, { 1, -3 }, { 4, 7 } };
            new TIFFField(t, TIFFTag.TIFF_RATIONAL, tiffRationals.length, tiffRationals);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        ok = false;
        try {
            TIFFTag t = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_RATIONAL);
            long[][] tiffRationals = new long[][] { { 10, 2 }, { 0x100000000L, 3 }, { 4, 7 } };
            new TIFFField(t, TIFFTag.TIFF_RATIONAL, tiffRationals.length, tiffRationals);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        List<TIFFTag> tags = new ArrayList<>();
        tags.add(tag);
        TIFFTagSet[] sets = { new TIFFTagSet(tags) };
        TIFFDirectory dir = new TIFFDirectory(sets, null);
        ok = false;
        try {
            new TIFFField(null, type, 4L, dir);
        } catch (NullPointerException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "null tag");
        ok = false;
        try {
            new TIFFField(tag, type, 0L, dir);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "non-positive offset");
        long offset = 4;
        for (int t = MIN_TYPE; t <= MAX_TYPE; t++) {
            tag = new TIFFTag(NAME, NUM, 1 << t);
            if (t == TIFFTag.TIFF_LONG || t == TIFFTag.TIFF_IFD_POINTER) {
                f = new TIFFField(tag, t, offset, dir);
                check(f.hasDirectory(), "must have directory");
                check(f.getDirectory().getTag(NUM).getName().equals(NAME), "invalid tag name");
                check(f.getCount() == 1, "invalid count");
                check(f.getAsLong(0) == offset, "invalid offset");
            } else {
                ok = false;
                try {
                    new TIFFField(tag, t, offset, dir);
                } catch (IllegalArgumentException e) {
                    ok = true;
                }
                check(ok, CONSTRUCT + "invalid data type");
            }
        }
        type = TIFFTag.TIFF_IFD_POINTER;
        tag = new TIFFTag(NAME, NUM, 1 << type);
        ok = false;
        try {
            new TIFFField(tag, type, offset, null);
        } catch (NullPointerException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "null TIFFDirectory");
        type = TIFFTag.TIFF_LONG;
        tag = new TIFFTag(NAME, NUM, 1 << type);
        ok = false;
        try {
            new TIFFField(tag, type, offset, null);
        } catch (NullPointerException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "null TIFFDirectory");
    }

    private void testTypes() {
        boolean ok = false;
        try {
            TIFFField.getTypeName(MIN_TYPE - 1);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "invalid data type number used");
        ok = false;
        try {
            TIFFField.getTypeName(MAX_TYPE + 1);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "invalid data type number used");
        for (int type = MIN_TYPE; type <= MAX_TYPE; type++) {
            String name = TIFFField.getTypeName(type);
            check(TIFFField.getTypeByName(name) == type, "invalid type");
        }
        for (int type = MIN_TYPE; type <= MAX_TYPE; type++) {
            TIFFTag tag = new TIFFTag(NAME, NUM, 1 << type);
            TIFFField f = new TIFFField(tag, type, 1);
            check(f.getType() == type, "invalid type");
            for (int type2 = MIN_TYPE; type2 <= MAX_TYPE; ++type2) {
                if (type2 != type) {
                    ok = false;
                    try {
                        new TIFFField(tag, type2, 1);
                    } catch (IllegalArgumentException e) {
                        ok = true;
                    }
                    check(ok, "invalid type was successfully set");
                }
            }
        }
    }

    private void testGetAs() {
        int type = TIFFTag.TIFF_SHORT;
        TIFFTag tag = new TIFFTag(NAME, NUM, 1 << TIFFTag.TIFF_SHORT);
        short v = 123;
        TIFFField f = new TIFFField(tag, v);
        check(f.getAsInt(0) == (int) v, "invalid int value");
        check(f.getAsLong(0) == (long) v, "invalid long value");
        check(f.getAsFloat(0) == (float) v, "invalid float value");
        check(f.getAsDouble(0) == (double) v, "invalid double value");
        check(f.getValueAsString(0).equals(Short.toString(v)), "invalid string representation");
        check(f.getAsInts().length == 1, "inavlid array size");
        check((int) v == f.getAsInts()[0], "invalid int value");
        float[] fa = { 0.01f, 1.01f };
        type = TIFFTag.TIFF_FLOAT;
        f = new TIFFField(new TIFFTag(NAME, NUM, 1 << type), type, fa.length, fa);
        check(f.getCount() == fa.length, "invalid count");
        float[] fa2 = f.getAsFloats();
        check(fa2.length == fa.length, "invalid array size");
        for (int i = 0; i < fa.length; i++) {
            check(fa2[i] == fa[i], "invalid value");
            check(f.getAsDouble(i) == fa[i], "invalid value");
            check(f.getAsInt(i) == (int) fa[i], "invalid value");
            check(f.getValueAsString(i).equals(Float.toString(fa[i])), "invalid string representation");
        }
        byte[] ba = { -1, -10, -100 };
        type = TIFFTag.TIFF_BYTE;
        f = new TIFFField(new TIFFTag(NAME, NUM, 1 << type), type, ba.length, ba);
        check(f.getCount() == ba.length, "invalid count");
        byte[] ba2 = f.getAsBytes();
        check(ba2.length == ba.length, "invalid count");
        for (int i = 0; i < ba.length; i++) {
            check(ba[i] == ba2[i], "invalid value");
            check(ba[i] == (byte) f.getAsDouble(i), "invalid value");
            check(ba[i] == (byte) f.getAsLong(i), "invalid value");
            int unsigned = ba[i] & 0xff;
            check(f.getAsInt(i) == unsigned, "must be treated as unsigned");
        }
        char[] ca = { 'a', 'z', 0xffff };
        type = TIFFTag.TIFF_SHORT;
        f = new TIFFField(new TIFFTag(NAME, NUM, 1 << type), type, ca.length, ca);
        check(f.getCount() == ca.length, "invalid count");
        char[] ca2 = f.getAsChars();
        check(ba2.length == ba.length, "invalid count");
        for (int i = 0; i < ca.length; i++) {
            check(ca[i] == ca2[i], "invalid value");
            check(ca[i] == (char) f.getAsDouble(i), "invalid value");
            check(ca[i] == (char) f.getAsLong(i), "invalid value");
            check(ca[i] == (char) f.getAsInt(i), "invalid value");
        }
        type = TIFFTag.TIFF_DOUBLE;
        double[] da = { 0.1, 0.2, 0.3 };
        f = new TIFFField(new TIFFTag(NAME, NUM, 1 << type), type, da.length, da);
        check(!f.isIntegral(), "isIntegral must be false");
        double[] da2 = f.getAsDoubles();
        check(f.getData() instanceof double[], "invalid data type");
        double[] da3 = (double[]) f.getData();
        check((da.length == da2.length) && (da.length == da2.length) && (da.length == f.getCount()), "invalid data count");
        for (int i = 0; i < da.length; ++i) {
            check(da[i] == da2[i], "invalid data");
            check(da[i] == da3[i], "invalid data");
        }
        boolean ok = false;
        try {
            f.getAsShorts();
        } catch (ClassCastException e) {
            ok = true;
        }
        check(ok, "invalid data cast");
        ok = false;
        try {
            f.getAsRationals();
        } catch (ClassCastException e) {
            ok = true;
        }
        check(ok, "invalid data cast");
        ok = false;
        try {
            TIFFField.createArrayForType(TIFFTag.MIN_DATATYPE - 1, 1);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "can create array with invalid datatype");
        ok = false;
        try {
            TIFFField.createArrayForType(TIFFTag.MAX_DATATYPE + 1, 1);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "can create array with invalid datatype");
        ok = false;
        try {
            TIFFField.createArrayForType(TIFFTag.TIFF_FLOAT, -1);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "can create array with negative count");
        int n = 3;
        Object RA = TIFFField.createArrayForType(TIFFTag.TIFF_RATIONAL, n), SRA = TIFFField.createArrayForType(TIFFTag.TIFF_SRATIONAL, n);
        check(RA instanceof long[][], "invalid data type");
        check(SRA instanceof int[][], "invalid data type");
        long[][] ra = (long[][]) RA;
        int[][] sra = (int[][]) SRA;
        check((ra.length == n) && (sra.length == n), "invalid data size");
        for (int i = 0; i < n; i++) {
            check((ra[i].length == 2) && (sra[i].length == 2), "invalid data size");
            ra[i][0] = 1;
            ra[i][1] = 5 + i;
            sra[i][0] = -1;
            sra[i][1] = 5 + i;
        }
        type = TIFFTag.TIFF_RATIONAL;
        TIFFField f1 = new TIFFField(new TIFFTag(NAME, NUM, 1 << type), type, n, ra);
        type = TIFFTag.TIFF_SRATIONAL;
        TIFFField f2 = new TIFFField(new TIFFTag(NAME, NUM, 1 << type), type, n, sra);
        check((f1.getCount() == ra.length) && (f2.getCount() == sra.length), "invalid data count");
        check(f1.getAsRationals().length == n, "invalid data count");
        check(f2.getAsSRationals().length == n, "invalid data count");
        for (int i = 0; i < n; i++) {
            long[] r = f1.getAsRational(i);
            check(r.length == 2, "invalid data format");
            check((r[0] == 1) && (r[1] == i + 5), "invalid data");
            int[] sr = f2.getAsSRational(i);
            check(sr.length == 2, "invalid data format");
            check((sr[0] == -1) && (sr[1] == i + 5), "invalid data");
            String s = Long.toString(r[0]) + "/" + Long.toString(r[1]);
            check(s.equals(f1.getValueAsString(i)), "invalid string representation");
            s = Integer.toString(sr[0]) + "/" + Integer.toString(sr[1]);
            check(s.equals(f2.getValueAsString(i)), "invalid string representation");
            check(f1.getAsInt(i) == (int) (r[0] / r[1]), "invalid result for getAsInt");
            check(f2.getAsInt(i) == (int) (r[0] / r[1]), "invalid result for getAsInt");
        }
        ok = false;
        try {
            f1.getAsRational(ra.length);
        } catch (ArrayIndexOutOfBoundsException e) {
            ok = true;
        }
        check(ok, "invalid index");
        String[] sa = { "-1.e-25", "22", "-1.23E5" };
        type = TIFFTag.TIFF_ASCII;
        f = new TIFFField(new TIFFTag(NAME, NUM, 1 << type), type, sa.length, sa);
        TIFFField cloned = null;
        try {
            cloned = f.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        check(f.getCount() == cloned.getCount(), "invalid cloned field count");
        check(f.getCount() == sa.length, "invalid data count");
        for (int i = 0; i < sa.length; i++) {
            check(sa[i].equals(f.getAsString(i)), "invalid data");
            check(f.getAsInt(i) == (int) Double.parseDouble(sa[i]), "invalid data");
            check(f.getAsDouble(i) == Double.parseDouble(sa[i]), "invalid data");
            check(sa[i].equals(cloned.getAsString(i)), "invalid cloned data");
        }
    }

    private void testCreateFromNode() {
        int type = TIFFTag.TIFF_LONG;
        List<TIFFTag> tags = new ArrayList<>();
        int v = 1234567;
        TIFFTag tag = new TIFFTag(NAME, NUM, 1 << type);
        tags.add(tag);
        TIFFTagSet ts = new TIFFTagSet(tags);
        boolean ok = false;
        try {
            TIFFField.createFromMetadataNode(ts, null);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, "can create TIFFField from a null node");
        TIFFField f = new TIFFField(tag, v);
        Node node = f.getAsNativeNode();
        check(node.getNodeName().equals(f.getClass().getSimpleName()), "invalid node name");
        NamedNodeMap attrs = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            String an = attrs.item(i).getNodeName().toLowerCase();
            String av = attrs.item(i).getNodeValue();
            if (an.contains("name")) {
                check(av.equals(NAME), "invalid tag name");
            } else if (an.contains("number")) {
                check(av.equals(Integer.toString(NUM)), "invalid tag number");
            }
        }
        IIOMetadataNode nok = new IIOMetadataNode("NOK");
        ok = false;
        try {
            TIFFField.createFromMetadataNode(ts, nok);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        check(ok, CONSTRUCT + "invalid node name");
        TIFFField f2 = TIFFField.createFromMetadataNode(ts, node);
        check(f2.getType() == type, "invalid type");
        check(f2.getTagNumber() == NUM, "invalid tag number");
        check(f2.getTag().getName().equals(NAME), "invalid tag name");
        check(f2.getCount() == 1, "invalid count");
        check(f2.getAsInt(0) == v, "invalid value");
    }

    public static void main(String[] args) {
        TIFFFieldTest test = new TIFFFieldTest();
        test.testConstructors();
        test.testCreateFromNode();
        test.testTypes();
        test.testGetAs();
    }
}
