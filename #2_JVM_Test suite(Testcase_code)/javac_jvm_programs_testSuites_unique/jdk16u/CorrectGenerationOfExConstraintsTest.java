



public class CorrectGenerationOfExConstraintsTest {
    public static class MyExBase extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public static class MyEx1 extends MyExBase {
        private static final long serialVersionUID = 1L;
    }

    public static class MyEx2 extends MyExBase {
        private static final long serialVersionUID = 1L;
    }

    public interface MyLambdaIF1 <E extends Exception> {
        void lambdaFun() throws E, MyEx2;
    }

    public interface MyLambdaIF2 <E extends Exception> {
        void lambdaFun() throws MyEx2, E;
    }

    public <E extends Exception> void fun1(MyLambdaIF1<E> myLambda) throws E, MyEx2 {
        myLambda.lambdaFun();
    }

    public <E extends Exception> void fun2(MyLambdaIF2<E> myLambda) throws E, MyEx2 {
        myLambda.lambdaFun();
    }

    public void useIt1() throws MyEx1, MyEx2 {
        fun1(this::lambda);
    }

    public void useIt1a() throws MyExBase {
        fun1(this::lambda);
    }

    public void useIt2() throws MyEx1, MyEx2 {
        fun2(this::lambda);
    }

    public void lambda() throws MyEx1, MyEx2 {
        if (Math.random() > 0.5)
        throw new MyEx2();
        throw new MyEx1();
    }
}
