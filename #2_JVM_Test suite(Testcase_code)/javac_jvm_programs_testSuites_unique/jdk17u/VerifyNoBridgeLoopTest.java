public class VerifyNoBridgeLoopTest {

    static class Expression {
    }

    abstract static class ExpVisitor<R, D> {

        static int f = 1;

        protected R visitExpression(Expression exp, D d) {
            f *= 100;
            System.out.println(exp);
            return null;
        }
    }

    abstract static class ExpExpVisitor<D> extends ExpVisitor<Expression, D> {
    }

    static class FindTail extends ExpExpVisitor<Expression> {

        protected Expression visitExpression(Expression exp, Expression returnContinuation) {
            return super.visitExpression(exp, exp);
        }
    }

    public static void main(String[] args) {
        new FindTail().visitExpression(new Expression(), new Expression());
        ExpVisitor<Expression, Expression> e = new FindTail();
        e.visitExpression(new Expression(), new Expression());
        if (e.f != 10000)
            throw new AssertionError("Incorrect call sequence");
    }
}
