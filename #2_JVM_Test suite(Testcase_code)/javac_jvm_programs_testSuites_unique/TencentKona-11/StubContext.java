
package util;

import java.util.Hashtable;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

@SuppressWarnings("unchecked")
public class StubContext implements Context {

    @Override
    public Object lookup(Name name) throws NamingException {
        return null;
    }

    @Override
    public Object lookup(String name) throws NamingException {
        return null;
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {

    }

    @Override
    public void bind(String name, Object obj) throws NamingException {

    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {

    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {

    }

    @Override
    public void unbind(Name name) throws NamingException {

    }

    @Override
    public void unbind(String name) throws NamingException {

    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {

    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {

    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return new NamingEnumerationStub();
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return new NamingEnumerationStub();
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return new NamingEnumerationStub();
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        return new NamingEnumerationStub();
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {

    }

    @Override
    public void destroySubcontext(String name) throws NamingException {

    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        return null;
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return null;
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        return null;
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return null;
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        return new NameParserStub();
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        return new NameParserStub();
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {
        return null;
    }

    @Override
    public String composeName(String name, String prefix) throws NamingException {
        return null;
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return null;
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        return null;
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return new Hashtable();
    }

    @Override
    public void close() throws NamingException {

    }

    @Override
    public String getNameInNamespace() throws NamingException {
        return null;
    }

    class NamingEnumerationStub implements NamingEnumeration {

        @Override
        public Object next() throws NamingException {
            return null;
        }

        @Override
        public boolean hasMore() throws NamingException {
            return false;
        }

        @Override
        public void close() throws NamingException {

        }

        @Override
        public boolean hasMoreElements() {
            return false;
        }

        @Override
        public Object nextElement() {
            return null;
        }

    }

    class NameParserStub implements NameParser {

        @Override
        public Name parse(String name) throws NamingException {
            return null;
        }

    }

}
