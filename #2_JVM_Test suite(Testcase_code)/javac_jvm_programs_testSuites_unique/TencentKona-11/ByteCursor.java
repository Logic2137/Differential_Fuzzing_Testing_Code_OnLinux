class ByteCursor {

    private int offset;

    private byte[] data;

    public ByteCursor(byte[] data) {
        this.offset = 0;
        this.data = data;
    }

    public int getOffset() {
        return offset;
    }

    public void skipBytes(int n) {
        offset += n;
    }

    public int readUnsignedByte() {
        int val = readUnsignedByteAt(offset);
        offset += 1;
        return val;
    }

    public int readUnsignedByteAt(int offset) {
        return data[offset++] & 0xff;
    }

    public int readUnsignedShort() {
        int val = readUnsignedShortAt(offset);
        offset += 2;
        return val;
    }

    public int readInt() {
        int val = readIntAt(offset);
        offset += 4;
        return val;
    }

    public int readUnsignedShortAt(int offset) {
        int b1 = data[offset++] & 0xff;
        int b2 = data[offset] & 0xff;
        return (b1 << 8) + b2;
    }

    public int readIntAt(int offset) {
        int s1 = readUnsignedShortAt(offset);
        int s2 = readUnsignedShortAt(offset + 2);
        return (s1 << 16) + s2;
    }

    public String readUtf8(int length) throws IllegalStateException {
        char[] str = new char[length];
        int count = 0;
        int pos = 0;
        while (count < length) {
            int c = readUnsignedByte();
            switch(c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    {
                        count++;
                        if (c == '/') {
                            str[pos++] = '.';
                        } else {
                            str[pos++] = (char) c;
                        }
                        break;
                    }
                case 12:
                case 13:
                    {
                        count += 2;
                        int c2 = readUnsignedByte();
                        if ((c2 & 0xC0) != 0x80) {
                            throw new IllegalStateException();
                        }
                        str[pos++] = (char) (((c & 0x1F) << 6) | (c2 & 0x3F));
                        break;
                    }
                case 14:
                    {
                        count += 3;
                        int c2 = readUnsignedByte();
                        int c3 = readUnsignedByte();
                        if ((c2 & 0xC0) != 0x80 || (c3 & 0xC0) != 0x80) {
                            throw new IllegalStateException();
                        }
                        str[pos++] = (char) (((c & 0x0F) << 12) | ((c2 & 0x3F) << 6) | ((c3 & 0x3F) << 0));
                        break;
                    }
                default:
                    throw new IllegalStateException();
            }
        }
        return new String(str);
    }
}
