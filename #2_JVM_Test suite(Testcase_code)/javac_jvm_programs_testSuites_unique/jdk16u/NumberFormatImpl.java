
package test;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class NumberFormatImpl extends NumberFormat {

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo,
                               FieldPosition pos) {
        return null;
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo,
                               FieldPosition pos) {
        return null;
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return null;
    }
}

