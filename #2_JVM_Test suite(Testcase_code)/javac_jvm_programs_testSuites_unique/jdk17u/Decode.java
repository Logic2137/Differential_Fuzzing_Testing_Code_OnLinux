public class Decode {

    private static boolean isAscii(char c) {
        return c < '\u0080';
    }

    private static boolean isPrintable(char c) {
        return ('\u0020' < c) && (c < '\u007f');
    }

    public static void main(String[] args) throws Throwable {
        if (args.length < 2)
            throw new Exception("Usage: java Decode CHARSET BYTE [BYTE ...]");
        String cs = args[0];
        byte[] bytes = new byte[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            bytes[i - 1] = (arg.length() == 1 && isAscii(arg.charAt(0))) ? (byte) arg.charAt(0) : arg.equals("ESC") ? 0x1b : arg.equals("SO") ? 0x0e : arg.equals("SI") ? 0x0f : arg.equals("SS2") ? (byte) 0x8e : arg.equals("SS3") ? (byte) 0x8f : arg.matches("0x.*") ? Integer.decode(arg).byteValue() : Integer.decode("0x" + arg).byteValue();
        }
        String s = new String(bytes, cs);
        for (int j = 0; j < s.length(); j++) {
            if (j > 0)
                System.out.print(' ');
            char c = s.charAt(j);
            if (isPrintable(c))
                System.out.print(c);
            else if (c == '\u001b')
                System.out.print("ESC");
            else
                System.out.printf("\\u%04x", (int) c);
        }
        System.out.print("\n");
    }
}
