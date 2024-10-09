



import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class SpecialCasingTest {

    private static boolean err = false;

    
    private static List<Locale> locales = new ArrayList<>();
    static {
        locales.add(new Locale("az", ""));
        locales.addAll(java.util.Arrays.asList(Locale.getAvailableLocales()));
    }

    
    private static String defaultLang;

    
    
    private static boolean specificLocale;

    
    
    
    
    
    private static final String[] additionalTestData = {
        
        

        
        
            "03A3; 03C3; 03A3; 03A3; SunSpecific_Not_Final_Sigma1",
            "03A3; 03C3; 03A3; 03A3; SunSpecific_Not_Final_Sigma2",

        
        
            "0307; 0307; 0307; 0307; L1 After_Soft_Dotted",
            "0307; 0307; 0307; 0307; lt SunSpecific_Not_After_Soft_Dotted",
            "0307; 0307; 0307; 0307; L1 SunSpecific_Not_After_Soft_Dotted",

        
        
            "0049; 0131     ; 0049; 0049; az More_Above",
            "0049; 0131     ; 0049; 0049; tr More_Above",
            "0049; 0069     ; 0049; 0049; L3 More_Above",
            "0049; 0069     ; 0049; 0049; lt SunSpecific_Not_More_Above",
            "0049; 0131     ; 0049; 0049; az SunSpecific_Not_More_Above",
            "0049; 0131     ; 0049; 0049; tr SunSpecific_Not_More_Above",
            "0049; 0069     ; 0049; 0049; L3 SunSpecific_Not_More_Above",
        
            "004A; 006A     ; 004A; 004A; L1 More_Above",
            "004A; 006A     ; 004A; 004A; lt SunSpecific_Not_More_Above",
            "004A; 006A     ; 004A; 004A; L1 SunSpecific_Not_More_Above",
        
            "012E; 012F     ; 012E; 012E; L1 More_Above",
            "012E; 012F     ; 012E; 012E; lt SunSpecific_Not_More_Above",
            "012E; 012F     ; 012E; 012E; L1 SunSpecific_Not_More_Above",

        
        
        
            "0307; 0307 0307; 0307; 0307; lt After_I",
            "0307; 0307     ; 0307; 0307; L3 After_I",
            "0307; 0307     ; 0307; 0307; tr SunSpecific_Not_After_I",
            "0307; 0307     ; 0307; 0307; az SunSpecific_Not_After_I",
            "0307; 0307     ; 0307; 0307; L2 SunSpecific_Not_After_I",

        
        
        
            "0049; 0069          ; 0049; 0049; L2 Not_Before_Dot",
            "0049; 0069          ; 0049; 0049; tr SunSpecific_Before_Dot",
            "0049; 0069          ; 0049; 0049; az SunSpecific_Before_Dot",
            "0049; 0069 0307 0307; 0049; 0049; lt SunSpecific_Before_Dot",
            "0049; 0069 0307     ; 0049; 0049; L3 SunSpecific_Before_Dot",
    };

    public static void main (String[] args) {
        SpecialCasingTest specialCasingTest = new SpecialCasingTest();
        specialCasingTest.test();
    }

    private void test ()  {
        Locale defaultLocale = Locale.getDefault();
        BufferedReader in = null;

        try {
            int locale_num = locales.size();
            for (int l = 0; l < locale_num; l++) {
                Locale locale = locales.get(l);
                Locale.setDefault(locale);
                System.out.println("Testing on " + locale + " locale....");

                defaultLang = locale.getLanguage();
                if (defaultLang.equals("az") ||
                    defaultLang.equals("lt") ||
                    defaultLang.equals("tr")) {
                    specificLocale = true;
                } else {
                    specificLocale = false;
                }
                in = Files.newBufferedReader(Paths.get(System.getProperty("test.src.path"), "..", "/Character/SpecialCasing.txt")
                     .toRealPath());
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.length() == 0 || line.charAt(0) == '#') {
                        continue;
                    }
                    test(line);
                }
                in.close();
                in = null;
                System.out.println("Testing with Sun original data....");
                for (String additionalTestData1 : additionalTestData) {
                    test(additionalTestData1);
                }
            }
        }
        catch (IOException e) {
            err = true;
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                }
            }
            Locale.setDefault(defaultLocale);
            if (err) {
                throw new RuntimeException("SpecialCasingTest failed.");
            } else {
                System.out.println("*** SpecialCasingTest passed.");
            }
        }
    }

    private void test(String line) {
        int index = line.indexOf('#');
        if (index != -1) {
            line = line.substring(0, index);
        }

        String lang = null;
        String condition = null;
        String[] fields = line.split("; ");

        for (int i = 0; i < 4; i++) {
            if (fields[i].length() != 0) {
                fields[i] = convert(fields[i]);
            }
        }
        if (fields.length != 4) {
            StringTokenizer st = new StringTokenizer(fields[4]);

            while (st.hasMoreTokens()) {
                String token = st.nextToken();

                if (token.equals("Final_Sigma")) {
                    condition = "Final Sigma";
                    fields[0] = "Abc" + fields[0];
                    fields[1] = "abc" + fields[1];
                    fields[3] = "ABC" + fields[3];
                } else if (token.equals("SunSpecific_Not_Final_Sigma1")) {
                    condition = "*Sun Specific* Not Final Sigma 1";
                    fields[0] = "Abc" + fields[0] + "xyz";
                    fields[1] = "abc" + fields[1] + "xyz";
                    fields[3] = "ABC" + fields[3] + "XYZ";
                } else if (token.equals("SunSpecific_Not_Final_Sigma2")) {
                    condition = "*Sun Specific* Not Final Sigma 2";
                } else if (token.equals("After_Soft_Dotted")) {
                    condition = "After Soft-Dotted";
                    fields[0] = "\u1E2D" + fields[0];
                    fields[1] = "\u1E2D" + fields[1];
                    fields[3] = "\u1E2C" + fields[3];
                } else if (token.equals("SunSpecific_Not_After_Soft_Dotted")) {
                    condition = "*Sun Specific* Not After Soft-Dotted";
                    fields[0] = "Dot" + fields[0];
                    fields[1] = "dot" + fields[1];
                    fields[3] = "DOT" + fields[3];
                } else if (token.equals("More_Above")) {
                    condition = "More Above";
                    fields[0] = fields[0] + "\u0306";
                    fields[1] = fields[1] + "\u0306";
                    fields[3] = fields[3] + "\u0306";
                } else if (token.equals("SunSpecific_Not_More_Above")) {
                    condition = "*Sun Specific* Not More Above";
                    fields[0] = fields[0] + "breve";
                    fields[1] = fields[1] + "breve";
                    fields[3] = fields[3] + "BREVE";
                } else if (token.equals("After_I")) {
                    condition = "After I";
                    fields[0] = "I" + fields[0];
                    fields[1] = "i" + fields[1];
                    fields[3] = "I" + fields[3];
                } else if (token.equals("SunSpecific_Not_After_I")) {
                    condition = "*Sun Specific* Not After I";
                    fields[0] = "A" + fields[0];
                    fields[1] = "a" + fields[1];
                    fields[3] = "A" + fields[3];
                } else if (token.equals("Not_Before_Dot")) {
                    condition = "Not Before Dot";
                    fields[0] = fields[0] + "Z";
                    fields[1] = fields[1] + "z";
                    fields[3] = fields[3] + "Z";
                } else if (token.equals("SunSpecific_Before_Dot")) {
                    condition = "*Sun Specific* Before Dot";
                    fields[0] = fields[0] + "\u0307";
                    fields[3] = fields[3] + "\u0307";
                } else if (token.length() == 2) {
                    lang = token;

                    if (lang.equals("L1")) {
                        if (defaultLang.equals("lt")) {
                            lang = "en";
                        } else {
                            lang = defaultLang;
                        }
                    } else if (lang.equals("L2")) {
                        if (defaultLang.equals("az") ||
                            defaultLang.equals("tr")) {
                            lang = "en";
                        } else {
                            lang = defaultLang;
                        }
                    } else if (lang.equals("L3")) {
                        if (defaultLang.equals("az") ||
                            defaultLang.equals("lt") ||
                            defaultLang.equals("tr")) {
                            lang = "en";
                        } else {
                            lang = defaultLang;
                        }
                    
                    
                    
                    
                    } else if (!lang.equals("az") &&
                               !lang.equals("lt") &&
                               !lang.equals("tr")) {
                        throw new RuntimeException("Unsupported locale: " +
                            lang + ". It may need to be considered in ConditionalSpecialCasing.java. Please confirm.");
                    }
                } else {
                    throw new RuntimeException("Unknown condition: " + token);
                }
            }
        } else if (fields[0].equals("\u0130")) {
            
            if (defaultLang.equals("az") ||
                defaultLang.equals("tr")) {
                lang = "en";
            } else {
                lang = defaultLang;
            }
        }
        testLowerCase(fields[0], fields[1], lang, condition);
        testUpperCase(fields[0], fields[3], lang, condition);
    }

    private void testLowerCase(String orig, String expected,
                               String lang, String condition) {
        String got = (lang == null) ?
            orig.toLowerCase() : orig.toLowerCase(new Locale(lang, ""));

        if (!expected.equals(got)) {
            err = true;
            System.err.println("toLowerCase(lang=" + lang +
                ") failed.\n\tOriginal: " + toString(orig) +
                "\n\tGot:      " + toString(got) +
                "\n\tExpected: " + toString(expected) +
                ((condition == null) ? "" : ("\n    under condition(" +
                condition + ")")));
        }
    }

    private void testUpperCase(String orig, String expected,
                               String lang, String condition) {
        String got = (lang == null) ?
            orig.toUpperCase() : orig.toUpperCase(new Locale(lang, ""));

        if (!expected.equals(got)) {
            err = true;
            System.err.println("toUpperCase(lang=" + lang +
                ") failed.\n\tOriginal: " + toString(orig) +
                "\n\tGot:      " + toString(got) +
                "\n\tExpected: " + toString(expected) +
                ((condition == null) ? "" : ("\n    under condition(" +
                condition + ")")));
        }
    }
    StringBuilder sb = new StringBuilder();

    private String convert(String str) {
        sb.setLength(0);

        String[] tokens = str.split(" ");
        for (String token : tokens) {
            sb.append((char) Integer.parseInt(token, 16));
        }
        return sb.toString();
    }

    private String toString(String str) {
        sb.setLength(0);

        int len = str.length();
        for (int i = 0; i < len; i++) {
            sb.append("0x").append(Integer.toHexString(str.charAt(i)).toUpperCase()).append(" ");
        }
        return sb.toString();
    }

}
