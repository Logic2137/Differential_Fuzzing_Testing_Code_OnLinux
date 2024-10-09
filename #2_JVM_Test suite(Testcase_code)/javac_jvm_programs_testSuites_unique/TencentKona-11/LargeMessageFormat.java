



import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LargeMessageFormat {

    public static void main(String[] args) throws ParseException {
        Locale reservedLocale = Locale.getDefault();
        TimeZone reservedTimeZone = TimeZone.getDefault();
        try {
            Locale.setDefault(Locale.GERMANY);
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
            testFormat();
            testParse();
        } finally {
            
            Locale.setDefault(reservedLocale);
            TimeZone.setDefault(reservedTimeZone);
        }
    }

    private static final int REPEATS = 89;

    private static void testFormat() {
        
        @SuppressWarnings("deprecation")
        Object[] sample = {
                 0, 
                "hello",
                new Date(89, 10, 9),
                567890,
                1234.50
        };
        int samples = sample.length;
        Object[] arguments = new Object[REPEATS * (samples + 1)];
        for (int i = 0; i < REPEATS; i++) {
            System.arraycopy(sample, 0, arguments, i * samples, samples);
            arguments[i * samples] = i;
        }

        
        StringBuffer template = new StringBuffer();
        for (int i = 0; i < REPEATS; i++) {
            template.append("section {" + (i * samples) + ", number} - ");
            template.append("string: {" + (i * samples + 1) + "}; ");
            template.append("date: {" + (i * samples + 2) + ", date}; ");
            template.append("integer: {" + (i * samples + 3) + ", number}; ");
            template.append("currency: {" + (i * samples + 4) + ", number, currency};\n");
        }

        
        StringBuffer expected = new StringBuffer();
        for (int i = 0; i < REPEATS; i++) {
            expected.append("section " + i + " - ");
            expected.append("string: hello; ");
            expected.append("date: 09.11.1989; ");
            expected.append("integer: 567.890; ");
            expected.append("currency: 1.234,50 \u20AC;\n");
        }

        
        MessageFormat format = new MessageFormat(template.toString());
        String result = format.format(arguments);
        if (!result.equals(expected.toString())) {
           System.out.println("Template:");
           System.out.println(template);
           System.out.println("Expected result: ");
           System.out.println(expected);
           System.out.println("Actual result: ");
           System.out.println(result);
           throw new RuntimeException();
       }
    }

    private static void testParse() throws ParseException {
        StringBuffer parseTemplate = new StringBuffer();
        StringBuffer parseInput = new StringBuffer();
        for (int i = 0; i < REPEATS; i++) {
            parseTemplate.append("{" + i + ", number} ");
            parseInput.append(i + " ");
        }
        MessageFormat parseFormat = new MessageFormat(parseTemplate.toString());
        Object[] parseResult = parseFormat.parse(parseInput.toString());
        for (int i = 0; i < REPEATS; i++) {
            if (((Number) parseResult[i]).intValue() != i) {
                throw new RuntimeException("got wrong parse result");
            }
        }
    }
}
