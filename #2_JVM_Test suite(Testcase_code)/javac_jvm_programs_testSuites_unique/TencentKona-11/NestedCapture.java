



abstract class NestedCapture {
  interface List<T> {}
  abstract <T> List<T> copyOf(List<? extends T> lx);
  abstract <E> List<E> filter(List<E> lx);

  <U> void test1(List<U> lx) {
    copyOf(filter(lx));
  }

  void test2(List<?> lx) {
    copyOf(filter(lx));
  }

}
