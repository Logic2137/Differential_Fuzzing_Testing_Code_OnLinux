import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.sun.security.auth.UnixPrincipal;
import javax.security.auth.Subject;

public class Subset {

    public static void main(String[] args) throws Exception {
        int exceptionCounter = 0;
        Iterator iter1;
        HashSet creds = new HashSet();
        Subject emptys = new Subject(false, Collections.singleton(new UnixPrincipal("user")), Collections.EMPTY_SET, creds);
        Set princ = emptys.getPrincipals();
        HashSet collp = new HashSet();
        collp.add(new String("abc"));
        collp.add(new String("def"));
        collp.add(new String("Exists"));
        collp.add(new String("Does not Exist"));
        try {
            if (princ.containsAll(collp)) {
                throw new Exception("Error: Contains the collection");
            } else
                System.out.println("Does not Contain the collection");
        } catch (SecurityException e) {
            throw new Exception("Error: Exception in containsAll (string coll)!!");
        }
        Set p1 = emptys.getPrivateCredentials();
        if (p1.size() != 0) {
            throw new Exception("Error:p1 size should have been 6 and was " + p1.size());
        }
        creds.add("abc");
        creds.add(new Integer(3));
        creds.add(Boolean.TRUE);
        Subject sremove = new Subject(false, Collections.singleton(new UnixPrincipal("user")), Collections.EMPTY_SET, creds);
        Set p2 = sremove.getPrivateCredentials();
        if (p2.size() != 3) {
            throw new Exception("Error: p2 size should have been 3 and was " + p2.size());
        }
        iter1 = p2.iterator();
        exceptionCounter = 0;
        while (iter1.hasNext()) {
            try {
                Object o = iter1.next();
                System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
            } catch (SecurityException e) {
                System.out.println("Expected Exception occured");
                exceptionCounter++;
            }
        }
        if (exceptionCounter != 2) {
            throw new Exception("Expected number of exceptions was 2 " + "The actual number was " + exceptionCounter);
        }
        iter1.remove();
        if (p2.size() != 2) {
            throw new RuntimeException("Error: p2 size should have been 2 and was " + p2.size());
        }
        System.out.println("Checking the value after removal");
        p2 = sremove.getPrivateCredentials();
        try {
            if (!p2.add(new String("XYZ"))) {
                throw new RuntimeException("Error in adding string");
            }
            if (!p2.add(new Integer(99))) {
                throw new RuntimeException("Error in adding Integer");
            }
            HashSet coll1 = new HashSet();
            coll1.add(new String("RST"));
            coll1.add(new Integer(1));
            if (!p2.addAll(coll1)) {
                throw new RuntimeException("Error in addAll");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception in add");
        }
        iter1 = p2.iterator();
        while (iter1.hasNext()) {
            try {
                Object o = iter1.next();
                System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
            } catch (SecurityException e) {
            }
        }
        iter1 = p2.iterator();
        System.out.println("Checked the value after removal");
        HashSet creds1 = new HashSet();
        creds1.add("abc");
        creds1.add("def");
        creds1.add(Boolean.TRUE);
        creds1.add(new Integer(1));
        creds1.add(new String("Exists"));
        Subject scontain = new Subject(false, Collections.singleton(new UnixPrincipal("user")), Collections.EMPTY_SET, creds1);
        p2 = scontain.getPrivateCredentials();
        try {
            Object ObjAr = p2.toArray();
        } catch (SecurityException e) {
            System.out.println("Should get an Exception in toArray()");
        }
        HashSet creds3 = new HashSet();
        creds3.add(new String("abc"));
        p2 = scontain.getPrivateCredentials();
        try {
            Object ObjCred = (Object) creds3.clone();
            System.out.println("Size of p2 is " + p2.size() + "Size of ObjCred is " + ((HashSet) ObjCred).size());
            if (p2.equals(ObjCred))
                throw new RuntimeException("Error:Equals ObjCred  *** ");
            else
                System.out.println("Does not Equal Objcred");
        } catch (SecurityException e) {
            throw new RuntimeException("Error:Should not get an Exception in equals of creds3");
        }
        try {
            Object ObjCred = (Object) creds1.clone();
            System.out.println("Size of p2 is " + p2.size() + "Size of ObjCred is " + ((HashSet) ObjCred).size());
            if (p2.equals(ObjCred))
                throw new RuntimeException("Error: Equals ObjCred");
            else
                throw new RuntimeException("Error: Does not Equal Objcred");
        } catch (SecurityException e) {
            System.out.println("Should get an Exception in equals of creds1");
        }
        HashSet creds2 = new HashSet();
        creds2.add("abc");
        creds2.add("def");
        creds2.add("ghi");
        Subject sstring = new Subject(false, Collections.singleton(new UnixPrincipal("user")), Collections.EMPTY_SET, creds2);
        p2 = sstring.getPrivateCredentials();
        try {
            String[] selectArray = { "exits", "Does not exist" };
            Object ObjAr = p2.toArray(selectArray);
            System.out.println(" No Exception in ObjAr- String");
        } catch (SecurityException e) {
            throw new RuntimeException(" Error:  Exception in ObjAr- String!!");
        }
        HashSet creds4 = new HashSet();
        creds4.add("abc");
        creds4.add("def");
        creds4.add("ghi");
        creds4.add(new Integer(1));
        creds4.add("Exists");
        Subject scontain1 = new Subject(false, Collections.singleton(new UnixPrincipal("user")), Collections.EMPTY_SET, creds4);
        Set p3 = scontain1.getPrivateCredentials();
        try {
            Object Obj = new String("Exists");
            if (p3.contains(Obj))
                System.out.println("Contains String cred");
            else
                throw new RuntimeException("Error Does not Contain the stringcred exists");
        } catch (SecurityException e) {
            throw new RuntimeException("Error:Exception!!");
        }
        try {
            Object ObjCred = (Object) creds4.clone();
            if (p3.equals(ObjCred))
                throw new RuntimeException("Error:Equals ObjCred");
            else
                throw new RuntimeException("Error:Does not Equal Objcred");
        } catch (SecurityException e) {
            System.out.println("Should  get an Exception in equals");
        }
        try {
            Object Obj = new Integer(1);
            if (p3.contains(Obj))
                throw new RuntimeException("Error:Contains integer cred");
            else
                throw new RuntimeException("Error:Does not Contain integer cred");
        } catch (SecurityException e) {
            System.out.println("Should get an Exception in contains Integer cred");
        }
        HashSet coll = new HashSet();
        coll.add(new String("abc"));
        coll.add(new String("def"));
        coll.add(new String("Exists"));
        coll.add(new String("Does not Exist"));
        try {
            if (p3.containsAll(coll))
                throw new RuntimeException("Error: Contains the collection");
            else
                System.out.println("Does not Contain the collection");
        } catch (SecurityException e) {
            throw new RuntimeException("Error: Exception in containsAll (string coll)!!");
        }
        coll.remove(new String("Exists"));
        coll.remove(new String("Does not Exist"));
        try {
            if (p3.containsAll(coll))
                System.out.println("Contains the collection");
            else
                throw new RuntimeException("Error:Does not Contain the collection");
        } catch (SecurityException e) {
            throw new RuntimeException("Error: Exception in containsAll (string coll)!!");
        }
        Object Obj = new String("Exists");
        try {
            if (p3.contains(Obj))
                System.out.println("Contains String cred exists");
            else
                System.out.println("Does not Contain String cred exists");
        } catch (SecurityException e) {
            System.out.println("Exception in String cred!!");
        }
        Obj = new String("Does not exist");
        try {
            if (p3.contains(Obj))
                throw new RuntimeException("Error: Contains the String does not exist");
            else
                System.out.println("Does not Contain the String cred Does not exist");
        } catch (SecurityException e) {
            throw new RuntimeException("Error: Exception in Contains!!");
        }
        p3.add(new Integer(2));
        coll.add(new Integer(2));
        p3.add("XYZ");
        System.out.println("Testing Retainall ");
        exceptionCounter = 0;
        iter1 = p3.iterator();
        while (iter1.hasNext()) {
            try {
                Object o = iter1.next();
                System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
            } catch (SecurityException e) {
                System.out.println(" We should get exception");
                System.out.println("Exception!!");
                exceptionCounter++;
            }
        }
        System.out.println(" After the retainall Operation");
        try {
            if (p3.retainAll(coll))
                System.out.println("Retained the collection");
            else
                throw new RuntimeException("Error: RetainAll did not succeed");
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Error: Unexpected Exception in retainAll!");
        }
        iter1 = p3.iterator();
        while (iter1.hasNext()) {
            try {
                Object o = iter1.next();
                System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
            } catch (SecurityException e) {
                exceptionCounter++;
            }
        }
        System.out.println("Retainall collection");
        p3.add(new Integer(3));
        iter1 = p3.iterator();
        while (iter1.hasNext()) {
            try {
                Object o = iter1.next();
                System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
            } catch (SecurityException e) {
                System.out.println("Should get Exception ");
            }
        }
        exceptionCounter = 0;
        HashSet coll2 = new HashSet();
        coll2.add(new String("abc"));
        coll2.add(new Integer(3));
        System.out.println(" before removeall");
        iter1 = p3.iterator();
        exceptionCounter = 0;
        while (iter1.hasNext()) {
            try {
                Object o = iter1.next();
                System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
            } catch (SecurityException e) {
                System.out.println("Expected Exception thrown ");
                exceptionCounter++;
            }
        }
        if (exceptionCounter != 2) {
            throw new RuntimeException("Expected 2 Exceptions; received " + exceptionCounter + "exceptions ");
        }
        try {
            p3.removeAll(coll2);
            System.out.println(" removeall successful! ");
        } catch (SecurityException e) {
            throw new RuntimeException(" Error: removeAll Security Exception!!");
        }
        iter1 = p3.iterator();
        System.out.println(" After removeall");
        exceptionCounter = 0;
        while (iter1.hasNext()) {
            try {
                Object o = iter1.next();
                System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
            } catch (SecurityException e) {
                System.out.println("Expected Exception thrown ");
                exceptionCounter++;
            }
        }
        if (exceptionCounter != 1) {
            throw new RuntimeException("Expected 1 Exceptions; received " + exceptionCounter + "exceptions ");
        }
        try {
            p3.clear();
            System.out.println(" Clear() successful! ");
        } catch (SecurityException e) {
            throw new RuntimeException(" Error: Clear Security Exception!!");
        }
        creds.clear();
        creds.add("abc");
        creds.add("def");
        creds.add("ghi");
        creds.add(new Integer(1));
        Subject s = new Subject(false, Collections.singleton(new UnixPrincipal("user")), Collections.EMPTY_SET, creds);
        try {
            Set privCredSet = s.getPrivateCredentials(char.class);
            if (privCredSet.size() != 0) {
                throw new RuntimeException("Error:String Privcred size should have been 0 and was " + privCredSet.size());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error " + e.toString());
        }
        try {
            Set privCredSet = s.getPrivateCredentials(String.class);
            if (privCredSet.size() != 3) {
                throw new RuntimeException("Error:String Privcred size should have been 2 and was " + privCredSet.size());
            }
            s.getPrivateCredentials().add("XYZ");
            if (privCredSet.size() != 3) {
                throw new RuntimeException("Error:String Privcred size should have been 2 and was " + privCredSet.size());
            }
            s.getPrivateCredentials().remove("XYZ");
            Iterator iter = privCredSet.iterator();
            while (iter.hasNext()) {
                try {
                    Object o = iter.next();
                    System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
                } catch (SecurityException e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexcpected Exception");
        }
        s.getPrivateCredentials().add("XYZ");
        s.getPrivateCredentials().remove("XYZ");
        s.getPrivateCredentials().add(new Integer(2));
        s.getPrivateCredentials().remove(new Integer(2));
        try {
            Set privCredSet1 = s.getPrivateCredentials(Boolean.class);
            if (privCredSet1.size() != 0) {
                throw new RuntimeException("Error:String PrivcredSet1 of Boolean size should have been 0 and was " + privCredSet1.size());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Unexcpected Exception");
        }
        System.out.println("Checked Boolean Creds ");
        try {
            Set privCredSet1 = s.getPrivateCredentials(Integer.class);
            if (privCredSet1.size() != 0) {
                throw new RuntimeException("Error:String PrivcredSet1 of Integer size should have been 0 and was " + privCredSet1.size());
            }
        } catch (SecurityException e) {
            System.out.println("Expected exception");
        }
        System.out.println("Checked Integer Creds ");
        Set privCredSet2 = s.getPrivateCredentials();
        if (privCredSet2.size() != 4) {
            throw new RuntimeException("Error:String PrivcredSet1 size should have been 4 and was " + privCredSet2.size());
        }
        s.getPrivateCredentials().add("XYZ");
        if (privCredSet2.size() != 5) {
            throw new RuntimeException("Error:String PrivcredSet1 size should have been 5 and was " + privCredSet2.size());
        }
        s.getPrivateCredentials().remove("XYZ");
        if (privCredSet2.size() != 4) {
            throw new RuntimeException("String privCredSet2 size should have been  5 and was " + privCredSet2.size());
        }
        System.out.println("Checked remove(String) operation");
        s.getPrivateCredentials().add(Boolean.TRUE);
        s.getPrivateCredentials().add(new Integer(2));
        exceptionCounter = 0;
        iter1 = privCredSet2.iterator();
        while (iter1.hasNext()) {
            try {
                Object o = iter1.next();
                System.out.println(" private creds of class " + o.getClass() + "value is " + o.toString());
            } catch (SecurityException e) {
                System.out.println(" We should get exception");
                System.out.println("Exception!!");
                exceptionCounter++;
            }
        }
        if (exceptionCounter != 3) {
            throw new RuntimeException("Expected number of exception was 3 " + "The actual number was " + exceptionCounter);
        }
        privCredSet2.add(new Integer(3));
        try {
            int hashCode = privCredSet2.hashCode();
        } catch (SecurityException e) {
            System.out.println("hashCode Expected exception");
        }
        System.out.println("Tests completed");
    }
}
