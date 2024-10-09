



import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

public class TestAttributedStringCtor {

    public static void main(String[] args) {

        
        Hashtable attributes = new Hashtable();
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        AttributedString origString = new AttributedString("Hello world.", attributes);

        
        AttributedCharacterIterator iter = origString.getIterator(null, 4, 6);

        
        
        AttributedString newString = new AttributedString(iter);

        
        System.out.println("DONE");
    }
}
