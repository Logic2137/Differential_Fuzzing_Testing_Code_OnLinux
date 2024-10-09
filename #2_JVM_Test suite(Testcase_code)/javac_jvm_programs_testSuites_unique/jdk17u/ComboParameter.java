
package combo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ComboParameter {

    Pattern pattern = Pattern.compile("#\\{([A-Z_][A-Z0-9_]*(?:\\[\\d+\\])?)(?:\\.([A-Z0-9_]*))?\\}");

    String expand(String optParameter);

    class Constant<D> implements ComboParameter {

        D data;

        public Constant(D data) {
            this.data = data;
        }

        @Override
        public String expand(String _unused) {
            return String.valueOf(data);
        }
    }

    interface Resolver {

        ComboParameter lookup(String name);
    }

    static String expandTemplate(String template, Resolver resolver) {
        CharSequence in = template;
        StringBuffer out = new StringBuffer();
        while (true) {
            boolean more = false;
            Matcher m = pattern.matcher(in);
            while (m.find()) {
                String parameterName = m.group(1);
                String minor = m.group(2);
                ComboParameter parameter = resolver.lookup(parameterName);
                if (parameter == null) {
                    throw new IllegalStateException("Unhandled parameter name " + parameterName);
                }
                String replacement = parameter.expand(minor);
                more |= pattern.matcher(replacement).find();
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
