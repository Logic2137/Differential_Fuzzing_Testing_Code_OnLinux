
package test.java.time.temporal;

import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import java.time.DateTimeException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;

public enum MockFieldNoValue implements TemporalField {

    INSTANCE;

    @Override
    public TemporalUnit getBaseUnit() {
        return WEEKS;
    }

    @Override
    public TemporalUnit getRangeUnit() {
        return MONTHS;
    }

    @Override
    public ValueRange range() {
        return ValueRange.of(1, 20);
    }

    @Override
    public boolean isDateBased() {
        return false;
    }

    @Override
    public boolean isTimeBased() {
        return false;
    }

    @Override
    public boolean isSupportedBy(TemporalAccessor temporal) {
        return true;
    }

    @Override
    public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
        return ValueRange.of(1, 20);
    }

    @Override
    public long getFrom(TemporalAccessor temporal) {
        throw new DateTimeException("Mock");
    }

    @Override
    public <R extends Temporal> R adjustInto(R temporal, long newValue) {
        throw new DateTimeException("Mock");
    }

    @Override
    public String toString() {
        return null;
    }
}
