class S<T> {

    T t;
}

class C {

    class I {
    }
}

class E extends S<C> {

    {
        t = new C();
        ((C) t).new I() {
        };
    }
}

class X {

    public static void main(String[] args) {
        new E();
    }
}
