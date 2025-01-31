
package xwp;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

public class TransformerFactoryWrapper extends TransformerFactory {

    private TransformerFactory defaultImpl = TransformerFactory.newDefaultInstance();

    @Override
    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        return defaultImpl.newTransformer(source);
    }

    @Override
    public Transformer newTransformer() throws TransformerConfigurationException {
        return defaultImpl.newTransformer();
    }

    @Override
    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        return defaultImpl.newTemplates(source);
    }

    @Override
    public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
        return defaultImpl.getAssociatedStylesheet(source, media, title, charset);
    }

    @Override
    public void setURIResolver(URIResolver resolver) {
        defaultImpl.setURIResolver(resolver);
    }

    @Override
    public URIResolver getURIResolver() {
        return defaultImpl.getURIResolver();
    }

    @Override
    public void setFeature(String name, boolean value) throws TransformerConfigurationException {
        defaultImpl.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name) {
        return defaultImpl.getFeature(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        defaultImpl.setAttribute(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return defaultImpl.getAttribute(name);
    }

    @Override
    public void setErrorListener(ErrorListener listener) {
        defaultImpl.setErrorListener(listener);
    }

    @Override
    public ErrorListener getErrorListener() {
        return defaultImpl.getErrorListener();
    }
}
