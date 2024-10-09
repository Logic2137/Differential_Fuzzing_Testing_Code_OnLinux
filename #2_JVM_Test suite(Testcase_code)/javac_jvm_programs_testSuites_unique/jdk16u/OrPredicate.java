

package jdk.test.lib.cli.predicate;

import java.util.function.BooleanSupplier;

public class OrPredicate implements BooleanSupplier {
    private final BooleanSupplier a;
    private final BooleanSupplier b;

    public OrPredicate(BooleanSupplier a, BooleanSupplier b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean getAsBoolean() {
        return a.getAsBoolean() || b.getAsBoolean();
    }
}
