import java.text.Bidi;

public class Bug8005277 {

    public static void main(String[] args) {
        boolean err = false;
        String string = "\u05D0\u05D1\u05D2";
        Bidi bidi = new Bidi(string, Bidi.DIRECTION_LEFT_TO_RIGHT);
        int result = bidi.getRunCount();
        if (result != 1) {
            System.err.println("Incorrect run count: " + result);
            err = true;
        }
        result = bidi.getRunStart(0);
        if (result != 0) {
            System.err.println("Incorrect run start: " + result);
            err = true;
        }
        result = bidi.getRunLimit(0);
        if (result != 3) {
            System.err.println("Incorrect run limit: " + result);
            err = true;
        }
        result = bidi.getRunLevel(0);
        if (result != 1) {
            System.err.println("Incorrect run level: " + result);
            err = true;
        }
        if (err) {
            throw new RuntimeException("Failed.");
        }
    }
}
