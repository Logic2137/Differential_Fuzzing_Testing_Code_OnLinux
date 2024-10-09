

package jdk.test.lib.cli.predicate;

import java.util.function.BooleanSupplier;

public class NotPredicate implements BooleanSupplier {
    private final BooleanSupplier s;

    public NotPredicate(BooleanSupplier s) {
        this.s = s;
    }

    @Override
    public boolean getAsBoolean() {
        return !s.getAsBoolean();
    }
}
