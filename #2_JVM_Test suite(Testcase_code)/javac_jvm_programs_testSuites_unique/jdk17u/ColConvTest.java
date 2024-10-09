import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

public abstract class ColConvTest implements Runnable {

    static final int SI_X = 10;

    static final int SI_Y = 10;

    static final int SI_W = 100;

    static final int SI_H = 100;

    private boolean passed = false;

    static String getCSName(int cs) {
        switch(cs) {
            case ColorSpace.CS_GRAY:
                return "CS_GRAY";
            case ColorSpace.CS_CIEXYZ:
                return "CS_CIEXYZ";
            case ColorSpace.CS_LINEAR_RGB:
                return "CS_LINEAR_RGB";
            case ColorSpace.CS_PYCC:
                return "CS_PYCC";
            case ColorSpace.CS_sRGB:
                return "CS_sRGB";
        }
        return "UNKNOWN";
    }

    static String getDTName(int dType) {
        switch(dType) {
            case DataBuffer.TYPE_BYTE:
                return "TYPE_BYTE";
            case DataBuffer.TYPE_DOUBLE:
                return "TYPE_DOUBLE";
            case DataBuffer.TYPE_FLOAT:
                return "TYPE_FLOAT";
            case DataBuffer.TYPE_INT:
                return "TYPE_INT";
            case DataBuffer.TYPE_SHORT:
                return "TYPE_SHORT";
            case DataBuffer.TYPE_USHORT:
                return "TYPE_USHORT";
            case DataBuffer.TYPE_UNDEFINED:
                return "TYPE_UNDEFINED";
        }
        return "UNKNOWN";
    }

    static String getImageTypeName(int type) {
        switch(type) {
            case BufferedImage.TYPE_INT_ARGB:
                return "TYPE_INT_ARGB";
            case BufferedImage.TYPE_INT_RGB:
                return "TYPE_INT_RGB";
            case BufferedImage.TYPE_INT_BGR:
                return "TYPE_INT_BGR";
            case BufferedImage.TYPE_INT_ARGB_PRE:
                return "TYPE_INT_ARGB_PRE";
            case BufferedImage.TYPE_3BYTE_BGR:
                return "TYPE_3BYTE_BGR";
            case BufferedImage.TYPE_4BYTE_ABGR:
                return "TYPE_4BYTE_ABGR";
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                return "TYPE_4BYTE_ABGR_PRE";
            case BufferedImage.TYPE_BYTE_BINARY:
                return "TYPE_BYTE_BINARY";
            case BufferedImage.TYPE_BYTE_GRAY:
                return "TYPE_BYTE_GRAY";
            case BufferedImage.TYPE_BYTE_INDEXED:
                return "TYPE_BYTE_INDEXED";
            case BufferedImage.TYPE_USHORT_555_RGB:
                return "TYPE_USHORT_555_RGB";
            case BufferedImage.TYPE_USHORT_565_RGB:
                return "TYPE_USHORT_565_RGB";
            case BufferedImage.TYPE_USHORT_GRAY:
                return "TYPE_USHORT_GRAY";
        }
        return "UNKNOWN";
    }

    public abstract void init();

    public abstract void runTest();

    public final void run() {
        try {
            runTest();
            passed = true;
        } catch (Throwable ex) {
            ex.printStackTrace();
            passed = false;
            throw new RuntimeException(ex);
        }
    }

    public boolean isPassed() {
        return passed;
    }

    private static Boolean isOpenProfile = null;

    public static boolean isOpenProfile() {
        if (isOpenProfile == null) {
            ICC_Profile p = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
            byte[] h = p.getData(ICC_Profile.icSigHead);
            if (h == null || h.length < 128) {
                throw new RuntimeException("Test failed: invalid sRGB header");
            }
            final byte[] lcmsID = new byte[] { (byte) 0x6c, (byte) 0x63, (byte) 0x6d, (byte) 0x73 };
            int off = ICC_Profile.icHdrCmmId;
            isOpenProfile = ((h[off + 0] == lcmsID[0]) && (h[off + 1] == lcmsID[1]) && (h[off + 2] == lcmsID[2]) && (h[off + 3] == lcmsID[3]));
        }
        return isOpenProfile;
    }
}
