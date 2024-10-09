



import java.text.Annotation;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class AttributedStringTest {

    private static final String text = "Hello, world!";
    private static final Annotation hi = new Annotation("hi");
    private static final int[] array5_13 = {5, 13};
    private static final int[] array3_9_13 = {3, 9, 13};
    private static final int[] array5_9_13 = {5, 9, 13};
    private static final int[] array3_5_9_13 = {3, 5, 9, 13};
    private static final Attribute[] arrayLanguage = {Attribute.LANGUAGE};
    private static final Attribute[] arrayLanguageReading = {Attribute.LANGUAGE, Attribute.READING};
    private static final Set setLanguageReading = new HashSet();
    static {
        setLanguageReading.add(Attribute.LANGUAGE);
        setLanguageReading.add(Attribute.READING);
    }


    public static final void main(String argv[]) throws Exception {


        AttributedString string;
        AttributedCharacterIterator iterator;

        
        string = new AttributedString(text);
        iterator = string.getIterator();

        
        checkIteratorText(iterator, text);
        if (!iterator.getAllAttributeKeys().isEmpty()) {
            throwException(iterator, "iterator provides attributes where none are defined");
        }

        
        string.addAttribute(Attribute.LANGUAGE, Locale.ENGLISH, 3, 9);
        iterator = string.getIterator();

        
        checkIteratorAttributeKeys(iterator, arrayLanguage);
        checkIteratorSubranges(iterator, array3_9_13);
        checkIteratorAttribute(iterator, 0, Attribute.LANGUAGE, null);
        checkIteratorAttribute(iterator, 3, Attribute.LANGUAGE, Locale.ENGLISH);
        checkIteratorAttribute(iterator, 9, Attribute.LANGUAGE, null);

        
        string.addAttribute(Attribute.READING, hi, 0, 5);
        iterator = string.getIterator();

        
        checkIteratorAttributeKeys(iterator, arrayLanguageReading);
        checkIteratorSubranges(iterator, array3_5_9_13);
        checkIteratorAttribute(iterator, 0, Attribute.READING, hi);
        checkIteratorAttribute(iterator, 3, Attribute.READING, hi);
        checkIteratorAttribute(iterator, 5, Attribute.READING, null);
        checkIteratorAttribute(iterator, 9, Attribute.READING, null);

        
        
        checkIteratorSubranges(iterator, Attribute.LANGUAGE, array3_9_13);
        checkIteratorAttribute(iterator, 0, Attribute.LANGUAGE, null);
        checkIteratorAttribute(iterator, 3, Attribute.LANGUAGE, Locale.ENGLISH);
        checkIteratorAttribute(iterator, 5, Attribute.LANGUAGE, Locale.ENGLISH);
        checkIteratorAttribute(iterator, 9, Attribute.LANGUAGE, null);

        
        checkIteratorSubranges(iterator, setLanguageReading, array3_5_9_13);

        
        string.addAttribute(Attribute.LANGUAGE, Locale.US, 0, 5);
        iterator = string.getIterator();

        
        checkIteratorAttributeKeys(iterator, arrayLanguageReading);
        checkIteratorSubranges(iterator, array3_5_9_13);
        checkIteratorSubranges(iterator, Attribute.LANGUAGE, array5_9_13);
        checkIteratorSubranges(iterator, Attribute.READING, array5_13);
        checkIteratorSubranges(iterator, setLanguageReading, array5_9_13);
        checkIteratorAttribute(iterator, 0, Attribute.LANGUAGE, Locale.US);
        checkIteratorAttribute(iterator, 3, Attribute.LANGUAGE, Locale.US);
        checkIteratorAttribute(iterator, 5, Attribute.LANGUAGE, Locale.ENGLISH);
        checkIteratorAttribute(iterator, 9, Attribute.LANGUAGE, null);

        
        iterator = string.getIterator(null, 3, 5);
        checkIteratorAttribute(iterator, 3, Attribute.READING, null);
        checkIteratorAttribute(iterator, 5, Attribute.READING, null);
        iterator = string.getIterator(null, 0, 4);
        checkIteratorAttribute(iterator, 0, Attribute.READING, null);
        checkIteratorAttribute(iterator, 3, Attribute.READING, null);
        iterator = string.getIterator(null, 0, 5);
        checkIteratorAttribute(iterator, 0, Attribute.READING, hi);
        checkIteratorAttribute(iterator, 4, Attribute.READING, hi);
        checkIteratorAttribute(iterator, 5, Attribute.READING, null);

    }

    private static final void checkIteratorText(AttributedCharacterIterator iterator, String expectedText) throws Exception {
        if (iterator.getEndIndex() - iterator.getBeginIndex() != expectedText.length()) {
            throwException(iterator, "text length doesn't match between original text and iterator");
        }

        char c = iterator.first();
        for (int i = 0; i < expectedText.length(); i++) {
            if (c != expectedText.charAt(i)) {
                throwException(iterator, "text content doesn't match between original text and iterator");
            }
            c = iterator.next();
        }
        if (c != CharacterIterator.DONE) {
            throwException(iterator, "iterator text doesn't end with DONE");
        }
    }

    private static final void checkIteratorAttributeKeys(AttributedCharacterIterator iterator, Attribute[] expectedKeys) throws Exception {
         Set iteratorKeys = iterator.getAllAttributeKeys();
         if (iteratorKeys.size() != expectedKeys.length) {
             throwException(iterator, "number of keys returned by iterator doesn't match expectation");
         }
         for (int i = 0; i < expectedKeys.length; i++) {
             if (!iteratorKeys.contains(expectedKeys[i])) {
                 throwException(iterator, "expected key wasn't found in iterator's key set");
             }
         }
    }

    private static final void checkIteratorSubranges(AttributedCharacterIterator iterator, int[] expectedLimits) throws Exception {
        int previous = 0;
        char c = iterator.first();
        for (int i = 0; i < expectedLimits.length; i++) {
             if (iterator.getRunStart() != previous || iterator.getRunLimit() != expectedLimits[i]) {
                 throwException(iterator, "run boundaries are not as expected: " + iterator.getRunStart() + ", " + iterator.getRunLimit());
             }
             previous = expectedLimits[i];
             c = iterator.setIndex(previous);
        }
        if (c != CharacterIterator.DONE) {
            throwException(iterator, "iterator's run sequence doesn't end with DONE");
        }
    }

    private static final void checkIteratorSubranges(AttributedCharacterIterator iterator, Attribute key, int[] expectedLimits) throws Exception {
        int previous = 0;
        char c = iterator.first();
        for (int i = 0; i < expectedLimits.length; i++) {
             if (iterator.getRunStart(key) != previous || iterator.getRunLimit(key) != expectedLimits[i]) {
                 throwException(iterator, "run boundaries are not as expected: " + iterator.getRunStart(key) + ", " + iterator.getRunLimit(key) + " for key " + key);
             }
             previous = expectedLimits[i];
             c = iterator.setIndex(previous);
        }
        if (c != CharacterIterator.DONE) {
            throwException(iterator, "iterator's run sequence doesn't end with DONE");
        }
    }

    private static final void checkIteratorSubranges(AttributedCharacterIterator iterator, Set keys, int[] expectedLimits) throws Exception {
        int previous = 0;
        char c = iterator.first();
        for (int i = 0; i < expectedLimits.length; i++) {
             if (iterator.getRunStart(keys) != previous || iterator.getRunLimit(keys) != expectedLimits[i]) {
                 throwException(iterator, "run boundaries are not as expected: " + iterator.getRunStart(keys) + ", " + iterator.getRunLimit(keys) + " for keys " + keys);
             }
             previous = expectedLimits[i];
             c = iterator.setIndex(previous);
        }
        if (c != CharacterIterator.DONE) {
            throwException(iterator, "iterator's run sequence doesn't end with DONE");
        }
    }

    private static final void checkIteratorAttribute(AttributedCharacterIterator iterator, int index, Attribute key, Object expectedValue) throws Exception {
        iterator.setIndex(index);
        Object value = iterator.getAttribute(key);
        if (!((expectedValue == null && value == null) || (expectedValue != null && expectedValue.equals(value)))) {
            throwException(iterator, "iterator returns wrong attribute value - " + value + " instead of " + expectedValue);
        }
        value = iterator.getAttributes().get(key);
        if (!((expectedValue == null && value == null) || (expectedValue != null && expectedValue.equals(value)))) {
            throwException(iterator, "iterator's map returns wrong attribute value - " + value + " instead of " + expectedValue);
        }
    }

    private static final void throwException(AttributedCharacterIterator iterator, String details) throws Exception {
        dumpIterator(iterator);
        throw new Exception(details);
    }

    private static final void dumpIterator(AttributedCharacterIterator iterator) {
        Set attributeKeys = iterator.getAllAttributeKeys();
        System.out.print("All attributes: ");
        Iterator keyIterator = attributeKeys.iterator();
        while (keyIterator.hasNext()) {
            Attribute key = (Attribute) keyIterator.next();
            System.out.print(key);
        }
        for(char c = iterator.first(); c != CharacterIterator.DONE; c = iterator.next()) {
            if (iterator.getIndex() == iterator.getBeginIndex() ||
                        iterator.getIndex() == iterator.getRunStart()) {
                System.out.println();
                Map attributes = iterator.getAttributes();
                Set entries = attributes.entrySet();
                Iterator attributeIterator = entries.iterator();
                while (attributeIterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) attributeIterator.next();
                    System.out.print("<" + entry.getKey() + ": "
                                + entry.getValue() + ">");
                }
            }
            System.out.print(" ");
            System.out.print(c);
        }
        System.out.println();
        System.out.println("done");
        System.out.println();
    }

}
