public class TestNegativeCodepoint {

    public static void main(String[] args) {
        int[] invalidCodePoints = { -1, -'a', 0x110000 };
        for (int x = 0; x < invalidCodePoints.length; ++x) {
            int cp = invalidCodePoints[x];
            System.out.println("Testing codepoint: " + cp);
            if (Character.isLowerCase(cp) || Character.isUpperCase(cp) || Character.isTitleCase(cp) || Character.isISOControl(cp) || Character.isLetterOrDigit(cp) || Character.isLetter(cp) || Character.isDigit(cp) || Character.isDefined(cp) || Character.isJavaIdentifierStart(cp) || Character.isJavaIdentifierPart(cp) || Character.isUnicodeIdentifierStart(cp) || Character.isUnicodeIdentifierPart(cp) || Character.isIdentifierIgnorable(cp) || Character.isSpaceChar(cp) || Character.isWhitespace(cp) || Character.isMirrored(cp) || Character.toLowerCase(cp) != cp || Character.toUpperCase(cp) != cp || Character.toTitleCase(cp) != cp || Character.getDirectionality(cp) != Character.DIRECTIONALITY_UNDEFINED || Character.getType(cp) != Character.UNASSIGNED || Character.getNumericValue(cp) != -1 || Character.digit(cp, 10) != -1) {
                System.out.println("Failed.");
                throw new RuntimeException();
            }
            Character.UnicodeBlock block = null;
            try {
                block = Character.UnicodeBlock.of(cp);
                System.out.println("Failed.");
                throw new RuntimeException();
            } catch (IllegalArgumentException e) {
            }
        }
        System.out.println("Passed.");
    }
}
