public class FilterNonMembersToObtainFunctionDescriptorTest {

    <V, E extends Exception> V fails(CallableFail<V, E> callable) throws E {
        return null;
    }

    <V, E extends Exception> V failsSub(CallableFailSub<V, E> callable) throws E {
        return null;
    }

    void m() throws Exception {
        fails((String s) -> 2);
        failsSub((String s) -> 2);
    }

    interface Callable<V> {

        V callFail(String s) throws Exception;
    }

    interface CallableFail<V, E extends Exception> extends Callable<V> {

        @Override
        V callFail(String s) throws E;
    }

    interface CallableFailSub<V, E extends Exception> extends CallableFail<V, E> {
    }
}
