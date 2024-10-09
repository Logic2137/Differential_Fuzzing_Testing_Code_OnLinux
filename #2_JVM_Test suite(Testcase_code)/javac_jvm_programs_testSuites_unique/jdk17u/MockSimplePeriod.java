
package tck.java.time;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.FOREVER;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.time.DateTimeException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Objects;

public final class MockSimplePeriod implements TemporalAmount, Comparable<MockSimplePeriod> {

    public static final MockSimplePeriod ZERO_DAYS = new MockSimplePeriod(0, DAYS);

    public static final MockSimplePeriod ZERO_SECONDS = new MockSimplePeriod(0, SECONDS);

    private final long amount;

    private final TemporalUnit unit;

    public static MockSimplePeriod of(long amount, TemporalUnit unit) {
        return new MockSimplePeriod(amount, unit);
    }

    private MockSimplePeriod(long amount, TemporalUnit unit) {
        Objects.requireNonNull(unit, "unit");
        if (unit == FOREVER) {
            throw new DateTimeException("Cannot create a period of the Forever unit");
        }
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public long get(TemporalUnit unit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TemporalUnit> getUnits() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getAmount() {
        return amount;
    }

    public TemporalUnit getUnit() {
        return unit;
    }

    @Override
    public Temporal addTo(Temporal temporal) {
        return temporal.plus(amount, unit);
    }

    @Override
    public Temporal subtractFrom(Temporal temporal) {
        return temporal.minus(amount, unit);
    }

    @Override
    public int compareTo(MockSimplePeriod otherPeriod) {
        if (unit.equals(otherPeriod.getUnit()) == false) {
            throw new IllegalArgumentException("Units cannot be compared: " + unit + " and " + otherPeriod.getUnit());
        }
        return Long.compare(amount, otherPeriod.amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MockSimplePeriod) {
            MockSimplePeriod other = (MockSimplePeriod) obj;
            return this.amount == other.amount && this.unit.equals(other.unit);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return unit.hashCode() ^ (int) (amount ^ (amount >>> 32));
    }

    @Override
    public String toString() {
        return amount + " " + unit;
    }
}
