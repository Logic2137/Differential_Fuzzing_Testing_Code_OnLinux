



import java.text.*;
import java.util.*;

public class KoreanTest {

    
    
    
    

    
    
    
    

    
    static final String[][] compData1 = {
        
        {"\uACE0\uC591\uC774", "\u732B",
         "Hangul \"Cat\"(0xACE0 0xC591 0xC774) <---> Chinese Kanji \"Cat\"(0x732B)"},
        {"\u30FB", "\u2025",
         "Katakana middle dot(0x30FB) <---> Two dot leader(0x2025)"},

        {"\u00B1", "\u2260",
         "Plus-Minus Sign(0x00B1) <---> Not Equal To(0x2260)"},
        {"\u3011", "\u2260",
         "Right Black Lenticular Bracket(0x3011) <---> Not Equal To(0x2260)"},
        {"\u2260", "\u2103",
         "Not Equal To(0x2260) <---> Degree Celsius(0x2103)"},
        {"\u2260", "\u2606",
         "Not Equal To(0x2260) <---> White Star(0x2606)"},

        
        
        
    };

    
    static final String[][] compData2 = {
        
        {"\u798F", "\uFA1B",
         "CJK Unified Ideograph \"FUKU\"(0x798F) <---> CJK Compatibility Ideograph \"FUKU\"(0xFA1B)"},

    };

    Collator col = Collator.getInstance(Locale.KOREA);
    int result = 0;

    public static void main(String[] args) throws Exception {
        new KoreanTest().run();
    }

    public void run() {
        
        
        
        doCompare(compData1);
        doEquals(compData2);

        
        
        
        col.setStrength(Collator.SECONDARY);
        doCompare(compData1);
        doEquals(compData2);

        
        
        
        col.setStrength(Collator.PRIMARY);
        doCompare(compData1);
        doEquals(compData2);

        if (result !=0) {
            throw new RuntimeException("Unexpected results on Korean collation.");
        }
    }

    
    void doCompare(String[][] s) {
        int value;
        for (int i=0; i < s.length; i++) {
            if ((value = col.compare(s[i][0], s[i][1])) > -1) {
                result++;
                System.err.println("TERTIARY: The first string should be less than the second string:  " +
                    s[i][2] + "  compare() returned " + value + ".");
            }
        }
    }

    
    void doEquals(String[][] s) {
        for (int i=0; i < s.length; i++) {
            if (!col.equals(s[i][0], s[i][1])) {
                result++;
                System.err.println("TERTIARY: The first string should be equals to the second string:  " +
                    s[i][2] + "  compare() returned " +
                    col.compare(s[i][0], s[i][1] + "."));
            }
        }
    }
}
