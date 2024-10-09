import java.awt.im.InputMethodHighlight;

public class HeadlessInputMethodHighlight {

    public static void main(String[] args) {
        InputMethodHighlight imh;
        imh = new InputMethodHighlight(true, InputMethodHighlight.CONVERTED_TEXT);
        imh = new InputMethodHighlight(false, InputMethodHighlight.CONVERTED_TEXT);
        imh = new InputMethodHighlight(true, InputMethodHighlight.RAW_TEXT);
        imh = new InputMethodHighlight(false, InputMethodHighlight.RAW_TEXT);
    }
}
