



import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import com.sun.tools.javac.util.Assert;

public class BadEnclosingMethodAttrTest<T> {
    protected BadEnclosingMethodAttrTest() {
        Assert.check(getClass().getEnclosingMethod().toString().equals("static void BadEnclosingMethodAttrTest.lambdaScope(java.lang.Object)"));
        Type typeFromEnclosingMethod = getClass().getEnclosingMethod().getGenericParameterTypes()[0];
        ParameterizedType paramType = (ParameterizedType) getClass().getGenericSuperclass();
        Type typeFromGenericClass = paramType.getActualTypeArguments()[0];
        Assert.check(typeFromEnclosingMethod.equals(typeFromGenericClass));
    }

    static <X> void lambdaScope(X x) {
        Runnable r = () -> {
            new BadEnclosingMethodAttrTest<X>() {};
        };
        r.run();
    }

    public static void main(final String[] args) {
        lambdaScope("");
    }
}
