



import java.util.Locale;

public class CharacterName {

    public static void main(String[] args) {
        for (int cp = 0; cp < Character.MAX_CODE_POINT; cp++) {
            if (!Character.isValidCodePoint(cp)) {
                try {
                    Character.getName(cp);
                } catch (IllegalArgumentException x) {
                    continue;
                }
                throw new RuntimeException("Invalid failed: " + cp);
            } else if (Character.getType(cp) == Character.UNASSIGNED) {
                if (Character.getName(cp) != null)
                    throw new RuntimeException("Unsigned failed: " + cp);
            } else {
                String name = Character.getName(cp);
                if (cp != Character.codePointOf(name) ||
                    cp != Character.codePointOf(name.toLowerCase(Locale.ENGLISH)))
                throw new RuntimeException("Roundtrip failed: " + cp);
            }
        }
    }
}
