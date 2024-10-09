
package pkg;

import java.lang.annotation.*;

public interface BaseInterface {

    public class NestedClassFromInterface {
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationClass);
}
