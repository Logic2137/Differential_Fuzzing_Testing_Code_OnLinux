



public class SubtypeCaptureLeak {

    interface Parent<T> {}
    interface Child<T> extends Parent<T> {}
    interface Box<T> {}
    interface SubBox<T> extends Box<T> {}

    

    <T> void m1(Parent<? extends T> arg) {}

    void testApplicable(Child<?> arg) {
        m1(arg);
    }

    

    <T> void m2(Box<? extends Parent<? extends T>> arg) {}

    void testApplicable(Box<Child<?>> arg) {
        m2(arg);
    }

    

    <T> void m3(Parent<? extends T> arg) {}
    void m3(Child<?> arg) {}

    void testMostSpecific(Child<?> arg) {
        m3(arg);
    }

    

    <T> void m4(Box<? extends Parent<? extends T>> arg) {}
    void m4(SubBox<Child<?>> arg) {}

    void testMostSpecificNested(SubBox<Child<?>> arg) {
        m4(arg);
    }

}
