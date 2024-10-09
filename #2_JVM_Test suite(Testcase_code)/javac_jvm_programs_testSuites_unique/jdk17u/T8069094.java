@interface T8069094 {

    T8069094A foo() default T8069094A.Bar;
}

@Deprecated
enum T8069094A {

    Bar
}
