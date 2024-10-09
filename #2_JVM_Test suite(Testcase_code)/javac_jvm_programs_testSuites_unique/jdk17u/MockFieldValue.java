
package test.java.time.temporal;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;

public final class MockFieldValue implements TemporalAccessor {

    private final TemporalField field;

    private final long value;

    public MockFieldValue(TemporalField field, long value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public boolean isSupported(TemporalField field) {
        return field != null && field.equals(this.field);
    }

    @Override
    public ValueRange range(TemporalField field) {
        if (field instanceof ChronoField) {
            if (isSupported(field)) {
                return field.range();
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.rangeRefinedBy(this);
    }

    @Override
    public long getLong(TemporalField field) {
        if (this.field.equals(field)) {
            return value;
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
    }
}
