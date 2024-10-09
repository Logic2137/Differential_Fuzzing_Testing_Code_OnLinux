import java.util.stream.Stream;

class T8073842 {

    static class Chunck {

        public void work() {
        }
    }

    void test(Stream<? extends Chunck> s) {
        Stream<Runnable> r = s.map(o -> o::work);
    }
}
