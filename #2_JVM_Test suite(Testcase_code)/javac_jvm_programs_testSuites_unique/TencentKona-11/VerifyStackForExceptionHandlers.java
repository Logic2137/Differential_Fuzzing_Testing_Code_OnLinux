public class VerifyStackForExceptionHandlers extends ClassLoader {

    public static void main(String[] argv) throws Exception {
        VerifyStackForExceptionHandlers t = new VerifyStackForExceptionHandlers();
        try {
            t.loadGoodClass();
        } catch (VerifyError e) {
            throw new Exception("FAIL: should be no VerifyError for class A");
        }
        try {
            t.loadBadClass();
            throw new Exception("FAIL: should be a VerifyError for class B");
        } catch (VerifyError e) {
            System.out.println("PASS");
        }
    }

    private void loadGoodClass() {
        long[] cls_data = { 0xcafebabe00000031L, 0x000e0a0003000b07L, 0x000c07000d010006L, 0x3c696e69743e0100L, 0x0328295601000443L, 0x6f646501000f4c69L, 0x6e654e756d626572L, 0x5461626c65010001L, 0x6601000a536f7572L, 0x636546696c650100L, 0x06412e6a6176610cL, 0x0004000501000141L, 0x0100106a6176612fL, 0x6c616e672f4f626aL, 0x6563740021000200L, 0x0300000000000200L, 0x0100040005000100L, 0x060000001d000100L, 0x01000000052ab700L, 0x01b1000000010007L, 0x0000000600010000L, 0x0001000900080005L, 0x0001000600000019L, 0x0000000000000001L, 0xb100000001000700L, 0x0000060001000000L, 0x0200010009000000L, 0x02000a0000000000L };
        final int EXTRA = 5;
        byte[] cf_bytes = toByteArray(cls_data);
        Class c = defineClass("A", cf_bytes, 0, cf_bytes.length - EXTRA);
        try {
            c.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private void loadBadClass() throws VerifyError {
        long[] cls_data = { 0xcafebabe00000031L, 0x00120a000400060aL, 0x000d00030c000f00L, 0x0a0700050100106aL, 0x6176612f6c616e67L, 0x2f4f626a6563740cL, 0x0011000a01000a53L, 0x6f7572636546696cL, 0x6507000901001e6aL, 0x6176612f6c616e67L, 0x2f4e756c6c506f69L, 0x6e74657245786365L, 0x7074696f6e010003L, 0x282956010006422eL, 0x6a61736d01000443L, 0x6f646507000e0100L, 0x0142010001670100L, 0x01660100063c696eL, 0x69743e0021000d00L, 0x0400000000000300L, 0x010011000a000100L, 0x0c00000011000100L, 0x01000000052ab700L, 0x01b1000000000009L, 0x000f000a0001000cL, 0x0000000d00000000L, 0x00000001b1000000L, 0x0000090010000a00L, 0x01000c0000001c00L, 0x00000100000008b8L, 0x0002a700044bb100L, 0x0100000003000600L, 
        0x0800000001000700L, 0x000002000b000000L };
        final int EXTRA = 3;
        byte[] cf_bytes = toByteArray(cls_data);
        Class c = defineClass("B", cf_bytes, 0, cf_bytes.length - EXTRA);
        try {
            c.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
    }

    static private byte[] toByteArray(long[] arr) {
        java.nio.ByteBuffer bbuf = java.nio.ByteBuffer.allocate(arr.length * 8);
        bbuf.asLongBuffer().put(java.nio.LongBuffer.wrap(arr));
        return bbuf.array();
    }
}
