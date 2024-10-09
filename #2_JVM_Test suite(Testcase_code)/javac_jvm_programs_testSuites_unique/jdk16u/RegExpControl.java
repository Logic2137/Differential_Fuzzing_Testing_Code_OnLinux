

package jdk.jfr.api.settings;

import java.util.Set;
import java.util.regex.Pattern;

import jdk.jfr.SettingControl;

public final class RegExpControl extends SettingControl {
    private Pattern pattern = Pattern.compile(".*");

    public void setValue(String value) {
        this.pattern = Pattern.compile(value);
    }

    public String combine(Set<String> values) {
        return String.join("|", values);
    }

    public String getValue() {
        return pattern.toString();
    }

    public boolean matches(String uri) {
        return pattern.matcher(uri).find();
    }
}
