
package xp1;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;

public class XPathFactoryImpl extends XPathFactory {

    @Override
    public boolean isObjectModelSupported(String objectModel) {
        return true;
    }

    @Override
    public void setFeature(String name, boolean value) throws XPathFactoryConfigurationException {
    }

    @Override
    public boolean getFeature(String name) throws XPathFactoryConfigurationException {
        return false;
    }

    @Override
    public void setXPathVariableResolver(XPathVariableResolver resolver) {
    }

    @Override
    public void setXPathFunctionResolver(XPathFunctionResolver resolver) {
    }

    @Override
    public XPath newXPath() {
        return null;
    }
}
