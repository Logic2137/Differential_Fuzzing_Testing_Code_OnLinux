

package testTypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WhenType",
         propOrder = {"dtime"})
public class WhenType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtime;

    
    public XMLGregorianCalendar getDtime() {
        return dtime;
    }

    
    public void setDtime(XMLGregorianCalendar value) {
        this.dtime = value;
    }

}
