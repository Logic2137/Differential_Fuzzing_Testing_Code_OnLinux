
package pkg;

import static pkg.B.SubInner.Foo;

class B extends A {

    static class SubInner extends Inner {
    }
}

class A {

    static class Inner {

        static class Foo {
        }
    }
}
