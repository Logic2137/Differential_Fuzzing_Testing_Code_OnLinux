
package xp1;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

public class TransformerFactoryImpl extends TransformerFactory {

    @Override
    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        return null;
    }

    @Override
    public Transformer newTransformer() throws TransformerConfigurationException {
        return null;
    }

    @Override
    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        return null;
    }

    @Override
    public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
        return null;
    }

    @Override
    public void setURIResolver(URIResolver resolver) {
    }

    @Override
    public URIResolver getURIResolver() {
        return null;
    }

    @Override
    public void setFeature(String name, boolean value) throws TransformerConfigurationException {
    }

    @Override
    public boolean getFeature(String name) {
        return false;
    }

    @Override
    public void setAttribute(String name, Object value) {
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public void setErrorListener(ErrorListener listener) {
    }

    @Override
    public ErrorListener getErrorListener() {
        return null;
    }
}
