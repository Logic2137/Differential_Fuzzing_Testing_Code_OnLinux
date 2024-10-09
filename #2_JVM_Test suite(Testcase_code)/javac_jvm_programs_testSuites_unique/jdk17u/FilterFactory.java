
package test.astro;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.TransformerConfigurationException;
import org.xml.sax.SAXException;

public interface FilterFactory {

    TransformerHandler newRAFilter(double min, double max) throws TransformerConfigurationException, SAXException, ParserConfigurationException, IOException;

    TransformerHandler newDECFilter(double min, double max) throws TransformerConfigurationException, SAXException, ParserConfigurationException, IOException;

    TransformerHandler newRADECFilter(double rmin, double rmax, double dmin, double dmax) throws TransformerConfigurationException, SAXException, ParserConfigurationException, IOException;

    TransformerHandler newStellarTypeFilter(String type) throws TransformerConfigurationException, SAXException, ParserConfigurationException, IOException;

    TransformerHandler newHTMLOutput() throws TransformerConfigurationException, SAXException, ParserConfigurationException, IOException;
}
