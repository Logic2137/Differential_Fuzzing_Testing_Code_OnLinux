
package jdk.nashorn.test.models;

@SuppressWarnings("javadoc")
public abstract class Toothpaste {

    public void applyToBrush() {
        applyToBrushImpl();
    }

    protected abstract void applyToBrushImpl();
}
