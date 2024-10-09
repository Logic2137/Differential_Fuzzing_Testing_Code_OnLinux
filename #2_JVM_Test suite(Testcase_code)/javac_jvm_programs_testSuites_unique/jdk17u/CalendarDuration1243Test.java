import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class CalendarDuration1243Test {

    public static void main(String[] args) {
        try {
            String dateTimeString = "2006-11-22T00:00:00.0+01:02";
            DatatypeFactory dtf = DatatypeFactory.newInstance();
            XMLGregorianCalendar cal = dtf.newXMLGregorianCalendar(dateTimeString);
            System.out.println("XMLGregCal:" + cal.toString());
            System.out.println("GregCal:" + cal.toGregorianCalendar());
            String toGCal = cal.toGregorianCalendar().toString();
            if (toGCal.indexOf("GMT+12:00") > -1) {
                throw new RuntimeException("Expected GMT+01:02");
            }
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
