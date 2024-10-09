import java.nio.*;

public class NativeOrder {

    public static void main(String[] args) throws Exception {
        ByteOrder bo = ByteOrder.nativeOrder();
        System.err.println("ByteOrder.nativeOrder:" + bo);
        String arch = System.getProperty("os.arch");
        System.err.println("os.arch:" + arch);
        if (((arch.equals("i386") && (bo != ByteOrder.LITTLE_ENDIAN))) || ((arch.equals("amd64") && (bo != ByteOrder.LITTLE_ENDIAN))) || ((arch.equals("x86_64") && (bo != ByteOrder.LITTLE_ENDIAN))) || ((arch.equals("ppc64") && (bo != ByteOrder.BIG_ENDIAN))) || ((arch.equals("ppc64le") && (bo != ByteOrder.LITTLE_ENDIAN))) || ((arch.equals("s390x") && (bo != ByteOrder.BIG_ENDIAN)))) {
            throw new Exception("Wrong byte order");
        }
        System.err.println("test is OK");
    }
}
