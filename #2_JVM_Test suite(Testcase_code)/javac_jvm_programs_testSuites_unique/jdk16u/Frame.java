

import java.nio.ByteBuffer;


final class Frame {

    final Opcode opcode;
    final ByteBuffer data;
    final boolean last;

    public Frame(Opcode opcode, ByteBuffer data, boolean last) {
        this.opcode = opcode;
        
        this.data = ByteBuffer.allocate(data.remaining()).put(data.slice()).flip();
        this.last = last;
    }

    static final int MAX_HEADER_SIZE_BYTES = 2 + 8 + 4;
    static final int MAX_CONTROL_FRAME_PAYLOAD_SIZE = 125;

    enum Opcode {

        CONTINUATION   (0x0),
        TEXT           (0x1),
        BINARY         (0x2),
        NON_CONTROL_0x3(0x3),
        NON_CONTROL_0x4(0x4),
        NON_CONTROL_0x5(0x5),
        NON_CONTROL_0x6(0x6),
        NON_CONTROL_0x7(0x7),
        CLOSE          (0x8),
        PING           (0x9),
        PONG           (0xA),
        CONTROL_0xB    (0xB),
        CONTROL_0xC    (0xC),
        CONTROL_0xD    (0xD),
        CONTROL_0xE    (0xE),
        CONTROL_0xF    (0xF);

        private static final Opcode[] opcodes;

        static {
            Opcode[] values = values();
            opcodes = new Opcode[values.length];
            for (Opcode c : values) {
                opcodes[c.code] = c;
            }
        }

        private final byte code;

        Opcode(int code) {
            this.code = (byte) code;
        }

        boolean isControl() {
            return (code & 0x8) != 0;
        }

        static Opcode ofCode(int code) {
            return opcodes[code & 0xF];
        }
    }

    
    static final class Masker {

        
        private final ByteBuffer acc = ByteBuffer.allocate(8);
        private final int[] maskBytes = new int[4];
        private int offset;
        private long maskLong;

        
        static void transferMasking(ByteBuffer src, ByteBuffer dst, int mask) {
            if (src.remaining() > dst.remaining()) {
                throw new IllegalArgumentException();
            }
            new Masker().mask(mask).transferMasking(src, dst);
        }

        
        Masker mask(int value) {
            acc.clear().putInt(value).putInt(value).flip();
            for (int i = 0; i < maskBytes.length; i++) {
                maskBytes[i] = acc.get(i);
            }
            offset = 0;
            maskLong = acc.getLong(0);
            return this;
        }

        
        Masker transferMasking(ByteBuffer src, ByteBuffer dst) {
            begin(src, dst);
            loop(src, dst);
            end(src, dst);
            return this;
        }

        
        private void begin(ByteBuffer src, ByteBuffer dst) {
            if (offset == 0) { 
                return;
            }
            int i = src.position(), j = dst.position();
            final int srcLim = src.limit(), dstLim = dst.limit();
            for (; offset < 4 && i < srcLim && j < dstLim; i++, j++, offset++)
            {
                dst.put(j, (byte) (src.get(i) ^ maskBytes[offset]));
            }
            offset &= 3; 
            src.position(i);
            dst.position(j);
        }

        
        private void loop(ByteBuffer src, ByteBuffer dst) {
            int i = src.position();
            int j = dst.position();
            final int srcLongLim = src.limit() - 7, dstLongLim = dst.limit() - 7;
            for (; i < srcLongLim && j < dstLongLim; i += 8, j += 8) {
                dst.putLong(j, src.getLong(i) ^ maskLong);
            }
            if (i > src.limit()) {
                src.position(i - 8);
            } else {
                src.position(i);
            }
            if (j > dst.limit()) {
                dst.position(j - 8);
            } else {
                dst.position(j);
            }
        }

        
        private void end(ByteBuffer src, ByteBuffer dst) {
            assert Math.min(src.remaining(), dst.remaining()) < 8;
            final int srcLim = src.limit(), dstLim = dst.limit();
            int i = src.position(), j = dst.position();
            for (; i < srcLim && j < dstLim;
                 i++, j++, offset = (offset + 1) & 3) 
            {
                dst.put(j, (byte) (src.get(i) ^ maskBytes[offset]));
            }
            src.position(i);
            dst.position(j);
        }
    }

    
    static final class HeaderWriter {

        private char firstChar;
        private long payloadLen;
        private int maskingKey;
        private boolean mask;

        HeaderWriter fin(boolean value) {
            if (value) {
                firstChar |=  0b10000000_00000000;
            } else {
                firstChar &= ~0b10000000_00000000;
            }
            return this;
        }

        HeaderWriter rsv1(boolean value) {
            if (value) {
                firstChar |=  0b01000000_00000000;
            } else {
                firstChar &= ~0b01000000_00000000;
            }
            return this;
        }

        HeaderWriter rsv2(boolean value) {
            if (value) {
                firstChar |=  0b00100000_00000000;
            } else {
                firstChar &= ~0b00100000_00000000;
            }
            return this;
        }

        HeaderWriter rsv3(boolean value) {
            if (value) {
                firstChar |=  0b00010000_00000000;
            } else {
                firstChar &= ~0b00010000_00000000;
            }
            return this;
        }

        HeaderWriter opcode(Opcode value) {
            firstChar = (char) ((firstChar & 0xF0FF) | (value.code << 8));
            return this;
        }

        HeaderWriter payloadLen(long value) {
            if (value < 0) {
                throw new IllegalArgumentException("Negative: " + value);
            }
            payloadLen = value;
            firstChar &= 0b11111111_10000000; 
            if (payloadLen < 126) {
                firstChar |= payloadLen;
            } else if (payloadLen < 65536) {
                firstChar |= 126;
            } else {
                firstChar |= 127;
            }
            return this;
        }

