



public class CaptureGLB2 {

    interface A<T> { }

    Class<?> bar(A<? super Class<? extends Exception>> x, A<? super Class<? extends Throwable>> y){
        return foo(x, y);
    }

    <T> T foo(A<? super T> x, A<? super T> y){
        return null;
    }
}
