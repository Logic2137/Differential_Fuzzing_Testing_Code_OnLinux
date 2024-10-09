

package jdk.jfr.api.metadata.annotations;

import java.util.HashMap;
import java.util.Map;

import jdk.jfr.AnnotationElement;
import jdk.jfr.MetadataDefinition;




public class TestDynamicAnnotation {
    @MetadataDefinition
    @interface CustomAnnotation {
        String value();
        int intValue();
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("value", "MyValue");
        values.put("intValue", 1);
        new AnnotationElement(CustomAnnotation.class, values);
    }
}
