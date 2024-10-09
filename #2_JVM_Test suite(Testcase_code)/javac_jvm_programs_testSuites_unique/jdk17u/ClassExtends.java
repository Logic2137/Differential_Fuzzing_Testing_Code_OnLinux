
package typeannos;

import java.lang.annotation.*;

abstract class MyClass extends @ClassExtA ParameterizedClass<@ClassExtB String> implements @ClassExtB CharSequence, @ClassExtA ParameterizedInterface<@ClassExtB String> {
}

interface MyInterface extends @ClassExtA ParameterizedInterface<@ClassExtA String>, @ClassExtB CharSequence {
}

class ParameterizedClass<K> {
}

interface ParameterizedInterface<K> {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface ClassExtA {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface ClassExtB {
}
