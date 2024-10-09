


class DeprecationSE8Test {
    @FunctionalInterface
    interface I {
        DeprecationSE8Test meth();
    }

    @FunctionalInterface
    interface J {
        int meth();
    }

    @Deprecated
    public DeprecationSE8Test() {
    }

    @Deprecated
    public static int foo() {
        return 1;
    }

    
    void notBadUsages() {
        I i = DeprecationSE8Test::new;
        new DeprecationSE8Test();
        J j = DeprecationSE8Test::foo;
        foo();
    }
}

class DeprecationSE8_01 {
    
    void badUsages() {
        DeprecationSE8Test.I i = DeprecationSE8Test::new;
        new DeprecationSE8Test();
        DeprecationSE8Test.foo();
        DeprecationSE8Test.J j = DeprecationSE8Test::foo;
    }
}
