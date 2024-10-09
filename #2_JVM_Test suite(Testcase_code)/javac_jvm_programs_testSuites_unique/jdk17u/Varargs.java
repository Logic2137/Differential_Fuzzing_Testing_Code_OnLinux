
package typeannos;

import java.lang.annotation.*;

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface VarArgA {
}

class Varargs {

    void varargPlain(Object@VarArgA ... objs) {
    }

    void varargGeneric(Class<?>@VarArgA ... clz) {
    }
}
