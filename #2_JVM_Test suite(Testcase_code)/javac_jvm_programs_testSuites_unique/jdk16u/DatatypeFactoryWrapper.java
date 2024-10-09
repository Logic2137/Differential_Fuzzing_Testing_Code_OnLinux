

package xwp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

public class DatatypeFactoryWrapper extends DatatypeFactory {
    private DatatypeFactory defaultImpl = DatatypeFactory.newDefaultInstance();

    @Override
    public Duration newDuration(String lexicalRepresentation) {
        return defaultImpl.newDuration(lexicalRepresentation);
    }

    @Override
    public Duration newDuration(long durationInMilliSeconds) {
        return defaultImpl.newDuration(durationInMilliSeconds);
    }

    @Override
    public Duration newDuration(boolean isPositive, BigInteger years, BigInteger months, BigInteger days,
            BigInteger hours, BigInteger minutes, BigDecimal seconds) {
        return defaultImpl.newDuration(isPositive, years, months, days, hours, minutes, seconds);
    }

    @Override
    public XMLGregorianCalendar newXMLGregorianCalendar() {
        return defaultImpl.newXMLGregorianCalendar();
    }

    @Override
    public XMLGregorianCalendar newXMLGregorianCalendar(String lexicalRepresentation) {
        return defaultImpl.newXMLGregorianCalendar(lexicalRepresentation);
    }

    @Override
    public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar cal) {
        return defaultImpl.newXMLGregorianCalendar(cal);
    }

    @Override
    public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger year, int month, int day, int hour,
            int minute, int second, BigDecimal fractionalSecond, int timezone) {
        return defaultImpl.newXMLGregorianCalendar(year, month, day, hour, minute, second, fractionalSecond, timezone);
    }

}
