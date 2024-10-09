public class PreflowNotVisitingLambdaExpTest {

    interface HandleCallback<T, X extends Exception> {

        T withHandle(Handle handle) throws X;
    }

    interface HandleConsumer<X extends Exception> {

        void useHandle(Handle handle) throws X;
    }

    interface Handle {
    }

    interface Jdbi {

        <R, X extends Exception> R withHandle(HandleCallback<R, X> callback) throws X;

        <X extends Exception> void useHandle(final HandleConsumer<X> callback) throws X;
    }

    interface ObjectAssert<ACTUAL> {

        void isSameAs(ACTUAL t);
    }

    static <T> ObjectAssert<T> assertThat(T actual) {
        return null;
    }

    private Jdbi jdbi;

    public void nestedUseHandle() {
        jdbi.withHandle(h1 -> {
            jdbi.useHandle(h2 -> assertThat(h1).isSameAs(h2));
            return null;
        });
    }
}
