

public class SelfInitializerInLambdaTestc {
    interface SAM {
        void foo();
    }

    final SAM notInitialized = ()-> {
        SAM simpleVariable = () -> notInitialized.foo();
    };

    final SAM notInitialized2 = ()-> {
        SAM simpleVariable1 = () -> {
            SAM simpleVariable2 = () -> {
                SAM simpleVariable3 = () -> {
                    SAM simpleVariable4 = () -> notInitialized2.foo();
                };
            };
        };
    };
}
