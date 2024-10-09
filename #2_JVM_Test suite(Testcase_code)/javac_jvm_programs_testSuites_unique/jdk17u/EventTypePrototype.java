
package jdk.test.lib.jfr;

import java.util.ArrayList;
import java.util.List;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Name;
import jdk.jfr.ValueDescriptor;

public final class EventTypePrototype {

    private final List<ValueDescriptor> fields;

    private final List<AnnotationElement> annotations;

    private final String name;

    public EventTypePrototype(String name, List<AnnotationElement> as, List<ValueDescriptor> fields) {
        this.annotations = new ArrayList<>(as);
        this.annotations.add(new AnnotationElement(Name.class, name));
        this.fields = fields;
        this.name = name;
    }

    public EventTypePrototype(String name) {
        this(name, new ArrayList<>(), new ArrayList<>());
    }

    public int getFieldIndex(String key) {
        int index = 0;
        for (ValueDescriptor f : fields) {
            if (f.getName().equals(key)) {
                return index;
            }
            index++;
        }
        throw new NoSuchFieldError(key);
    }

    public void addField(ValueDescriptor fieldDescriptor) {
        fields.add(fieldDescriptor);
    }

    public void addAnnotation(AnnotationElement annotation) {
        annotations.add(annotation);
    }

    public List<ValueDescriptor> getFields() {
        return fields;
    }

    public List<AnnotationElement> getAnnotations() {
        return annotations;
    }

    public String getName() {
        return name;
    }
}
