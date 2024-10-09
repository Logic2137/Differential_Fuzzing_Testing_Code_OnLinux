
package jdk.test.lib.cli.predicate;

import java.util.function.BooleanSupplier;

public class AndPredicate implements BooleanSupplier {

    private final BooleanSupplier a;

    private final BooleanSupplier b;

    private final BooleanSupplier c;

    public AndPredicate(BooleanSupplier a, BooleanSupplier b) {
        this.a = a;
        this.b = b;
        this.c = () -> true;
    }

    public AndPredicate(BooleanSupplier a, BooleanSupplier b, BooleanSupplier c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean getAsBoolean() {
        return a.getAsBoolean() && b.getAsBoolean() && c.getAsBoolean();
    }
}
