



public class TestIsJavaIdentifierMethods {

    
    private static final int JAPANESE_ERA_CODEPOINT = 0x32FF;

    public static void main(String[] args) {
        testIsJavaIdentifierPart_int();
        testIsJavaIdentifierPart_char();
        testIsJavaIdentifierStart_int();
        testIsJavaIdentifierStart_char();
        testIsJavaLetter();
        testIsJavaLetterOrDigit();
    }

    
    public static void testIsJavaIdentifierPart_int() {
        for (int cp = 0; cp <= Character.MAX_CODE_POINT; cp++) {
            boolean expected = false;
            
            
            
            
            
            if (cp != JAPANESE_ERA_CODEPOINT) {
                byte type = (byte) Character.getType(cp);
                expected = Character.isLetter(cp)
                        || type == Character.CURRENCY_SYMBOL
                        || type == Character.CONNECTOR_PUNCTUATION
                        || Character.isDigit(cp)
                        || type == Character.LETTER_NUMBER
                        || type == Character.COMBINING_SPACING_MARK
                        || type == Character.NON_SPACING_MARK
                        || Character.isIdentifierIgnorable(cp);
            }

            if (Character.isJavaIdentifierPart(cp) != expected) {
                throw new RuntimeException(
                   "Character.isJavaIdentifierPart(int) failed for codepoint "
                                + Integer.toHexString(cp));
            }
        }
    }

    
    public static void testIsJavaIdentifierPart_char() {
        for (int i = 0; i <= Character.MAX_VALUE; ++i) {
            char ch = (char) i;
            boolean expected = false;
            
            
            
            
            
            if (i != JAPANESE_ERA_CODEPOINT) {
                byte type = (byte) Character.getType(ch);
                expected = Character.isLetter(ch)
                        || type == Character.CURRENCY_SYMBOL
                        || type == Character.CONNECTOR_PUNCTUATION
                        || Character.isDigit(ch)
                        || type == Character.LETTER_NUMBER
                        || type == Character.COMBINING_SPACING_MARK
                        || type == Character.NON_SPACING_MARK
                        || Character.isIdentifierIgnorable(ch);
            }

            if (Character.isJavaIdentifierPart((char) i) != expected) {
                throw new RuntimeException(
                    "Character.isJavaIdentifierPart(char) failed for codepoint "
                                + Integer.toHexString(i));
            }
        }
    }

    
    public static void testIsJavaIdentifierStart_int() {
        for (int cp = 0; cp <= Character.MAX_CODE_POINT; cp++) {
            boolean expected = false;
            
            
            
            
            
            if (cp != JAPANESE_ERA_CODEPOINT) {
                byte type = (byte) Character.getType(cp);
                expected = Character.isLetter(cp)
                        || type == Character.LETTER_NUMBER
                        || type == Character.CURRENCY_SYMBOL
                        || type == Character.CONNECTOR_PUNCTUATION;
            }

            if (Character.isJavaIdentifierStart(cp) != expected) {
                throw new RuntimeException(
                        "Character.isJavaIdentifierStart(int) failed for codepoint "
                                + Integer.toHexString(cp));
            }
        }
    }

    
    public static void testIsJavaIdentifierStart_char() {
        for (int i = 0; i <= Character.MAX_VALUE; i++) {
            char ch = (char) i;
            boolean expected = false;
            
            
            
            
            
            if (i != JAPANESE_ERA_CODEPOINT) {
                byte type = (byte) Character.getType(ch);
                expected = Character.isLetter(ch)
                        || type == Character.LETTER_NUMBER
                        || type == Character.CURRENCY_SYMBOL
                        || type == Character.CONNECTOR_PUNCTUATION;
            }

            if (Character.isJavaIdentifierStart(ch) != expected) {
                throw new RuntimeException(
                        "Character.isJavaIdentifierStart(char) failed for codepoint "
                                + Integer.toHexString(i));
            }
        }
    }

    
    public static void testIsJavaLetter() {
        for (int i = 0; i <= Character.MAX_VALUE; ++i) {
            char ch = (char) i;
            boolean expected = false;
            
            
            
            
            
            if (i != JAPANESE_ERA_CODEPOINT) {
                byte type = (byte) Character.getType(ch);
                expected = Character.isLetter(ch)
                        || type == Character.LETTER_NUMBER
                        || type == Character.CURRENCY_SYMBOL
                        || type == Character.CONNECTOR_PUNCTUATION;
            }

            if (Character.isJavaLetter(ch) != expected) {
                throw new RuntimeException(
                        "Character.isJavaLetter(ch) failed for codepoint "
                                + Integer.toHexString(i));
            }
        }
    }

    
    public static void testIsJavaLetterOrDigit() {
        for (int i = 0; i <= Character.MAX_VALUE; ++i) {
            char ch = (char) i;
            boolean expected = false;
            
            
            
            
            
            if (i != JAPANESE_ERA_CODEPOINT) {
                byte type = (byte) Character.getType(ch);
                expected = Character.isLetter(ch)
                        || type == Character.CURRENCY_SYMBOL
                        || type == Character.CONNECTOR_PUNCTUATION
                        || Character.isDigit(ch)
                        || type == Character.LETTER_NUMBER
                        || type == Character.COMBINING_SPACING_MARK
                        || type == Character.NON_SPACING_MARK
                        || Character.isIdentifierIgnorable(ch);
            }

            if (Character.isJavaLetterOrDigit(ch) != expected) {
                throw new RuntimeException(
                        "Character.isJavaLetterOrDigit(ch) failed for codepoint "
                                + Integer.toHexString(i));
            }
        }
    }
}
