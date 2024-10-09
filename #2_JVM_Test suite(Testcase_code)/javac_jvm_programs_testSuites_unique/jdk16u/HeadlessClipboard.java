

import java.awt.datatransfer.Clipboard;



public class HeadlessClipboard {
    public static void main(String args[]) {
        Clipboard cb = new Clipboard("dummy");
        cb.getName();
    }
}
