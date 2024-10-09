

package validation;

import java.io.IOException;
import java.io.Reader;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLSchemaValidator {

    private SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private Node node = null;
    private Reader schema = null;

    public XMLSchemaValidator(Node doc, Reader schema) {
        this.node = doc;
        this.schema = schema;
    }

    public synchronized void validate() throws SAXException, IOException {

        if (node == null || schema == null)
            return;

        Source schemaFile = new StreamSource(schema);
        Schema schema = factory.newSchema(schemaFile);
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(node));
    }
}
