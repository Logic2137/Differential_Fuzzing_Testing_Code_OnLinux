import java.awt.Font;

public class TextRequiresLayoutTest {

    public static void main(String[] args) {
        String simpleStr = "Hello World";
        String complexStr = "\u0641\u0642\u0643";
        char[] simpleChars = simpleStr.toCharArray();
        char[] complexChars = complexStr.toCharArray();
        if (Font.textRequiresLayout(simpleChars, 0, simpleChars.length)) {
            throw new RuntimeException("Simple text should not need layout");
        }
        if (!Font.textRequiresLayout(complexChars, 0, complexChars.length)) {
            throw new RuntimeException("Complex text should need layout");
        }
        if (Font.textRequiresLayout(complexChars, 0, 0)) {
            throw new RuntimeException("Empty text should not need layout");
        }
        boolean except = false;
        try {
            Font.textRequiresLayout(null, 0, 0);
        } catch (NullPointerException npe) {
            except = true;
        }
        if (!except) {
            throw new RuntimeException("No expected IllegalArgumentException");
        }
        except = false;
        try {
            Font.textRequiresLayout(complexChars, -1, 0);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            except = true;
        }
        if (!except) {
            throw new RuntimeException("No expected ArrayIndexOutOfBoundsException");
        }
        except = false;
        try {
            Font.textRequiresLayout(complexChars, 0, complexChars.length + 1);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            except = true;
        }
        if (!except) {
            throw new RuntimeException("No expected ArrayIndexOutOfBoundsException");
        }
    }
}
