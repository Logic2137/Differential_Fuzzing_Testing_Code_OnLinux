



package org.example.ldapv4;

import java.net.*;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.spi.*;

public class ldapv4URLContext implements DirContext {

    private DirContext ctx;

    public ldapv4URLContext(Hashtable env) throws NamingException {
        ctx = new InitialDirContext(env);
    }

    public Object lookup(String name) throws NamingException {
        try {
            return ctx.lookup("");
        } catch (Exception e) {
            NamingException ne = new NamingException();
            ne.setRootCause(e);
            throw ne;
        } finally {
            ctx.close();
        }
    }

    public Object lookup(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void bind(String name, Object obj) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void bind(Name name, Object obj) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rebind(String name, Object obj) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rebind(Name name, Object obj) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void unbind(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void unbind(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rename(String oldName, String newName) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rename(Name name, Name newName) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<NameClassPair> list(String name)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<NameClassPair> list(Name name)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<Binding> listBindings(String name)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<Binding> listBindings(Name name)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void destroySubcontext(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void destroySubcontext(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Context createSubcontext(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Context createSubcontext(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Object lookupLink(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Object lookupLink(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NameParser getNameParser(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NameParser getNameParser(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public String composeName(String name, String prefix)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public String getNameInNamespace() throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Object removeFromEnvironment(String propName)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Object addToEnvironment(String propName, Object propVal)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Hashtable getEnvironment() throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void close() throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Attributes getAttributes(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Attributes getAttributes(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Attributes getAttributes(Name name, String[] attrIds)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Attributes getAttributes(String name, String[] attrIds)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void modifyAttributes(Name name, int mod_op, Attributes attrs)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void modifyAttributes(String name, int mod_op, Attributes attrs)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void modifyAttributes(Name name, ModificationItem[] mods)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void modifyAttributes(String name, ModificationItem[] mods)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void bind(Name name, Object obj, Attributes attrs)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void bind(String name, Object obj, Attributes attrs)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rebind(Name name, Object obj, Attributes attrs)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rebind(String name, Object obj, Attributes attrs)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public DirContext createSubcontext(Name name, Attributes attrs)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public DirContext createSubcontext(String name, Attributes attrs)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public DirContext getSchema(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public DirContext getSchema(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public DirContext getSchemaClassDefinition(Name name)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public DirContext getSchemaClassDefinition(String name)
            throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<SearchResult>
        search(Name name,
               Attributes matchingAttributes,
               String[] attributesToReturn)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<SearchResult>
        search(String name,
               Attributes matchingAttributes,
               String[] attributesToReturn)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<SearchResult>
        search(Name name, Attributes matchingAttributes)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<SearchResult>
        search(String name, Attributes matchingAttributes)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<SearchResult>
        search(Name name,
               String filter,
               SearchControls cons)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<SearchResult>
        search(String name,
               String filter,
               SearchControls cons)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<SearchResult>
        search(Name name,
               String filterExpr,
               Object[] filterArgs,
               SearchControls cons)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    public NamingEnumeration<SearchResult>
        search(String name,
               String filterExpr,
               Object[] filterArgs,
               SearchControls cons)
        throws NamingException {
        throw new OperationNotSupportedException();
    }
}
