
package jdk.jfr.api.metadata.settingdescriptor;

import java.util.Set;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.SettingControl;
import jdk.jfr.Timestamp;

@Name(AnnotatedSetting.NAME)
@Label(AnnotatedSetting.LABEL)
@Description(AnnotatedSetting.DESCRIPTION)
@Timestamp(Timestamp.TICKS)
public class AnnotatedSetting extends SettingControl {

    public final static String LABEL = "Annotated Label";

    public final static String DESCRIPTION = "Description of an annotated setting";

    public final static String NAME = "annotatedType";

    public final static String DEFAULT_VALUE = "defaultAnnotated";

    @Override
    public String combine(Set<String> settingValues) {
        return DEFAULT_VALUE;
    }

    @Override
    public void setValue(String settingValue) {
    }

    @Override
    public String getValue() {
        return DEFAULT_VALUE;
    }
}
