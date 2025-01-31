import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;

public class MessageFormatsByArgumentIndex {

    private static String choicePattern = "0.0#are no files|1.0#is one file|1.0<are {0,number,integer} files";

    public static void main(String[] args) {
        Format[] subformats;
        MessageFormat format = new MessageFormat("{3, choice," + choicePattern + "}, {2}, {0}");
        subformats = format.getFormatsByArgumentIndex();
        checkSubformatLength(subformats, 4);
        checkSubformat(subformats, 0, null);
        checkSubformat(subformats, 1, null);
        checkSubformat(subformats, 2, null);
        checkSubformat(subformats, 3, new ChoiceFormat(choicePattern));
        subformats = format.getFormats();
        checkSubformatLength(subformats, 3);
        checkSubformat(subformats, 0, new ChoiceFormat(choicePattern));
        checkSubformat(subformats, 1, null);
        checkSubformat(subformats, 2, null);
        format.setFormatByArgumentIndex(0, NumberFormat.getInstance());
        checkPattern(format.toPattern(), "{3,choice," + choicePattern + "}, {2}, {0,number}");
        subformats = format.getFormatsByArgumentIndex();
        checkSubformatLength(subformats, 4);
        checkSubformat(subformats, 0, NumberFormat.getInstance());
        checkSubformat(subformats, 1, null);
        checkSubformat(subformats, 2, null);
        checkSubformat(subformats, 3, new ChoiceFormat(choicePattern));
        subformats = format.getFormats();
        checkSubformatLength(subformats, 3);
        checkSubformat(subformats, 0, new ChoiceFormat(choicePattern));
        checkSubformat(subformats, 1, null);
        checkSubformat(subformats, 2, NumberFormat.getInstance());
        format.setFormatsByArgumentIndex(subformats);
        checkPattern(format.toPattern(), "{3,choice," + choicePattern + "}, {2,number}, {0,choice," + choicePattern + "}");
        subformats = format.getFormatsByArgumentIndex();
        checkSubformatLength(subformats, 4);
        checkSubformat(subformats, 0, new ChoiceFormat(choicePattern));
        checkSubformat(subformats, 1, null);
        checkSubformat(subformats, 2, NumberFormat.getInstance());
        checkSubformat(subformats, 3, new ChoiceFormat(choicePattern));
        subformats = format.getFormats();
        checkSubformatLength(subformats, 3);
        checkSubformat(subformats, 0, new ChoiceFormat(choicePattern));
        checkSubformat(subformats, 1, NumberFormat.getInstance());
        checkSubformat(subformats, 2, new ChoiceFormat(choicePattern));
    }

    private static void checkPattern(String actual, String expected) {
        if (!expected.equals(actual)) {
            throw new RuntimeException("unexpected pattern:\n expected: " + expected + "\n   actual: " + actual);
        }
    }

    private static void checkSubformatLength(Format[] subformats, int expected) {
        if (subformats.length != expected) {
            throw new RuntimeException("unexpected subformat length:\n expected: " + expected + "\n   actual: " + subformats.length);
        }
    }

    private static void checkSubformat(Format[] subformats, int index, Format expected) {
        Format subformat = subformats[index];
        if (subformat == expected) {
            return;
        }
        if ((subformat != null) && subformat.equals(expected)) {
            return;
        }
        throw new RuntimeException("found unexpected subformat for argument " + index + ":\n expected: " + expected + "\n   actual: " + subformat);
    }
}
