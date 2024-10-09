
package jdk.jfr.api.metadata.settingdescriptor;

import java.util.Set;

import jdk.jfr.SettingControl;

public class PlainSetting extends SettingControl {

    public final static String DEFAULT_VALUE = "plain";

    @Override
    public String combine(Set<String> settingValue) {
        return DEFAULT_VALUE;
    }

    @Override
    public void setValue(String value) {
    }

    @Override
    public String getValue() {
        return DEFAULT_VALUE;
    }
}
