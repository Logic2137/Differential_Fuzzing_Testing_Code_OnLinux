
package compiler.c2;

public class Test6732154 {

    private byte[] ascii85Encode(byte[] inArr) {
        byte[] outArr = new byte[((inArr.length + 4) * 5 / 4) + 2];
        long p1 = 85;
        long p2 = p1 * p1;
        long p3 = p1 * p2;
        long p4 = p1 * p3;
        byte pling = '!';
        int i = 0;
        int olen = 0;
        long val, rem;
        while (i + 3 < inArr.length) {
            val = ((long) ((inArr[i++] & 0xff)) << 24) + ((long) ((inArr[i++] & 0xff)) << 16) + ((long) ((inArr[i++] & 0xff)) << 8) + ((long) (inArr[i++] & 0xff));
            if (val == 0) {
                outArr[olen++] = 'z';
            } else {
                rem = val;
                outArr[olen++] = (byte) (rem / p4 + pling);
                rem = rem % p4;
                outArr[olen++] = (byte) (rem / p3 + pling);
                rem = rem % p3;
                outArr[olen++] = (byte) (rem / p2 + pling);
                rem = rem % p2;
                outArr[olen++] = (byte) (rem / p1 + pling);
                rem = rem % p1;
                outArr[olen++] = (byte) (rem + pling);
            }
        }
        if (i < inArr.length) {
            int n = inArr.length - i;
            val = 0;
            while (i < inArr.length) {
                val = (val << 8) + (inArr[i++] & 0xff);
            }
            int append = 4 - n;
            while (append-- > 0) {
                val = val << 8;
            }
            byte[] c = new byte[5];
            rem = val;
            c[0] = (byte) (rem / p4 + pling);
            rem = rem % p4;
            c[1] = (byte) (rem / p3 + pling);
            rem = rem % p3;
            c[2] = (byte) (rem / p2 + pling);
            rem = rem % p2;
            c[3] = (byte) (rem / p1 + pling);
            rem = rem % p1;
            c[4] = (byte) (rem + pling);
            for (int b = 0; b < n + 1; b++) {
                outArr[olen++] = c[b];
            }
        }
        outArr[olen++] = '~';
        outArr[olen++] = '>';
        byte[] retArr = new byte[olen];
        System.arraycopy(outArr, 0, retArr, 0, olen);
        return retArr;
    }

    public static void main(String[] args) {
        new Test6732154().ascii85Encode(new byte[0]);
        System.out.println("Test passed.");
    }
}
