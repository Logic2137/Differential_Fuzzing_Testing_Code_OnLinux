import java.lang.annotation.*;

public class Lambda {

    interface LambdaInt {

        <S, T> void generic(S p1, T p2);
    }

    static class LambdaImpl implements LambdaInt {

        <S, T> LambdaImpl(S p1, T p2) {
        }

        public <S, T> void generic(S p1, T p2) {
        }
    }

    LambdaInt getMethodRefTA(LambdaImpl r) {
        return r::<@TA Object, @TB Object>generic;
    }

    LambdaInt getConstructorRefTA() {
        return LambdaImpl::<@TA Object, @TB Object>new;
    }
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface TA {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface TB {
}
