


package pkg;

import pkg.A.Outer.Inner;

class B extends Inner {
}

class A {
   static class Outer<X extends Inner> { static class Inner {} }
}

