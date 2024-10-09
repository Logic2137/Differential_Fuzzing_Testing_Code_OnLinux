
package nsk.share.gc;

public class TwoFieldsObject<T> {

    private T left, right;

    public TwoFieldsObject(T left, T right) {
        setLeft(left);
        setRight(right);
    }

    public final void setLeft(T left) {
        this.left = left;
    }

    public final void setRight(T right) {
        this.right = right;
    }

    public final T getLeft() {
        return left;
    }

    public final T getRight() {
        return right;
    }
}
