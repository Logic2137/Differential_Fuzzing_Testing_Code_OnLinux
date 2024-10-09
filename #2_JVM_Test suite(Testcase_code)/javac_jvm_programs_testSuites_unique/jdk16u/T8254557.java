



import java.util.Iterator;
import java.util.function.Function;

public class T8254557 {
    
    public <T> void testIf(boolean b) {
        test(rs -> {
            if (b) {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public T next() {
                        return null;
                    }
                };
            } else {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public T next() {
                        return null;
                    }
                };
            }
        });
    }

    
    public <T> void testWhile(boolean b) {
        test(rs -> {
            while (b) {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public T next() {
                        return null;
                    }
                };
            }
            return null;
        });
    }

    
    public <T> void testDoWhileLoop(boolean b) {
        test(rs -> {
            do {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public T next() {
                        return null;
                    }
                };
            } while (b);
        });
    }

    
    public <T> void testForLoop(boolean b) {
        test(rs -> {
            for ( ; ; ) {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public T next() {
                        return null;
                    }
                };
            }
        });
    }

    private void test(Function function) { }
}
