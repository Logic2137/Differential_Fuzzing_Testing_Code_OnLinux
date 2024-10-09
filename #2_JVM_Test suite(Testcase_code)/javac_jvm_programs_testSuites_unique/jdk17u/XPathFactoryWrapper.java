
package xwp;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;

public class XPathFactoryWrapper extends XPathFactory {

    private XPathFactory defaultImpl = XPathFactory.newDefaultInstance();

    @Override
    public boolean isObjectModelSupported(String objectModel) {
        return defaultImpl.isObjectModelSupported(objectModel);
    }

    @Override
    public void setFeature(String name, boolean value) throws XPathFactoryConfigurationException {
        defaultImpl.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name) throws XPathFactoryConfigurationException {
        return defaultImpl.getFeature(name);
    }

    @Override
    public void setXPathVariableResolver(XPathVariableResolver resolver) {
        defaultImpl.setXPathVariableResolver(resolver);
    }

    @Override
    public void setXPathFunctionResolver(XPathFunctionResolver resolver) {
        defaultImpl.setXPathFunctionResolver(resolver);
    }

    @Override
    public XPath newXPath() {
        return defaultImpl.newXPath();
    }
}
