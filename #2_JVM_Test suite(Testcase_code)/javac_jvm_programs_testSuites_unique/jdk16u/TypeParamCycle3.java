


package pkg;

import static pkg.A.Outer.Inner;

class A {
   static class Outer<X extends Inner> extends B { }
}

class B {
    static class Inner {}
}

