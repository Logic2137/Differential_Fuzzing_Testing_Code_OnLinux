
package compiler.jvmci.compilerToVM;

interface DummyInterface {

    void dummyFunction();

    default int dummyDefaultFunction(int x, int y) {
        int z = x * y;
        return (int) (Math.cos(x - y + z) * 100);
    }
}
