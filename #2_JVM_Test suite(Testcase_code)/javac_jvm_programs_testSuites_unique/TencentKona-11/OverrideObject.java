

package jdk.nashorn.test.models;

@SuppressWarnings("javadoc")
public class OverrideObject {
    @Override
    public int hashCode() {
        return 5;
    }

    @Override
    public String toString() {
        return "override-object";
    }

    @Override
    public boolean equals(final Object o) {
        
        return false;
    }
}
