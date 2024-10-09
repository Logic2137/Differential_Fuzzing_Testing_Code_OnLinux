


public class T8055963 {

    static class C<T> {}

    <T> T choose(T first, T second) { return null; }

    void test() {
        C<String> cs = choose(new C<String>(), new C<>());
    }

    public static void main(String [] args) {
      new T8055963().test();
    }
}
