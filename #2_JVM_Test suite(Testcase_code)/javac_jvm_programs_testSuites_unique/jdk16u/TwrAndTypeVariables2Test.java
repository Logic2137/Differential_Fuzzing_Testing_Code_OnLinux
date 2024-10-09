



abstract class TwrAndTypeVariables2Test_01 implements AutoCloseable {
    @Override
    public void close() {
    }
    public boolean close(long timeout) {
        return true;
    }
}

public abstract class TwrAndTypeVariables2Test<C extends TwrAndTypeVariables2Test_01> {
    abstract C newCloseable();

    void m() throws Exception{
        try(C p= newCloseable()){
        }
    }
}
