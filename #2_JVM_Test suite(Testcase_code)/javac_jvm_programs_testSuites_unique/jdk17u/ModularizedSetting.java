
package test.jfr.setting;

import java.util.Set;
import jdk.jfr.SettingControl;

public final class ModularizedSetting extends SettingControl {

    private String value = "false";

    private boolean isTrue = false;

    @Override
    public String combine(Set<String> settingValues) {
        for (String s : settingValues) {
            if ("true".equals(s)) {
                return "true";
            }
        }
        return "false";
    }

    @Override
    public void setValue(String settingValue) {
        this.value = settingValue;
        this.isTrue = Boolean.valueOf(settingValue);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean accept() {
        return isTrue;
    }
}
