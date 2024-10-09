


import java.text.*;
import java.util.*;

public class Bug8032446 {

    public static void main(String[] args) {
        boolean err = false;

        StringBuilder sb = new StringBuilder();
        for (int i = 0x10860; i <= 0x10876; i++) { 
            sb.append(Character.toChars(i));
        }
        sb.append(" ");
        for (int i = 0x10879; i <= 0x1087D; i++) { 
            sb.append(Character.toChars(i));
        }
        String s = sb.toString();

        BreakIterator bi = BreakIterator.getWordInstance(Locale.ROOT);
        bi.setText(s);
        bi.first();

        if (bi.next() != s.indexOf(' ')) {
            throw new RuntimeException("Unexpected word breaking.");
        }
    }

}
