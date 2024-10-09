

package hierarchies;


public abstract class TypeHierarchy<M extends TypeHierarchy.I, N extends TypeHierarchy.I> {
    
    public static final int ANSWER = 42;
    public static final int TEMP = 451;
    public static final int YEAR = 1984;

    private final M m;
    private final N n;
    private final Class<M> classM;
    private final Class<N> classN;

    protected TypeHierarchy(M m, N n, Class<M> classM, Class<N> classN) {
        this.m = m;
        this.n = n;
        this.classM = classM;
        this.classN = classN;
    }

    public final M getM() {
        return m;
    }

    public final N getN() {
        return n;
    }

    public final Class<M> getClassM() {
        return classM;
    }

    public final Class<N> getClassN() {
        return classN;
    }

    public interface I {
        int m();
    }

    public static class A implements I {
        @Override
        public int m() {
            return TypeHierarchy.ANSWER;
        }
    }
}
