



import java.net.*;

public class Decoder {

    public static void main(String args[]) throws Exception {

        boolean passed = true;
        String enc = "UTF-16";
        String strings[] = {
            "\u0100\u0101",
                "\u0100 \u0101",
                "\u0100 \u0101\u0102",
                "\u0100 \u0101 \u0102",
                "\u0100C\u0101 \u0102",
                "\u0100\u0101\u0102",
                "?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&?&",
                "foobar",
                "foo?bar"
        };

        for (int i = 0; i < strings.length; i++) {
            String encoded = URLEncoder.encode(strings[i], enc);
            System.out.println("ecnoded: " + encoded);
            String decoded = URLDecoder.decode(encoded, enc);
            System.out.print("init:    ");
            printString(strings[i]);
            System.out.print("decoded: ");
            printString(decoded);
            if (strings[i].equals(decoded)) {
                System.out.println(" - correct - \n");
            } else {
                System.out.println(" - incorrect - \n");
                throw new RuntimeException ("Unexpected decoded output on string " + i);
            }
        }
    }

    static void printString(String s) {
        for (int i = 0; i < s.length(); i++) {
            System.out.print((int)s.charAt(i) + " ");
        }
        System.out.println();
    }
}
