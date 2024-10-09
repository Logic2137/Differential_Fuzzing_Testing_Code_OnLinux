
package jdk.deprcases.members;

public enum ExampleEnum {

    ONE, TWO {

        @Override
        public void deprMethod1() {
        }

        @Override
        @Deprecated
        public void deprMethod2() {
        }
    }
    , @Deprecated
    THREE, FOUR, @Deprecated
    FIVE;

    @Deprecated
    public void deprMethod1() {
    }

    public void deprMethod2() {
    }
}
