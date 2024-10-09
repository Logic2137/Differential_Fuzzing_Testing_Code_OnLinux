
package jjjjj.security.auth;

import javax.management.remote.JMXPrincipal;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.Exception;
import java.lang.RuntimeException;
import java.security.Principal;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.io.FileOutputStream;

public class Subject implements java.io.Serializable {

    private static final long serialVersionUID = -8308522755600156056L;

    Set<Principal> principals;

    private volatile boolean readOnly = false;

    private static final int PRINCIPAL_SET = 1;

    public Subject(Set<? extends Principal> principals) {
        this.principals = Collections.synchronizedSet(new SecureSet<Principal>(this, PRINCIPAL_SET, principals));
    }

    public Set<Principal> getPrincipals() {
        return principals;
    }

    private static class SecureSet<E> extends AbstractSet<E> implements java.io.Serializable {

        private static final long serialVersionUID = 7911754171111800359L;

        private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("this$0", Subject.class), new ObjectStreamField("elements", LinkedList.class), new ObjectStreamField("which", int.class) };

        Subject subject;

        LinkedList<E> elements;

        private int which;

        SecureSet(Subject subject, int which, Set<? extends E> set) {
            this.subject = subject;
            this.which = which;
            this.elements = new LinkedList<E>(set);
        }

        public Iterator<E> iterator() {
            return elements.iterator();
        }

        public int size() {
            return elements.size();
        }

        private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
            ObjectOutputStream.PutField fields = oos.putFields();
            fields.put("this$0", subject);
            fields.put("elements", elements);
            fields.put("which", which);
            oos.writeFields();
        }
    }

    public static byte[] enc(Object obj) {
        try {
            ByteArrayOutputStream bout;
            bout = new ByteArrayOutputStream();
            new ObjectOutputStream(bout).writeObject(obj);
            byte[] data = bout.toByteArray();
            for (int i = 0; i < data.length - 5; i++) {
                if (data[i] == 'j' && data[i + 1] == 'j' && data[i + 2] == 'j' && data[i + 3] == 'j' && data[i + 4] == 'j') {
                    System.arraycopy("javax".getBytes(), 0, data, i, 5);
                }
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
