import java.util.HashSet;
import java.util.Set;

class InferStrict {

    public <T> Set<T> compute(Set<T> t) {
        return t;
    }

    public <T> T join(Set<T> t1, Set<T> t2) {
        return null;
    }

    public <T extends InferStrict> T compute() {
        return null;
    }

    public void test() {
        join(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(new HashSet<>()))))))))))))))))), compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(compute(new HashSet<String>())))))))))))))))))).length();
    }
}
