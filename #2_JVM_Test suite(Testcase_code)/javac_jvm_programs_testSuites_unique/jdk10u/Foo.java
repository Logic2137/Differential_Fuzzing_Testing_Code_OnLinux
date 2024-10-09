

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"fooObject"})
@XmlRootElement(name = "Foo")

public class Foo {

    protected List<Foo> fooObject;

    public List<Foo> getFooObject() {
        if (fooObject == null) {
            fooObject = new ArrayList<Foo>();
        }
        return this.fooObject;
    }
}
