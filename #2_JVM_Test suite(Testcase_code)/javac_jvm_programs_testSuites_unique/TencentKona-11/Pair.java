

package jdk.test.lib.util;

import java.util.Objects;


public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ":" + second + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Pair<?, ?>) {
            Pair<?, ?> otherPair = (Pair<?, ?>) other;
            return Objects.equals(first, otherPair.first) &&
                    Objects.equals(second, otherPair.second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (first == null) {
            return (second == null) ? 0 : second.hashCode();
        } else if (second == null) {
            return first.hashCode();
        } else {
            return first.hashCode() * 17 + second.hashCode();
        }
    }
}
