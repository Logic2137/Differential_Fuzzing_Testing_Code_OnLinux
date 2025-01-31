import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.zip.Checksum;

public class ChecksumBase {

    private static final byte[] BYTES_123456789 = "123456789".getBytes(StandardCharsets.US_ASCII);

    public static void testAll(Checksum checksum, long expected) {
        testBytes(checksum, expected);
        testByteArray(checksum, expected);
        testWrappedByteBuffer(checksum, expected);
        testReadonlyByteBuffer(checksum, expected);
        testDirectByteBuffer(checksum, expected);
        testByteArrayOffset(checksum, expected);
        testDirectByteBufferOffset(checksum, expected);
        testLittleEndianDirectByteBufferOffset(checksum, expected);
        testWrappedByteBufferOffset(checksum, expected);
        testLittleEndianWrappedByteBufferOffset(checksum, expected);
        testReadonlyByteBufferOffset(checksum, expected);
        testLittleEndianReadonlyByteBufferOffset(checksum, expected);
    }

    private static void testBytes(Checksum checksum, long expected) {
        checksum.reset();
        for (byte bits : BYTES_123456789) {
            checksum.update(bits);
        }
        checkChecksum(checksum, expected);
    }

    private static void testByteArray(Checksum checksum, long expected) {
        checksum.reset();
        checksum.update(BYTES_123456789);
        checkChecksum(checksum, expected);
    }

    private static void testWrappedByteBuffer(Checksum checksum, long expected) {
        checksum.reset();
        ByteBuffer bb = ByteBuffer.wrap(BYTES_123456789);
        checksum.update(bb);
        checkChecksum(checksum, expected);
    }

    private static void testReadonlyByteBuffer(Checksum checksum, long expected) {
        checksum.reset();
        ByteBuffer bb = ByteBuffer.wrap(BYTES_123456789).asReadOnlyBuffer();
        checksum.update(bb);
        checkChecksum(checksum, expected);
    }

    private static void testDirectByteBuffer(Checksum checksum, long expected) {
        checksum.reset();
        ByteBuffer bb = ByteBuffer.allocateDirect(BYTES_123456789.length);
        bb.put(BYTES_123456789);
        bb.rewind();
        checksum.update(bb);
        checkChecksum(checksum, expected);
    }

    private static void checkChecksum(Checksum checksum, long expected) {
        if (checksum.getValue() != expected) {
            throw new RuntimeException("Calculated checksum result was invalid." + " Expected " + Long.toHexString(expected) + ", but got " + Long.toHexString(checksum.getValue()) + ".");
        }
    }

    private static void testByteArrayOffset(Checksum checksum, long expected) {
        byte[] unaligned_bytes_123456789 = new byte[BYTES_123456789.length + 64];
        for (int i = 0; i < unaligned_bytes_123456789.length - BYTES_123456789.length; i++) {
            checksum.reset();
            System.arraycopy(BYTES_123456789, 0, unaligned_bytes_123456789, i, BYTES_123456789.length);
            checksum.update(unaligned_bytes_123456789, i, BYTES_123456789.length);
            checkChecksumOffset(checksum, expected, i);
        }
    }

    private static void testDirectByteBufferOffset(Checksum checksum, long expected) {
        byte[] unaligned_bytes_123456789 = new byte[BYTES_123456789.length + 64];
        for (int i = 0; i < unaligned_bytes_123456789.length - BYTES_123456789.length; i++) {
            checksum.reset();
            ByteBuffer bb = ByteBuffer.allocateDirect(unaligned_bytes_123456789.length);
            System.arraycopy(BYTES_123456789, 0, unaligned_bytes_123456789, i, BYTES_123456789.length);
            bb.put(unaligned_bytes_123456789);
            bb.position(i);
            bb.limit(i + BYTES_123456789.length);
            checksum.update(bb);
            checkChecksumOffset(checksum, expected, i);
        }
    }