        HeaderWriter mask(int value) {
            firstChar |= 0b00000000_10000000;
            maskingKey = value;
            mask = true;
            return this;
        }

        HeaderWriter noMask() {
            firstChar &= ~0b00000000_10000000;
            mask = false;
            return this;
        }

        
        void write(ByteBuffer buffer) {
            buffer.putChar(firstChar);
            if (payloadLen >= 126) {
                if (payloadLen < 65536) {
                    buffer.putChar((char) payloadLen);
                } else {
                    buffer.putLong(payloadLen);
                }
            }
            if (mask) {
                buffer.putInt(maskingKey);
            }
        }
    }

    
    interface Consumer {

        void fin(boolean value);

        void rsv1(boolean value);

        void rsv2(boolean value);

        void rsv3(boolean value);

        void opcode(Opcode value);

        void mask(boolean value);

        void payloadLen(long value);

        void maskingKey(int value);

        
        void payloadData(ByteBuffer data);

        void endFrame();
    }

    
    static final class Reader {

        private static final int AWAITING_FIRST_BYTE  =  1;
        private static final int AWAITING_SECOND_BYTE =  2;
        private static final int READING_16_LENGTH    =  4;
        private static final int READING_64_LENGTH    =  8;
        private static final int READING_MASK         = 16;
        private static final int READING_PAYLOAD      = 32;

        
        private final ByteBuffer accumulator = ByteBuffer.allocate(8);
        private int state = AWAITING_FIRST_BYTE;
        private boolean mask;
        private long remainingPayloadLength;

        
        void readFrame(ByteBuffer input, Consumer consumer) {
            loop:
            while (true) {
                byte b;
                switch (state) {
                    case AWAITING_FIRST_BYTE:
                        if (!input.hasRemaining()) {
                            break loop;
                        }
                        b = input.get();
                        consumer.fin( (b & 0b10000000) != 0);
                        consumer.rsv1((b & 0b01000000) != 0);
                        consumer.rsv2((b & 0b00100000) != 0);
                        consumer.rsv3((b & 0b00010000) != 0);
                        consumer.opcode(Opcode.ofCode(b));
                        state = AWAITING_SECOND_BYTE;
                        continue loop;
                    case AWAITING_SECOND_BYTE:
                        if (!input.hasRemaining()) {
                            break loop;
                        }
                        b = input.get();
                        consumer.mask(mask = (b & 0b10000000) != 0);
                        byte p1 = (byte) (b & 0b01111111);
                        if (p1 < 126) {
                            assert p1 >= 0 : p1;
                            consumer.payloadLen(remainingPayloadLength = p1);
                            state = mask ? READING_MASK : READING_PAYLOAD;
                        } else if (p1 < 127) {
                            state = READING_16_LENGTH;
                        } else {
                            state = READING_64_LENGTH;
                        }
                        continue loop;
                    case READING_16_LENGTH:
                        if (!input.hasRemaining()) {
                            break loop;
                        }
                        b = input.get();
                        if (accumulator.put(b).position() < 2) {
                            continue loop;
                        }
                        remainingPayloadLength = accumulator.flip().getChar();
                        if (remainingPayloadLength < 126) {
                            throw notMinimalEncoding(remainingPayloadLength);
                        }
                        consumer.payloadLen(remainingPayloadLength);
                        accumulator.clear();
                        state = mask ? READING_MASK : READING_PAYLOAD;
                        continue loop;
                    case READING_64_LENGTH:
                        if (!input.hasRemaining()) {
                            break loop;
                        }
                        b = input.get();
                        if (accumulator.put(b).position() < 8) {
                            continue loop;
                        }
                        remainingPayloadLength = accumulator.flip().getLong();
                        if (remainingPayloadLength < 0) {
                            throw negativePayload(remainingPayloadLength);
                        } else if (remainingPayloadLength < 65536) {
                            throw notMinimalEncoding(remainingPayloadLength);
                        }
                        consumer.payloadLen(remainingPayloadLength);
                        accumulator.clear();
                        state = mask ? READING_MASK : READING_PAYLOAD;
                        continue loop;
                    case READING_MASK:
                        if (!input.hasRemaining()) {
                            break loop;
                        }
                        b = input.get();
                        if (accumulator.put(b).position() != 4) {
                            continue loop;
                        }
                        consumer.maskingKey(accumulator.flip().getInt());
                        accumulator.clear();
                        state = READING_PAYLOAD;
                        continue loop;
                    case READING_PAYLOAD:
                        
                        
                        int deliverable = (int) Math.min(remainingPayloadLength,
                                                         input.remaining());
                        int oldLimit = input.limit();
                        input.limit(input.position() + deliverable);
                        if (deliverable != 0 || remainingPayloadLength == 0) {
                            consumer.payloadData(input);
                        }
                        int consumed = deliverable - input.remaining();
                        if (consumed < 0) {
                            
                            throw new InternalError();
                        }
                        input.limit(oldLimit);
                        remainingPayloadLength -= consumed;
                        if (remainingPayloadLength == 0) {
                            consumer.endFrame();
                            state = AWAITING_FIRST_BYTE;
                        }
                        break loop;
                    default:
                        throw new InternalError(String.valueOf(state));
                }
            }
        }

        private static IllegalArgumentException negativePayload(long payloadLength)
        {
            return new IllegalArgumentException("Negative payload length: "
                                                        + payloadLength);
        }

        private static IllegalArgumentException notMinimalEncoding(long payloadLength)
        {
            return new IllegalArgumentException("Not minimally-encoded payload length:"
                                                      + payloadLength);
        }
    }
}
