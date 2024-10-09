



import java.util.Properties;

import org.omg.CORBA.ORB;


public class SetDefaultORBTest {

    public static void main(String[] args) {
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("org.omg.CORBA.ORBSingletonClass",
                "com.sun.corba.se.impl.orb.ORBSingleton");
        System.setSecurityManager(new SecurityManager());
        Properties props = new Properties();
        props.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
        ORB orb = ORB.init(args, props);
        Class<?> orbClass = orb.getClass();
        if (orbClass.getName().equals("com.sun.corba.se.impl.orb.ORBImpl")) {
            System.out.println("orbClass is com.sun.corba.se.impl.orb.ORBimpl  as expected");
        } else {
            throw new RuntimeException("com.sun.corba.se.impl.orb.ORBimpl class expected for ORBImpl");
        }
        ORB singletonORB = ORB.init();
        Class<?> singletoneOrbClass = singletonORB.getClass();
        if (singletoneOrbClass.getName().equals("com.sun.corba.se.impl.orb.ORBSingleton")) {
            System.out.println("singeletonOrbClass is com.sun.corba.se.impl.orb.ORBSingleton  as expected");
        } else {
            throw new RuntimeException("com.sun.corba.se.impl.orb.ORBSingleton class expected for ORBSingleton");
        }
    }
}
