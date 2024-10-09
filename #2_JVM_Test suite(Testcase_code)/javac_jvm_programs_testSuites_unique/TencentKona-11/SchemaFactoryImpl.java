

package xp1;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class SchemaFactoryImpl extends SchemaFactory {

    @Override
    public boolean isSchemaLanguageSupported(String schemaLanguage) {
        
        if (schemaLanguage.equals(W3C_XML_SCHEMA_NS_URI))
            return true;
        else
            return false;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {

    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public void setResourceResolver(LSResourceResolver resourceResolver) {

    }

    @Override
    public LSResourceResolver getResourceResolver() {
        return null;
    }

    @Override
    public Schema newSchema(Source[] schemas) throws SAXException {
        return null;
    }

    @Override
    public Schema newSchema() throws SAXException {
        return null;
    }

}
