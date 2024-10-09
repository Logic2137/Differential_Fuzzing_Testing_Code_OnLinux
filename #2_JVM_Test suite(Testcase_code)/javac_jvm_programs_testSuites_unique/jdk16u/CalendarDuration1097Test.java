



import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;



public class CalendarDuration1097Test {

    
    public static void main(String[] args) {
        try {
            String dateTimeString = "0001-01-01T00:00:00.0000000-05:00";
            DatatypeFactory dtf = DatatypeFactory.newInstance();
            XMLGregorianCalendar cal = dtf.newXMLGregorianCalendar( dateTimeString );
            System.out.println( "Expected: 0001-01-01T00:00:00.0000000-05:00");
            System.out.println( "Actual:" + cal.toString() );
            System.out.println( "toXMLFormat:" + cal.toXMLFormat() );
            String test = cal.toString();
            if (test.indexOf("E-7") > -1) {
                throw new RuntimeException("Expected: 0001-01-01T00:00:00.0000000-05:00");
            }
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
