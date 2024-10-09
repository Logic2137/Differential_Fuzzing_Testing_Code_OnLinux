



import java.util.*;

class ExprTypeIsTypeVariableTest {
    abstract class A {}

    abstract class ACD<E> implements Iterable<E> {
        public Iterator<E> iterator() {
            return null;
        }
    }

    abstract class ALD<E> extends ACD<E> implements List<E> {}

    abstract class ASP<NT extends A> extends ALD<A> {
        <P extends ASP<NT>> void foo(P prod) {
            for (A sym : prod) {}
        }
    }
}
