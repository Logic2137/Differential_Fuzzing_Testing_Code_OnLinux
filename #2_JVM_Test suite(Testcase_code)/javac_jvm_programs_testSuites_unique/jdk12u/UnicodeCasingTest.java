



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UnicodeCasingTest {

    private static boolean err = false;

    
    private static List<Locale> locales = new ArrayList<>();
    static {
        locales.add(new Locale("az", ""));
        locales.addAll(java.util.Arrays.asList(Locale.getAvailableLocales()));
    }


    public static void main(String[] args) {
        UnicodeCasingTest specialCasingTest = new UnicodeCasingTest();
        specialCasingTest.test();
    }

    private void test() {
        Locale defaultLocale = Locale.getDefault();

        BufferedReader in = null;

        try {
            File file = new File(System.getProperty("test.src", "."),
                                 "UnicodeData.txt");

            int locale_num = locales.size();
            for (int l = 0; l < locale_num; l++) {
                Locale locale = locales.get(l);
                Locale.setDefault(locale);
                System.out.println("Testing on " + locale + " locale....");

                in = new BufferedReader(new FileReader(file));

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.length() == 0 || line.charAt(0) == '#') {
                        continue;
                    }

                    test(line);
                }

                in.close();
                in = null;
            }
        }
        catch (Exception e) {
            err = true;
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {
                }
            }

            Locale.setDefault(defaultLocale);

            if (err) {
                throw new RuntimeException("UnicodeCasingTest failed.");
            } else {
                System.out.println("UnicodeCasingTest passed.");
            }
        }
    }

    private void test(String line) {
        String[] fields = line.split(";", 15);
        int orig = convert(fields[0]);

        if (fields[12].length() != 0) {
            testUpperCase(orig, convert(fields[12]));
        } else {
            testUpperCase(orig, orig);
        }

        if (fields[13].length() != 0) {
            testLowerCase(orig, convert(fields[13]));
        } else {
            testLowerCase(orig, orig);
        }

        if (fields[14].length() != 0) {
            testTitleCase(orig, convert(fields[14]));
        } else {
            testTitleCase(orig, orig);
        }
    }

    private void testUpperCase(int orig, int expected) {
        int got = Character.toUpperCase(orig);

        if (expected != got) {
            err = true;
            System.err.println("toUpperCase(" +
                ") failed.\n\tOriginal: " + toString(orig) +
                "\n\tGot:      " + toString(got) +
                "\n\tExpected: " + toString(expected));
        }
    }

    private void testLowerCase(int orig, int expected) {
        int got = Character.toLowerCase(orig);

        if (expected != got) {
            err = true;
            System.err.println("toLowerCase(" +
                ") failed.\n\tOriginal: " + toString(orig) +
                "\n\tGot:      " + toString(got) +
                "\n\tExpected: " + toString(expected));
        }
    }

    private void testTitleCase(int orig, int expected) {
        int got = Character.toTitleCase(orig);

        if (expected != got) {
            err = true;
            System.err.println("toTitleCase(" +
                ") failed.\n\tOriginal: " + toString(orig) +
                "\n\tGot:      " + toString(got) +
                "\n\tExpected: " + toString(expected));
        }
    }

    private int convert(String str) {
        return Integer.parseInt(str, 16);
    }

    private String toString(int i) {
        return Integer.toHexString(i).toUpperCase();
    }

}
