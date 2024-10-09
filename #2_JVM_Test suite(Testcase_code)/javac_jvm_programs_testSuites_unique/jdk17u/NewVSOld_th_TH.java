import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Locale;
import java.text.BreakIterator;
import java.lang.Math;

public class NewVSOld_th_TH {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        final String ENCODING = "UTF-8";
        final Locale THAI_LOCALE = new Locale("th", "TH");
        String rawFileName = "test_th_TH.txt";
        String oldFileName = "broken_th_TH.txt";
        StringBuilder rawText = new StringBuilder();
        StringBuilder oldText = new StringBuilder();
        StringBuilder cookedText = new StringBuilder();
        File f;
        f = new File(System.getProperty("test.src", "."), rawFileName);
        try (InputStreamReader rawReader = new InputStreamReader(new FileInputStream(f), ENCODING)) {
            int c;
            while ((c = rawReader.read()) != -1) {
                rawText.append((char) c);
            }
        }
        f = new File(System.getProperty("test.src", "."), oldFileName);
        try (InputStreamReader oldReader = new InputStreamReader(new FileInputStream(f), ENCODING)) {
            int c;
            while ((c = oldReader.read()) != -1) {
                oldText.append((char) c);
            }
        }
        BreakIterator breakIterator = BreakIterator.getWordInstance(THAI_LOCALE);
        breakIterator.setText(rawText.toString());
        int start = breakIterator.first();
        for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
            cookedText.append(rawText.substring(start, end));
            cookedText.append("\n");
        }
        String cooked = cookedText.toString();
        String old = oldText.toString();
        if (cooked.compareTo(old) != 0) {
            throw new RuntimeException("Text not broken the same as with the old BreakIterators");
        }
    }
}
