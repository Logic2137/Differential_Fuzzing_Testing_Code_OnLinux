

package tools.javac.combo;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface Template {
    static final Pattern KEY_PATTERN = Pattern.compile("#\\{([A-Z_][A-Z0-9_]*(?:\\[\\d+\\])?)(?:\\.([A-Z0-9_]*))?\\}");

    String expand(String selector);

        
    public static String expandTemplate(String template,
                                        Map<String, Template> vars) {
        return expandTemplate(template, vars::get);
        }

    private static String expandTemplate(String template, Function<String, Template> resolver) {
            CharSequence in = template;
            StringBuffer out = new StringBuffer();
            while (true) {
                boolean more = false;
            Matcher m = KEY_PATTERN.matcher(in);
                while (m.find()) {
                    String major = m.group(1);
                    String minor = m.group(2);
                Template key = resolver.apply(major);
                    if (key == null)
                        throw new IllegalStateException("Unknown major key " + major);

                    String replacement = key.expand(minor == null ? "" : minor);
                more |= KEY_PATTERN.matcher(replacement).find();
                    m.appendReplacement(out, replacement);
                }
                m.appendTail(out);
                if (!more)
                    return out.toString();
                else {
                    in = out;
                    out = new StringBuffer();
                }
            }
    }
}

