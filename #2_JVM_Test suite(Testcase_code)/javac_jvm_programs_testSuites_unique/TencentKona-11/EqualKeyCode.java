




import java.awt.*;
import java.awt.event.KeyEvent;

public class EqualKeyCode {

    final static String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String []s) {
        for (int i = 0; i < LETTERS.length(); i++){
            char cSmall = LETTERS.charAt(i);
            char cLarge = Character.toUpperCase(cSmall);

            int iSmall = KeyEvent.getExtendedKeyCodeForChar(cSmall);
            int iLarge = KeyEvent.getExtendedKeyCodeForChar(cLarge);

            System.out.print(" " + cSmall + ":" + iSmall + " ---- ");
            System.out.println(" " + cLarge + " : " + iLarge);
            if (KeyEvent.getExtendedKeyCodeForChar(cSmall) !=
                KeyEvent.getExtendedKeyCodeForChar(cLarge))
            {
                throw new RuntimeException("ExtendedKeyCode doesn't exist or doesn't match between capital and small letters.");
            }
        }
    }
}
