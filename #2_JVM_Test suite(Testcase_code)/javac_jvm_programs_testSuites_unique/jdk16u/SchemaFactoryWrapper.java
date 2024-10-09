

package xwp;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class SchemaFactoryWrapper extends SchemaFactory {
    private SchemaFactory defaultImpl = SchemaFactory.newDefaultInstance();

    @Override
    public boolean isSchemaLanguageSupported(String schemaLanguage) {
        return defaultImpl.isSchemaLanguageSupported(schemaLanguage);
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        defaultImpl.setErrorHandler(errorHandler);
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return defaultImpl.getErrorHandler();
    }

    @Override
    public void setResourceResolver(LSResourceResolver resourceResolver) {
        defaultImpl.setResourceResolver(resourceResolver);
    }

    @Override
    public LSResourceResolver getResourceResolver() {
        return defaultImpl.getResourceResolver();
    }

    @Override
    public Schema newSchema(Source[] schemas) throws SAXException {
        return defaultImpl.newSchema(schemas);
    }

    @Override
    public Schema newSchema() throws SAXException {
        return defaultImpl.newSchema();
    }

}
