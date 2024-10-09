
package pkg;

class PrivateParent {

    private PrivateParent(int i) {
    }

    public PrivateParent returnTypeTest() {
        return this;
    }
}
