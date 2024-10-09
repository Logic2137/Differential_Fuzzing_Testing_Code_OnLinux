import java.util.*;
import java.nio.*;
import java.nio.charset.*;

public class Test6896617 {

    final static int SIZE = 256;

    public static void main(String[] args) {
        String csn = "ISO-8859-1";
        Charset cs = Charset.forName(csn);
        CharsetEncoder enc = cs.newEncoder();
        enc.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        CharsetDecoder dec = cs.newDecoder();
        dec.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        byte repl = (byte) '?';
        enc.replaceWith(new byte[] { repl });
        sun.nio.cs.ArrayEncoder arrenc = (sun.nio.cs.ArrayEncoder) enc;
        sun.nio.cs.ArrayDecoder arrdec = (sun.nio.cs.ArrayDecoder) dec;
        Random rnd = new Random(0);
        int maxchar = 0xFF;
        char[] a = new char[SIZE];
        byte[] b = new byte[SIZE];
        char[] at = new char[SIZE];
        byte[] bt = new byte[SIZE];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < SIZE; i++) {
            char c = (char) rnd.nextInt(maxchar);
            if (!enc.canEncode(c)) {
                System.out.printf("Something wrong: can't encode c=%03x\n", (int) c);
                System.exit(97);
            }
            a[i] = c;
            b[i] = (byte) c;
            at[i] = (char) -1;
            bt[i] = (byte) -1;
        }
        if (arrenc.encode(a, 0, SIZE, bt) != SIZE || !Arrays.equals(b, bt)) {
            System.out.println("Something wrong: ArrayEncoder.encode failed");
            System.exit(97);
        }
        if (arrdec.decode(b, 0, SIZE, at) != SIZE || !Arrays.equals(a, at)) {
            System.out.println("Something wrong: ArrayDecoder.decode failed");
            System.exit(97);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < SIZE; i++) {
            at[i] = (char) -1;
            bt[i] = (byte) -1;
        }
        ByteBuffer bb = ByteBuffer.wrap(b);
        CharBuffer ba = CharBuffer.wrap(a);
        ByteBuffer bbt = ByteBuffer.wrap(bt);
        CharBuffer bat = CharBuffer.wrap(at);
        if (!enc.encode(ba, bbt, true).isUnderflow() || !Arrays.equals(b, bt)) {
            System.out.println("Something wrong: Encoder.encode failed");
            System.exit(97);
        }
        if (!dec.decode(bb, bat, true).isUnderflow() || !Arrays.equals(a, at)) {
            System.out.println("Something wrong: Decoder.decode failed");
            System.exit(97);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < SIZE; i++) {
            at[i] = (char) -1;
            bt[i] = (byte) -1;
        }
        boolean failed = false;
        int result = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            result += arrenc.encode(a, 0, SIZE, bt);
            result -= arrdec.decode(b, 0, SIZE, at);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            result += arrenc.encode(a, 0, SIZE, bt);
            result -= arrdec.decode(b, 0, SIZE, at);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            result += arrenc.encode(a, 0, SIZE, bt);
            result -= arrdec.decode(b, 0, SIZE, at);
        }
        if (result != 0 || !Arrays.equals(b, bt) || !Arrays.equals(a, at)) {
            failed = true;
            System.out.println("Failed: ArrayEncoder.encode char[" + SIZE + "] and ArrayDecoder.decode byte[" + SIZE + "]");
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < SIZE; i++) {
            at[i] = (char) -1;
            bt[i] = (byte) -1;
        }
        boolean is_underflow = true;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            ba.clear();
            bb.clear();
            bat.clear();
            bbt.clear();
            boolean enc_res = enc.encode(ba, bbt, true).isUnderflow();
            boolean dec_res = dec.decode(bb, bat, true).isUnderflow();
            is_underflow = is_underflow && enc_res && dec_res;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < SIZE; i++) {
            at[i] = (char) -1;
            bt[i] = (byte) -1;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            ba.clear();
            bb.clear();
            bat.clear();
            bbt.clear();
            boolean enc_res = enc.encode(ba, bbt, true).isUnderflow();
            boolean dec_res = dec.decode(bb, bat, true).isUnderflow();
            is_underflow = is_underflow && enc_res && dec_res;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < SIZE; i++) {
            at[i] = (char) -1;
            bt[i] = (byte) -1;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            ba.clear();
            bb.clear();
            bat.clear();
            bbt.clear();
            boolean enc_res = enc.encode(ba, bbt, true).isUnderflow();
            boolean dec_res = dec.decode(bb, bat, true).isUnderflow();
            is_underflow = is_underflow && enc_res && dec_res;
        }
        if (!is_underflow || !Arrays.equals(b, bt) || !Arrays.equals(a, at)) {
            failed = true;
            System.out.println("Failed: Encoder.encode char[" + SIZE + "] and Decoder.decode byte[" + SIZE + "]");
        }
        System.out.println("Testing different source and destination sizes");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 1; i <= SIZE; i++) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int j = 1; j <= SIZE; j++) {
                bt = new byte[j];
                result = arrenc.encode(a, 0, i, bt);
                int l = Math.min(i, j);
                if (result != l) {
                    failed = true;
                    System.out.println("Failed: encode char[" + i + "] to byte[" + j + "]: result = " + result + ", expected " + l);
                }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
                for (int k = 0; k < l; k++) {
                    if (bt[k] != b[k]) {
                        failed = true;
                        System.out.println("Failed: encoded byte[" + k + "] (" + bt[k] + ") != " + b[k]);
                    }
                }
                int sz = SIZE - i + 1;
                result = arrenc.encode(a, i - 1, sz, bt);
                l = Math.min(sz, j);
                if (result != l) {
                    failed = true;
                    System.out.println("Failed: encode char[" + sz + "] to byte[" + j + "]: result = " + result + ", expected " + l);
                }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
                for (int k = 0; k < l; k++) {
                    if (bt[k] != b[i + k - 1]) {
                        failed = true;
                        System.out.println("Failed: encoded byte[" + k + "] (" + bt[k] + ") != " + b[i + k - 1]);
                    }
                }
            }
        }
        System.out.println("Testing big char");
        byte orig = (byte) 'A';
        bt = new byte[SIZE];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 1; i <= SIZE; i++) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int j = 0; j < i; j++) {
                a[j] += 0x100;
                bt[j] = orig;
                result = arrenc.encode(a, 0, i, bt);
                if (result != i) {
                    failed = true;
                    System.out.println("Failed: encode char[" + i + "] to byte[" + i + "]: result = " + result + ", expected " + i);
                }
                if (bt[j] != repl) {
                    failed = true;
                    System.out.println("Failed: encoded replace byte[" + j + "] (" + bt[j] + ") != " + repl);
                }
                bt[j] = b[j];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
                for (int k = 0; k < i; k++) {
                    if (bt[k] != b[k]) {
                        failed = true;
                        System.out.println("Failed: encoded byte[" + k + "] (" + bt[k] + ") != " + b[k]);
                    }
                }
                a[j] -= 0x100;
            }
        }
        int itrs = Integer.getInteger("iterations", 1000000);
        int size = Integer.getInteger("size", 256);
        a = new char[size];
        b = new byte[size];
        bt = new byte[size];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < size; i++) {
            char c = (char) rnd.nextInt(maxchar);
            if (!enc.canEncode(c)) {
                System.out.printf("Something wrong: can't encode c=%03x\n", (int) c);
                System.exit(97);
            }
            a[i] = c;
            b[i] = (byte) -1;
            bt[i] = (byte) c;
        }
        ba = CharBuffer.wrap(a);
        bb = ByteBuffer.wrap(b);
        boolean enc_res = enc.encode(ba, bb, true).isUnderflow();
        if (!enc_res || !Arrays.equals(b, bt)) {
            failed = true;
            System.out.println("Failed 1: Encoder.encode char[" + size + "]");
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < size; i++) {
            b[i] = (byte) -1;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            ba.clear();
            bb.clear();
            enc_res = enc_res && enc.encode(ba, bb, true).isUnderflow();
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < size; i++) {
            b[i] = (byte) -1;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            ba.clear();
            bb.clear();
            enc_res = enc_res && enc.encode(ba, bb, true).isUnderflow();
        }
        if (!enc_res || !Arrays.equals(b, bt)) {
            failed = true;
            System.out.println("Failed 2: Encoder.encode char[" + size + "]");
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < size; i++) {
            b[i] = (byte) -1;
        }
        System.out.println("Testing ISO_8859_1$Encode.encodeArrayLoop() performance");
        long start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < itrs; i++) {
            ba.clear();
            bb.clear();
            enc_res = enc_res && enc.encode(ba, bb, true).isUnderflow();
        }
        long end = System.currentTimeMillis();
        if (!enc_res || !Arrays.equals(b, bt)) {
            failed = true;
            System.out.println("Failed 3: Encoder.encode char[" + size + "]");
        } else {
            System.out.println("size: " + size + " time: " + (end - start));
        }
        result = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < size; i++) {
            b[i] = (byte) -1;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            result += arrenc.encode(a, 0, size, b);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < size; i++) {
            b[i] = (byte) -1;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10000; i++) {
            result += arrenc.encode(a, 0, size, b);
        }
        if (result != size * 20000 || !Arrays.equals(b, bt)) {
            failed = true;
            System.out.println("Failed 1: ArrayEncoder.encode char[" + SIZE + "]");
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < size; i++) {
            b[i] = (byte) -1;
        }
        System.out.println("Testing ISO_8859_1$Encode.encode() performance");
        result = 0;
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < itrs; i++) {
            result += arrenc.encode(a, 0, size, b);
        }
        end = System.currentTimeMillis();
        if (!Arrays.equals(b, bt)) {
            failed = true;
            System.out.println("Failed 2: ArrayEncoder.encode char[" + size + "]");
        } else {
            System.out.println("size: " + size + " time: " + (end - start));
        }
        if (failed) {
            System.out.println("FAILED");
            System.exit(97);
        }
        System.out.println("PASSED");
    }
}
