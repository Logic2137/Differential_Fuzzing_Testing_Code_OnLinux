
package xp2;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

public class DatatypeFactoryImpl extends DatatypeFactory {

    @Override
    public Duration newDuration(String lexicalRepresentation) {
        return null;
    }

    @Override
    public Duration newDuration(long durationInMilliSeconds) {
        return null;
    }

    @Override
    public Duration newDuration(boolean isPositive, BigInteger years, BigInteger months, BigInteger days, BigInteger hours, BigInteger minutes, BigDecimal seconds) {
        return null;
    }

    @Override
    public XMLGregorianCalendar newXMLGregorianCalendar() {
        return null;
    }

    @Override
    public XMLGregorianCalendar newXMLGregorianCalendar(String lexicalRepresentation) {
        return null;
    }

    @Override
    public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar cal) {
        return null;
    }

    @Override
    public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger year, int month, int day, int hour, int minute, int second, BigDecimal fractionalSecond, int timezone) {
        return null;
    }
}