    private static void testLittleEndianDirectByteBufferOffset(Checksum checksum, long expected) {
        byte[] unaligned_bytes_123456789 = new byte[BYTES_123456789.length + 64];
        for (int i = 0; i < unaligned_bytes_123456789.length - BYTES_123456789.length; i++) {
            checksum.reset();
            ByteBuffer bb = ByteBuffer.allocateDirect(unaligned_bytes_123456789.length);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            System.arraycopy(BYTES_123456789, 0, unaligned_bytes_123456789, i, BYTES_123456789.length);
            bb.put(unaligned_bytes_123456789);
            bb.position(i);
            bb.limit(i + BYTES_123456789.length);
            checksum.update(bb);
            checkChecksumOffset(checksum, expected, i);
        }
    }

    private static void testWrappedByteBufferOffset(Checksum checksum, long expected) {
        byte[] unaligned_bytes_123456789 = new byte[BYTES_123456789.length + 64];
        for (int i = 0; i < unaligned_bytes_123456789.length - BYTES_123456789.length; i++) {
            checksum.reset();
            System.arraycopy(BYTES_123456789, 0, unaligned_bytes_123456789, i, BYTES_123456789.length);
            ByteBuffer bb = ByteBuffer.wrap(unaligned_bytes_123456789);
            bb.position(i);
            bb.limit(i + BYTES_123456789.length);
            checksum.update(bb);
            checkChecksumOffset(checksum, expected, i);
        }
    }

    private static void testLittleEndianWrappedByteBufferOffset(Checksum checksum, long expected) {
        byte[] unaligned_bytes_123456789 = new byte[BYTES_123456789.length + 64];
        for (int i = 0; i < unaligned_bytes_123456789.length - BYTES_123456789.length; i++) {
            checksum.reset();
            System.arraycopy(BYTES_123456789, 0, unaligned_bytes_123456789, i, BYTES_123456789.length);
            ByteBuffer bb = ByteBuffer.wrap(unaligned_bytes_123456789);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.position(i);
            bb.limit(i + BYTES_123456789.length);
            checksum.update(bb);
            checkChecksumOffset(checksum, expected, i);
        }
    }

    private static void testReadonlyByteBufferOffset(Checksum checksum, long expected) {
        byte[] unaligned_bytes_123456789 = new byte[BYTES_123456789.length + 64];
        for (int i = 0; i < unaligned_bytes_123456789.length - BYTES_123456789.length; i++) {
            checksum.reset();
            System.arraycopy(BYTES_123456789, 0, unaligned_bytes_123456789, i, BYTES_123456789.length);
            ByteBuffer bb = ByteBuffer.wrap(unaligned_bytes_123456789).asReadOnlyBuffer();
            bb.position(i);
            bb.limit(i + BYTES_123456789.length);
            checksum.update(bb);
            checkChecksumOffset(checksum, expected, i);
        }
    }

    private static void testLittleEndianReadonlyByteBufferOffset(Checksum checksum, long expected) {
        byte[] unaligned_bytes_123456789 = new byte[BYTES_123456789.length + 64];
        for (int i = 0; i < unaligned_bytes_123456789.length - BYTES_123456789.length; i++) {
            checksum.reset();
            System.arraycopy(BYTES_123456789, 0, unaligned_bytes_123456789, i, BYTES_123456789.length);
            ByteBuffer bb = ByteBuffer.wrap(unaligned_bytes_123456789).asReadOnlyBuffer();
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.position(i);
            bb.limit(i + BYTES_123456789.length);
            checksum.update(bb);
            checkChecksumOffset(checksum, expected, i);
        }
    }

    private static void checkChecksumOffset(Checksum checksum, long expected, int offset) {
        if (checksum.getValue() != expected) {
            throw new RuntimeException("Calculated CRC32C result was invalid. Array offset " + offset + ". Expected: " + Long.toHexString(expected) + ", Got: " + Long.toHexString(checksum.getValue()));
        }
    }
}
