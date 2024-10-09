



import java.text.BreakIterator;

public class Bug4912404 {

    public static void main(String[] args) {
        BreakIterator b = BreakIterator.getWordInstance();
        b.setText("abc");
        if (b.equals(null)) {
            throw new RuntimeException("BreakIterator.equals(null) should return false.");
        }
    }
}
